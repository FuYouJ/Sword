package com.fuyouj.sword.database.wal.exception;

public class InvalidWalFileHeader extends RuntimeException {
    public InvalidWalFileHeader(final String message) {
        super(message);
    }
}
