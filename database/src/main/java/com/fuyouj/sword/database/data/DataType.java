package com.fuyouj.sword.database.data;

import lombok.Getter;

public enum DataType {
    Text(0b0000_0000),
    Int64(0b0000_001),
    Int32(0b0000_010),
    Int16(0b0000_011),
    Int8(0b0000_100);

    @Getter
    private int code;

    DataType(final int code) {
        this.code = code;
    }
}
