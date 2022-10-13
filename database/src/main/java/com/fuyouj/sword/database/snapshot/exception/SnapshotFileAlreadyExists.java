package com.fuyouj.sword.database.snapshot.exception;

public class SnapshotFileAlreadyExists extends RuntimeException {
    public SnapshotFileAlreadyExists(final String path) {
        super(String.format("snapshot file [%s] already exists", path));
    }
}
