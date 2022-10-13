package com.fuyouj.sword.database.snapshot.exception;

import java.io.File;

public class UnableToAccessSnapshotFile extends RuntimeException {
    public UnableToAccessSnapshotFile(final File file, final String message) {
        super(String.format("Failed to access snapshot [%s], because of [%s]", file.getName(), message));
    }
}
