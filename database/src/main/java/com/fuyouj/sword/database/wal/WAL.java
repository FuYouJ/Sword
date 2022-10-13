package com.fuyouj.sword.database.wal;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Iterator;

import com.fuyouj.sword.database.Deserializer;
import com.fuyouj.sword.database.PersistentConfiguration;
import com.fuyouj.sword.database.wal.exception.FailedToAppendWal;
import com.fuyouj.sword.database.wal.exception.FailedToCreateWalFile;
import com.fuyouj.sword.database.wal.exception.UnableToAccessWalFile;
import com.fuyouj.sword.database.wal.exception.WALFileAlreadyExists;
import com.fuyouj.sword.scabard.Exceptions2;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class WAL {
    private final long createAt;
    private final File file;
    private final DataOutputStream writer;
    private final DataInputStream reader;
    private final WalHeader header;

    private WALState state = WALState.UnLoaded;
    /**
     * 表示当前还未使用的最新的entryIndex,使用后+1
     */
    private volatile long currentEntryIndex = 0;
    private boolean fileInvalid = false;

    /**
     * create new WAL
     *
     * @param absoluteName
     * @param createAt
     */
    public WAL(final String absoluteName, final long createAt) {
        this.createAt = createAt;
        this.file = createFile(buildLogName(absoluteName, createAt));
        this.writer = createWriter(this.file);
        this.reader = createReader(this.file);
        this.header = new WalHeader(createAt);
        this.appendHeader();
        this.state = WALState.Loaded;
    }

    /**
     * load wal
     *
     * @param file
     * @param createAt
     */
    private WAL(final File file, final long createAt) {
        this.createAt = createAt;
        this.file = file;
        this.writer = createWriter(this.file);
        this.reader = createReader(this.file);
        this.header = WalHeader.read(this.file, this.reader);
    }

    /**
     * 给定一个WAL日志文件，初始化加载到内存中
     *
     * @param walFile 日志文件的地址和描述
     * @return WAL
     */
    public static WAL init(final WalFile walFile) {
        return new WAL(walFile.getFile(), walFile.getTs());
    }

    public long size() {
        if (this.file == null || !Files.exists(this.file.toPath())) {
            return 0;
        }

        try {
            return Files.size(this.file.toPath());
        } catch (IOException e) {
            throw new UnableToAccessWalFile(
                    this.file,
                    String.format("can not check file size because of [%s]",
                            Exceptions2.extractMessage(e)));
        }
    }

    long append(final EntryType entryType, final byte[] data) {
        long nextEntryIndex = this.nextEntryIndex();

        WALEntry.write(this.writer, nextEntryIndex, entryType, data);

        return nextEntryIndex;
    }

    void clean() {
        try {
            this.reader.close();
            this.writer.close();

            this.file.delete();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    long count() {
        return this.currentEntryIndex;
    }

    void fsync() {
        try {
            this.writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    boolean isInvalid() {
        return this.fileInvalid;
    }

    Iterator<Command> toCommandIterator(final Deserializer deserializer, final PersistentConfiguration configuration) {
        return new Iterator<>() {
            private final boolean ignoreError = configuration.isIgnoreErrorWhileReplaying();
            private WALEntry currentEntry = null;

            @Override
            public boolean hasNext() {
                if (fileInvalid) {
                    return false;
                }

                try {
                    return reader.available() > 0;
                } catch (IOException e) {
                    String message = String.format(
                            "Failed to get available bytes from wal because of [%s]", Exceptions2.extractMessage(e)
                    );

                    return handleException(ignoreError, file, message, false);
                }
            }

            @Override
            public Command next() {
                currentEntry = readAndValidEntry(ignoreError);

                if (currentEntry == null) {
                    fileInvalid = true;
                    return null;
                }

                Command command = deserializer.deserialize(currentEntry.getData(), Command.class, !ignoreError);
                if (command != null) {
                    command.type = currentEntry.getEntryType();
                }

                return command;
            }
        };
    }

    private void appendHeader() {
        try {
            this.writer.write(this.header.toBytes());
        } catch (IOException e) {
            throw new FailedToAppendWal(String.format(
                    "Failed to write wal header [%s] because of [%s]",
                    this.file.getAbsolutePath(),
                    e.getMessage()
            ));
        }
    }

    private String buildLogName(final String absoluteName, final long ts) {
        return absoluteName + ".log." + ts;
    }

    private File createFile(final String path) {
        File file = new File(path);

        if (file.exists()) {
            throw new WALFileAlreadyExists(path);
        }

        try {
            if (!file.createNewFile()) {
                throw new FailedToCreateWalFile("failed to create " + path);
            }
        } catch (IOException e) {
            throw new FailedToCreateWalFile("failed to create " + path + ", because of " + e.getMessage());
        }

        return file;
    }

    private DataInputStream createReader(final File file) {
        try {
            return new DataInputStream(new BufferedInputStream(new FileInputStream(file)));
        } catch (FileNotFoundException e) {
            throw new UnableToAccessWalFile(this.file, Exceptions2.extractMessage(e));
        }
    }

    private DataOutputStream createWriter(final File file) {
        try {
            return new DataOutputStream(new BufferedOutputStream(new FileOutputStream(file, true)));
        } catch (IOException e) {
            throw new FailedToCreateWalFile(String.format(
                    "Failed to create writer [%s], because of [%s]",
                    this.file.getName(),
                    e.getMessage()
            ));
        }
    }

    private <T> T handleException(final boolean ignoreError,
                                  final File file,
                                  final String message,
                                  final T returnValue) {
        if (ignoreError) {
            log.error(message);
            return returnValue;
        } else {
            throw new UnableToAccessWalFile(file, message);
        }
    }

    /**
     * @return
     */
    private long nextEntryIndex() {
        return ++this.currentEntryIndex;
    }

    private WALEntry readAndValidEntry(final boolean ignoreError) {
        WALEntry walEntry;

        try {
            walEntry = WALEntry.read(reader);
        } catch (IOException ex) {
            return handleException(ignoreError, this.file, "IOException:" + ex.getMessage(), null);
        }

        if (!walEntry.isValid()) {
            return handleException(ignoreError, this.file, "Wal file is invalid, data must be damaged", null);
        }

        this.currentEntryIndex = walEntry.getEntryIndex();

        return walEntry;
    }
}
