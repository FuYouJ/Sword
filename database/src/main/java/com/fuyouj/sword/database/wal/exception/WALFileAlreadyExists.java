package com.fuyouj.sword.database.wal.exception;

public class WALFileAlreadyExists extends RuntimeException {
    public WALFileAlreadyExists(final String absoluteName) {
        super(String.format("wal file [%s] already exists", absoluteName));
    }
}
