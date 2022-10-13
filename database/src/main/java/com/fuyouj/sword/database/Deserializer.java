package com.fuyouj.sword.database;

public interface Deserializer {
    <T> T deserialize(byte[] serialized, Class<T> tClass);

    default <T> T deserialize(byte[] serialized, Class<T> tClass, boolean throwError) {
        return deserialize(serialized, tClass);
    }

    Object deserialize(byte[] serialized);

    default Object deserialize(byte[] serialized, boolean throwError) {
        return deserialize(serialized);
    }
}
