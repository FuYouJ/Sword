package com.fuyouj.sword.database.data.filter;

import java.util.Optional;

import com.fuyouj.sword.scabard.Strings;

public enum OrderOp {
    DESC, ASC;

    public static Optional<OrderOp> fromString(final String str) {
        if (Strings.isBlank(str)) {
            return Optional.empty();
        }

        try {
            return Optional.of(valueOf(str.toUpperCase()));
        } catch (IllegalArgumentException e) {
            return Optional.empty();
        }
    }
}
