package com.fuyouj.sword.database.snapshot.exception;

public class FailedToCreateSnapshotFile extends RuntimeException {
    public FailedToCreateSnapshotFile(final String message) {
        super(message);
    }
}
