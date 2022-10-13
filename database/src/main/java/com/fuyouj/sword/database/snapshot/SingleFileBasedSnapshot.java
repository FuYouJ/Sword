package com.fuyouj.sword.database.snapshot;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.fuyouj.sword.database.snapshot.exception.FailedToCreateSnapshotFile;
import com.fuyouj.sword.database.snapshot.exception.UnableToAccessSnapshotFile;
import com.fuyouj.sword.scabard.Exceptions2;

public abstract class SingleFileBasedSnapshot<TData> extends BaseSnapshot<TData> {
    protected OutputStream out;
    protected InputStream in;

    protected SingleFileBasedSnapshot(final String key, final String path, final long createAt) {
        super(key, createFile(path), createAt);
    }

    protected SingleFileBasedSnapshot(final String key, final File file, final long createAt) {
        super(key, file, createAt);
    }

    @Override
    public void clean() {
        this.closeInputStream(this.in);
        this.closeOutputStream(this.out);

        this.in = null;
        this.out = null;

        super.clean();
    }

    @Override
    public void endRead() {
        this.closeInputStream(this.in);
    }

    @Override
    public void endWrite() {
        this.closeOutputStream(this.out);
    }

    protected final void ensureInputStream(final File file) {
        if (this.in != null) {
            return;
        }

        try {
            this.in = createInputStream(file);
        } catch (IOException e) {
            throw new UnableToAccessSnapshotFile(file, Exceptions2.extractMessage(e));
        }
    }

    protected final void ensureOutputStream(final File file) {
        if (this.out != null) {
            return;
        }

        try {
            this.out = createOutStream(file);
        } catch (IOException e) {
            throw new FailedToCreateSnapshotFile(String.format(
                    "Failed to create writer [%s], because of [%s]",
                    file.getName(),
                    e.getMessage()
            ));
        }
    }
}
