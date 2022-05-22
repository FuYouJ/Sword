package com.fuyouj.sword.database.wal.exception;

import java.io.File;

public class UnableToAccessWalFile extends RuntimeException {
    public UnableToAccessWalFile(final File file, final String message) {
        super(String.format("Failed to read write ahead log [%s], because of [%s]", file.getName(), message));
    }

    public UnableToAccessWalFile(final String message) {
        super(String.format("Failed to read write ahead log, because of [%s]", message));
    }
}
