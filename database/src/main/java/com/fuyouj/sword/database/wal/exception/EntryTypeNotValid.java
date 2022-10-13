package com.fuyouj.sword.database.wal.exception;

public class EntryTypeNotValid extends RuntimeException {
    private final byte errorEntryCode;

    public EntryTypeNotValid(final byte errorEntryCode) {
        this.errorEntryCode = errorEntryCode;
    }

    @Override
    public String getMessage() {
        return String.format("Invalid entry code:[%d]", errorEntryCode);
    }
}
