package com.fuyouj.sword.database.snapshot;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.fuyouj.sword.database.snapshot.exception.FailedToCreateSnapshotFile;
import com.fuyouj.sword.database.snapshot.exception.UnableToAccessSnapshotFile;
import com.fuyouj.sword.scabard.Exceptions2;

public abstract class MultiFilesBasedSnapshot<TData> extends BaseSnapshot<TData> {
    protected static final int WRITE_WORKER_COUNT = Runtime.getRuntime().availableProcessors() * 2 + 1;
    private static final String FINISH_FILE = "fin";

    protected MultiFilesBasedSnapshot(final String key, final String path, final long createAt) {
        super(key, createFolder(path), createAt);
    }

    protected MultiFilesBasedSnapshot(final String key, final File folder, final long createAt) {
        super(key, folder, createAt);
    }

    protected final InputStream ensureInputStream(final File file) {
        try {
            return createInputStream(file);
        } catch (IOException e) {
            throw new UnableToAccessSnapshotFile(file, Exceptions2.extractMessage(e));
        }
    }

    protected final OutputStream ensureOutputStream(final File file) {
        try {
            return createOutStream(file);
        } catch (IOException e) {
            throw new FailedToCreateSnapshotFile(String.format(
                    "Failed to create writer [%s], because of [%s]",
                    file.getName(),
                    e.getMessage()
            ));
        }
    }

    protected boolean hasFinishFile() {
        return new File(this.file, FINISH_FILE).exists();
    }

    protected boolean isFinishFile(final File file) {
        return file.getName().equals(FINISH_FILE);
    }

    protected void writeFinishFile() {
        FileOutputStream writer = null;
        File finFile = null;
        try {
            finFile = createFile(this.file, FINISH_FILE);
            writer = (FileOutputStream) this.ensureOutputStream(finFile);

            writer.write(FINISH_FILE.getBytes());
        } catch (IOException e) {
            throw new FailedToCreateSnapshotFile(String.format(
                    "Failed to create writer [%s], because of [%s]",
                    finFile.getName(),
                    e.getMessage()
            ));
        } finally {
            this.closeOutputStream(writer);
        }
    }

}
