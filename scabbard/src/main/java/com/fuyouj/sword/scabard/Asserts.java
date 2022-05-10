package com.fuyouj.sword.scabard;

import java.util.Collection;

public class Asserts {
    public static void hasArg(final Object object, final String message) {
        if (object == null) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void hasText(final String text, final String message) {
        if (text == null || text.length() == 0 || text.trim().length() == 0) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void isTrue(final boolean condition, final String message) {
        if (!condition) {
            throw new IllegalArgumentException(message);
        }
    }

    public static <T> void notEmpty(final Collection<T> items, final String message) {
        hasArg(items, message);
        if (items.isEmpty()) {
            throw new IllegalArgumentException(message);
        }
    }
}
