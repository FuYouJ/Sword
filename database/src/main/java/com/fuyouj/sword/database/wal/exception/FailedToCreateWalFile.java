package com.fuyouj.sword.database.wal.exception;

public class FailedToCreateWalFile extends RuntimeException {
    public FailedToCreateWalFile(final String message) {
        super(message);
    }
}
