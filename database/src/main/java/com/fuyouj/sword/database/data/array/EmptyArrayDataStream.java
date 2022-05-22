package com.fuyouj.sword.database.data.array;

import java.util.List;

import com.fuyouj.sword.database.data.QueryOption;
import com.fuyouj.sword.scabard.NotImplementedYet;

public class EmptyArrayDataStream<Value> implements ArrayDataStream<Value> {
    public static final EmptyArrayDataStream EMPTY = new EmptyArrayDataStream();

    public static <Value> EmptyArrayDataStream<Value> empty() {
        return (EmptyArrayDataStream<Value>) EMPTY;
    }

    @Override
    public int add(final Value data) {
        return -1;
    }

    @Override
    public int bufferCount() {
        return 0;
    }

    @Override
    public void clear() {

    }

    @Override
    public void close() {

    }

    @Override
    public int count() {
        return 0;
    }

    @Override
    public boolean fillBufferHolyWithNullTo(final int index) {
        return false;
    }

    @Override
    @NotImplementedYet
    public List<Value> getAll() {
        return null;
    }

    @Override
    public long getBufferSize() {
        return 0;
    }

    @Override
    public Value getByIndex(final int index) {
        return null;
    }

    @Override
    public List<Value> getByIndexes(final List<Integer> indexes) {
        return null;
    }

    @Override
    public List<Value> getByIndexRange(final int from, final int to) {
        return null;
    }

    @Override
    public Class getDataType() {
        return null;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public boolean isEmpty(final int index) {
        return true;
    }

    @Override
    public int latestIndex() {
        return -1;
    }

    @Override
    public void put(final List<Value> values) {

    }

    @Override
    public QueryResult<Value> query(final QueryOption<Value> query) {
        return null;
    }

    @Override
    public void removeByIndex(final int index) {

    }

    @Override
    public boolean setByIndex(final int index, final Value text) {
        return false;
    }
}
