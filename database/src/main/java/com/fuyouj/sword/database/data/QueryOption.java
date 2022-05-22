package com.fuyouj.sword.database.data;

public class QueryOption<T> {
    private Condition<T> condition;
    private int limit;
    private Sort sort;
}
