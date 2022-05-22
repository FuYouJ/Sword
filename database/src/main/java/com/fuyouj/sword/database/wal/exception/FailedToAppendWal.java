package com.fuyouj.sword.database.wal.exception;

public class FailedToAppendWal extends RuntimeException {
    public FailedToAppendWal(final String message) {
        super(message);
    }
}
