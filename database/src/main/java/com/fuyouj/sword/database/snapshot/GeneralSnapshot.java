package com.fuyouj.sword.database.snapshot;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class GeneralSnapshot extends SingleFileBasedSnapshot<byte[]> {

    private final SnapshotHeader header;
    private DataOutputStream writer;
    private DataInputStream reader;

    private SnapshotState state = SnapshotState.UnLoaded;

    public GeneralSnapshot(final String key, final String absoluteName, final long createAt) {
        super(key, buildSnapshotName(absoluteName, createAt), createAt);

        this.ensureOutputStream(this.file);
        this.header = SnapshotHeader.write(this.file, this.writer, createAt);

        this.state = SnapshotState.Loaded;
    }

    public GeneralSnapshot(final String key, final File file, final long createAt) {
        super(key, file, createAt);

        this.ensureInputStream(file);
        this.header = SnapshotHeader.read(this.file, this.reader);

        this.state = SnapshotState.Loaded;
    }

    public static GeneralSnapshot init(final SnapshotFile snapshotFile) {
        return new GeneralSnapshot(snapshotFile.getKey(), snapshotFile.getFile(), snapshotFile.getTs());
    }

    @Override
    protected InputStream createInputStream(final File file) throws IOException {
        this.reader = new DataInputStream(new BufferedInputStream(new FileInputStream(file)));
        return this.reader;
    }

    @Override
    protected OutputStream createOutStream(final File file) throws IOException {
        this.writer = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(file)));
        return this.writer;
    }

    @Override
    protected byte[] readAndValidEntry(final boolean ignoreError) {
        SnapshotEntry snapshotEntry;

        try {
            snapshotEntry = SnapshotEntry.read(this.reader);
        } catch (IOException ex) {
            return handleException(ignoreError, this.file, "IOException:" + ex.getMessage(), null);
        }

        if (!snapshotEntry.isValid()) {
            return handleException(ignoreError, this.file, "Snapshot file is invalid, data must be damaged", null);
        }

        return snapshotEntry.getData();
    }

    @Override
    protected void write(final String key, final byte[] data) {
        SnapshotEntry.write(this.writer, data);
        this.fsync(this.writer);
    }
}
