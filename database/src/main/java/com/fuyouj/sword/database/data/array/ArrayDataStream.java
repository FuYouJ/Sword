package com.fuyouj.sword.database.data.array;

import java.util.List;

import com.fuyouj.sword.database.data.DataStream;
import com.fuyouj.sword.database.data.QueryOption;

public interface ArrayDataStream<Value> extends DataStream {
    int add(Value data);

    int bufferCount();

    boolean fillBufferHolyWithNullTo(int index);

    List<Value> getAll();

    Value getByIndex(int index);

    List<Value> getByIndexRange(int from, int to);

    List<Value> getByIndexes(List<Integer> indexes);

    boolean isEmpty(int index);

    int latestIndex();

    void put(List<Value> values);

    QueryResult<Value> query(QueryOption<Value> query);

    void removeByIndex(int index);

    boolean setByIndex(int index, Value text);
}
