package com.fuyouj.sword.database.id;

public interface Generator<T> {
    T next(Object currentId);
}
