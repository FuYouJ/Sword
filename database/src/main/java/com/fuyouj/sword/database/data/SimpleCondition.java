package com.fuyouj.sword.database.data;

import lombok.Getter;

public class SimpleCondition<T> implements Condition<T> {
    @Getter
    private final Operator op;
    @Getter
    private final T value;

    private SimpleCondition(final Operator op, final T value) {
        this.op = op;
        this.value = value;
    }

    public static <T> SimpleCondition<T> eq(final T value) {
        return new SimpleCondition<>(Operator.EQ, value);
    }

    public static <T> SimpleCondition<T> gt(final T value) {
        return new SimpleCondition<>(Operator.GT, value);
    }

    public static <T> SimpleCondition<T> gte(final T value) {
        return new SimpleCondition<>(Operator.GTE, value);
    }

    public static <T> SimpleCondition<T> like(final T value) {
        return new SimpleCondition<>(Operator.LIKE, value);
    }

    public static <T> SimpleCondition<T> lt(final T value) {
        return new SimpleCondition<>(Operator.LT, value);
    }

    public static <T> SimpleCondition<T> lte(final T value) {
        return new SimpleCondition<>(Operator.LTE, value);
    }

    public static <T> SimpleCondition<T> ne(final T value) {
        return new SimpleCondition<>(Operator.NE, value);
    }
}
