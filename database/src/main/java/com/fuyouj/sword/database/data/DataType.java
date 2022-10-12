package com.fuyouj.sword.database.data;

import lombok.Getter;

public enum DataType {
    Text((byte) 0),
    Int64((byte) 1);

    @Getter
    private final byte code;

    DataType(final byte code) {
        this.code = code;
    }
}
