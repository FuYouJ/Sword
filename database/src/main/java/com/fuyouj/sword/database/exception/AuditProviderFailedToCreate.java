package com.fuyouj.sword.database.exception;

public class AuditProviderFailedToCreate extends RuntimeException {
    public AuditProviderFailedToCreate(final String message) {
        super(message);
    }
}
