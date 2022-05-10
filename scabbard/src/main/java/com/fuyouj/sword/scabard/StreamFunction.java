package com.fuyouj.sword.scabard;

public interface StreamFunction<T, R> {
    R apply(T t, int index);
}
