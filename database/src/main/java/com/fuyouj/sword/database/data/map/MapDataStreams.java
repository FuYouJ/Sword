package com.fuyouj.sword.database.data.map;

import java.util.List;
import java.util.Map;

import com.fuyouj.sword.database.data.array.AddOrUpdateResult;
import com.fuyouj.sword.scabard.Bytes;

public interface MapDataStreams<Key extends Comparable<Key>> {
    int add(Key key, Map<String, Object> valueMap);

    <Value> void addAll(String field, List<KeyedValue<Key, Value>> values, Class<Value> tClass);

    AddOrUpdateResult addOrUpdate(Key key, Map<String, Object> valueMap);

    int count();

    Map<String, Object> get(Key key);

    long getBufferSize();

    Map<String, Object> getByPrimaryValue(Object primaryValue);

    String getPrimaryKey();

    default String getReadableBufferSize() {
        return Bytes.toReadableString(getBufferSize());
    }

    <Value> List<KeyedValue<Key, Value>> getStreamValue(String field, Class<Value> tClass);

    boolean put(Key key, Map<String, Object> value);

    boolean remove(Key key);

}
