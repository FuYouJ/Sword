package com.fuyouj.sword.database.wal.exception;

import java.io.File;

public class UnableToAccessWalFile extends RuntimeException {
    public UnableToAccessWalFile(final File file, final String message) {
        super(String.format("Failed to access wal file [%s], because of [%s]", file.getName(), message));
    }
}
