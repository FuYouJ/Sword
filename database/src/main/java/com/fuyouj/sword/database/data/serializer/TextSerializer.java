package com.fuyouj.sword.database.data.serializer;

import com.fuyouj.sword.database.Serializer;

public class TextSerializer implements Serializer<String> {
    @Override
    public byte[] serialize(final String object) {
        return object.getBytes();
    }
}
