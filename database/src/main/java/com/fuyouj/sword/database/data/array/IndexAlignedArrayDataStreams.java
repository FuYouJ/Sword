package com.fuyouj.sword.database.data.array;

import java.util.Map;

import com.fuyouj.sword.scabard.Bytes;

public interface IndexAlignedArrayDataStreams extends ArrayDataStreams {
    int add(Map<String, Object> value);

    int count();

    Map<String, Object> get(int index);

    long getBufferSize();

    String getPrimaryKey();

    default String getReadableBufferSize() {
        return Bytes.toReadableString(getBufferSize());
    }

    boolean put(int index, Map<String, Object> value);

    boolean remove(int index);
}
