package com.fuyouj.sword.database.snapshot;

import java.util.List;

public class SnapshotFiles {
    private final List<SnapshotFile> snapshotFiles;

    private SnapshotFiles(final List<SnapshotFile> snapshotFiles) {
        this.snapshotFiles = snapshotFiles;
        this.snapshotFiles.sort(SnapshotFile::compareTo);
    }

    public static SnapshotFiles of(final List<SnapshotFile> snapshotFiles) {
        return new SnapshotFiles(snapshotFiles);
    }

    public List<SnapshotFile> getSnapshotFiles() {
        return snapshotFiles;
    }

}
