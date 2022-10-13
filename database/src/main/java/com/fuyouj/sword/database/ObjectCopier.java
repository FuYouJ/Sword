package com.fuyouj.sword.database;

public interface ObjectCopier {
    <T> T deepCopy(T item);
}
