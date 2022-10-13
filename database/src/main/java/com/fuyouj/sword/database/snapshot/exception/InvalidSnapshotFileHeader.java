package com.fuyouj.sword.database.snapshot.exception;

public class InvalidSnapshotFileHeader extends RuntimeException {
    public InvalidSnapshotFileHeader(final String message) {
        super(message);
    }
}
