package com.fuyouj.sword.database.data.map;

import java.util.List;

import com.fuyouj.sword.database.data.DataStream;
import com.fuyouj.sword.database.data.QueryOption;
import com.fuyouj.sword.database.data.array.QueryResult;

public interface MapDataStream<Key extends Comparable<Key>, Value> extends DataStream {
    int add(KeyedValue<Key, Value> value);

    int bufferCount();

    void clearByKey(Key key);

    void deleteByKey(Key key);

    List<KeyedValue<Key, Value>> getAll();

    Object getByKey(Key key);

    List<KeyedValue<Key, Value>> getByKey(Key from, Key to);

    List<KeyedValue<Key, Value>> getByKeys(List<Key> keys);

    boolean isEmpty(Key key);

    Key latestKey();

    boolean put(Key key, Object value);

    QueryResult<KeyedValue<Key, Value>> query(QueryOption<Value> query);

    void replaceAll(List<KeyedValue<Key, Value>> values);
}
