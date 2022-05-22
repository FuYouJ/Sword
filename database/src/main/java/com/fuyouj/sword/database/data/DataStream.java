package com.fuyouj.sword.database.data;

import java.util.Map;

import com.fuyouj.sword.scabard.Bytes;
import com.fuyouj.sword.scabard.Maps2;

public interface DataStream extends AutoCloseable {
    void clear();

    void close();

    int count();

    default boolean dataTypeAligned(Class<?> dataType) {
        return getDataType() == dataType;
    }

    long getBufferSize();

    Class<?> getDataType();

    default Map<String, Object> getMemoryLayout() {
        return Maps2.of(getName(), getBufferSize());
    }

    String getName();

    default String getReadableBufferSize() {
        return Bytes.toReadableString(getBufferSize());
    }

    default Map<String, Object> getReadableMemoryLayout() {
        return Maps2.of(getName(), getReadableBufferSize());
    }
}
