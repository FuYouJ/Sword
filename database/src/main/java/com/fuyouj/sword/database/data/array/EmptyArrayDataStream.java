package com.fuyouj.sword.database.data.array;

import java.util.Iterator;
import java.util.List;

import com.fuyouj.sword.database.data.QueryOption;
import com.fuyouj.sword.database.utils.Iterators;
import com.fuyouj.sword.scabard.Lists2;

public class EmptyArrayDataStream<Value> implements ArrayDataStream<Value> {
    public static final EmptyArrayDataStream EMPTY = new EmptyArrayDataStream();

    public static <Value> EmptyArrayDataStream<Value> empty() {
        return (EmptyArrayDataStream<Value>) EMPTY;
    }

    @Override
    public long add(final Value data) {
        return -1;
    }

    @Override
    public List<Long> addAll(final List<Value> values) {
        return Lists2.staticEmpty();
    }

    @Override
    public void addHook(final ArrayDataStreamHook<Value> hook) {

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
    public void deleteByIndex(final long index) {

    }

    @Override
    public boolean fillBufferHolyWithNullTo(final long index) {
        return false;
    }

    @Override
    public long getBufferSize() {
        return 0;
    }

    @Override
    public IndexedValue<Value> getByIndex(final long index) {
        return IndexedValue.empty();
    }

    @Override
    public Class<?> getDataType() {
        return Object.class;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public boolean isEmpty(final long index) {
        return true;
    }

    @Override
    public Iterator<IndexedValue<Value>> iterator(final IndexStream indexStream) {
        return Iterators.empty();
    }

    @Override
    public Iterator<IndexedValue<Value>> iterator(final int skip, final int limit) {
        return Iterators.empty();
    }

    @Override
    public Iterator<IndexedValue<Value>> iterator() {
        return Iterators.empty();
    }

    @Override
    public Iterator<IndexedValue<Value>> iteratorBackward(final long start, final int limit) {
        return Iterators.empty();
    }

    @Override
    public Iterator<IndexedValue<Value>> iteratorForward(final long start, final int limit) {
        return Iterators.empty();
    }

    @Override
    public Iterator<IndexedValue<Value>> iteratorWithDeletes() {
        return Iterators.empty();
    }

    @Override
    public long latestIndex() {
        return -1;
    }

    @Override
    public QueryResult<Value> query(final QueryOption<Value> query) {
        return null;
    }

    @Override
    public void removeHook(final ArrayDataStreamHook<Value> hook) {

    }

    @Override
    public boolean replaceAll(final IndexedValues<Value> values) {
        return false;
    }

    @Override
    public List<Long> replaceAll(final List<Value> values) {
        return Lists2.staticEmpty();
    }

    @Override
    public Iterator<IndexedValue<Value>> reverseIterator() {
        return Iterators.empty();
    }

    @Override
    public boolean setByIndex(final long index, final Value text) {
        return false;
    }

}
