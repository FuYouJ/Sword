package com.fuyouj.sword.database.id;

import com.fuyouj.sword.database.exception.Exceptions;

public class Generators {
    private static final Generator<String> STRING_GENERATOR = new SnowflakeStringGenerator();
    private static final Generator<Long> LONG_GENERATOR = new SnowflakeLongGenerator();

    public static <T> Generator<T> select(final Class<?> tClass) {
        if (tClass == String.class) {
            return (Generator<T>) STRING_GENERATOR;
        }

        if (tClass == Long.class) {
            return (Generator<T>) LONG_GENERATOR;
        }

        throw Exceptions.idMustBeStringOrLong();
    }
}
