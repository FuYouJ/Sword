package com.fuyouj.sword.scabard.error;

public interface ErrorCode {

    default String getCode() {
        return name().replace('_', '.');
    }

    String name();
}
