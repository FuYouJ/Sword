package com.fuyouj.sword.database;

public interface Deserializer {
    <T> T deserialize(byte[] serialized, Class<T> tClass);

    Object deserialize(byte[] serialized);
}
