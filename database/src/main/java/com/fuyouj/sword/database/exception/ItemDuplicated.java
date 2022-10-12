package com.fuyouj.sword.database.exception;

import lombok.Getter;

public class ItemDuplicated extends RuntimeException {
    @Getter
    private final Object conflictKey;
    @Getter
    private final Object duplicatedValue;

    public ItemDuplicated(final String message, final Object conflictKey, final Object duplicatedValue) {
        super(message);
        this.conflictKey = conflictKey;
        this.duplicatedValue = duplicatedValue;
    }
}

