package com.fuyouj.sword.database.object;

public interface Matcher<T> {
    boolean match(T item);
}
