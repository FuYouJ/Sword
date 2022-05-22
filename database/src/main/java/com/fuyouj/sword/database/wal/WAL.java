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
import java.util.Iterator;

import com.fuyouj.sword.database.Deserializer;
import com.fuyouj.sword.database.wal.exception.FailedToAppendWal;
import com.fuyouj.sword.database.wal.exception.FailedToCreateWalFile;
import com.fuyouj.sword.database.wal.exception.UnableToAccessWalFile;
import com.fuyouj.sword.database.wal.exception.WALFileAlreadyExists;
import com.fuyouj.sword.scabard.Exceptions2;

public final class WAL {
    private final File file;
    private final DataOutputStream writer;
    private final WalHeader header;
    private final long firstEntryIndex;
    private final DataInputStream reader;
    private WALState state = WALState.UnLoaded;
    /**
     * 表示当前还未使用的最新的entryIndex,使用后+1
     */
    private long currentEntryIndex = 0;

    /**
     * create new WAL
     *
     * @param absoluteName
     * @param firstEntryIndex
     */
    public WAL(final String absoluteName, final long firstEntryIndex) {
        this.firstEntryIndex = firstEntryIndex;
        this.file = createFile(buildLogName(absoluteName, firstEntryIndex));
        this.writer = createWriter(this.file);
        this.reader = createReader(this.file);
        this.header = new WalHeader(firstEntryIndex);
        this.appendBytes(this.header.toBytes());
        this.state = WALState.Loaded;
    }

    /**
     * load wal
     *
     * @param file
     * @param firstEntryIndex
     */
    private WAL(final File file, final long firstEntryIndex) {
        this.firstEntryIndex = firstEntryIndex;
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
        return new WAL(walFile.getFile(), walFile.getFirstEntryIndex());
    }

    public long append(final EntryType entryType, final byte[] data) {
        long nextEntryIndex = this.nextEntryIndex();

        WALEntry.write(this.writer, nextEntryIndex, entryType, data);

        return nextEntryIndex;
    }

    public void fsync() {
        try {
            this.writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getAbsoluteFileName() {
        return this.file.getAbsolutePath();
    }

    Iterator<Command> toCommandIterator(final Deserializer deserializer) {
        return new Iterator<>() {
            private WALEntry currentEntry = readAndValidEntry();

            @Override
            public boolean hasNext() {
                return currentEntry != null;
            }

            @Override
            public Command next() {
                Command command = deserializer.deserialize(currentEntry.getData(), Command.class);
                command.type = currentEntry.getEntryType();

                currentEntry = readAndValidEntry();

                return command;
            }
        };
    }

    private void appendBytes(final byte[] bytes) {
        try {
            this.writer.write(bytes);
        } catch (IOException e) {
            throw new FailedToAppendWal(String.format(
                    "Failed to append log to wal [%s] because of [%s]",
                    this.file.getAbsolutePath(),
                    e.getMessage()
            ));
        }
    }

    private String buildLogName(final String absoluteName, final long firstEntryIndex) {
        return absoluteName + ".log." + firstEntryIndex;
    }

    private File createFile(final String absoluteName) {
        File file = new File(absoluteName);

        if (file.exists()) {
            throw new WALFileAlreadyExists(absoluteName);
        }

        try {
            if (!file.createNewFile()) {
                throw new FailedToCreateWalFile("failed to create " + absoluteName);
            }
        } catch (IOException e) {
            throw new FailedToCreateWalFile("failed to create " + absoluteName + ", because of " + e.getMessage());
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

    /**
     * @return
     */
    private long nextEntryIndex() {
        return ++this.currentEntryIndex;
    }

    private WALEntry readAndValidEntry() {
        WALEntry walEntry = WALEntry.read(reader);

        if (!walEntry.isValid()) {
            return null;
        }

        this.currentEntryIndex = walEntry.getEntryIndex();

        return walEntry;
    }

    private enum WALState {
        Loaded, UnLoaded
    }
}
