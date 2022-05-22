package com.fuyouj.sword.database.exception;

public class ItemIdMustBeStringOrLong extends RuntimeException {
    public ItemIdMustBeStringOrLong() {
        super("Item @Id must be String or Long");
    }
}
