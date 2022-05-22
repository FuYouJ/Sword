package com.fuyouj.sword.database.exception;

public class WalFileNameInvalid extends RuntimeException {
    public WalFileNameInvalid(final String filename) {
        super(String.format("wal file with name [%s] is invalid, it must be something like {keyOfTheResource}.log"
                + ".{firstEntryIndex}", filename));
    }
}
