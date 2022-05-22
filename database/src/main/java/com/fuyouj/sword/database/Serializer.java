package com.fuyouj.sword.database;

public interface Serializer<T> {
    byte[] serialize(T object);
}
