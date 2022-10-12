package com.fuyouj.sword.database.data.array;

import java.util.Iterator;
import java.util.List;

import com.fuyouj.sword.database.data.DataStream;
import com.fuyouj.sword.database.data.QueryOption;

public interface ArrayDataStream<Value> extends DataStream, Iterable<IndexedValue<Value>> {
    long add(Value data);

    List<Long> addAll(List<Value> values);

    void addHook(ArrayDataStreamHook<Value> hook);

    int bufferCount();

    /**
     * 删除index对应的格子
     *
     * @param index 格子的index
     */
    void deleteByIndex(long index);

    boolean fillBufferHolyWithNullTo(long index);

    IndexedValue<Value> getByIndex(long index);

    boolean isEmpty(long index);

    default boolean isPrimaryKey() {
        return false;
    }

    Iterator<IndexedValue<Value>> iterator(IndexStream indexStream);

    Iterator<IndexedValue<Value>> iterator(int skip, int limit);

    Iterator<IndexedValue<Value>> iteratorBackward(long start, int limit);

    Iterator<IndexedValue<Value>> iteratorForward(long start, int limit);

    Iterator<IndexedValue<Value>> iteratorWithDeletes();

    long latestIndex();

    QueryResult<Value> query(QueryOption<Value> query);

    void removeHook(ArrayDataStreamHook<Value> hook);

    boolean replaceAll(IndexedValues<Value> values);

    List<Long> replaceAll(List<Value> values);

    Iterator<IndexedValue<Value>> reverseIterator();

    boolean setByIndex(long index, Value text);
}
