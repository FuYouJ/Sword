package com.fuyouj.sword.database.snapshot.exception;

public class FailedToWriteSnapshot extends RuntimeException {
    public FailedToWriteSnapshot(final String message) {
        super(message);
    }
}
