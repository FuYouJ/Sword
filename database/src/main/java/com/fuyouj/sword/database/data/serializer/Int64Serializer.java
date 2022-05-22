package com.fuyouj.sword.database.data.serializer;

import com.fuyouj.sword.database.Serializer;

public class Int64Serializer implements Serializer<Long> {
    @Override
    public byte[] serialize(final Long object) {
        return new byte[0];
    }
}
