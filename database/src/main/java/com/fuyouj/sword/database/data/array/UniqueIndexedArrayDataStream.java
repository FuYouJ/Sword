package com.fuyouj.sword.database.data.array;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.fuyouj.sword.database.data.QueryOption;
import com.fuyouj.sword.database.exception.Exceptions;
import com.fuyouj.sword.database.exception.ItemDuplicated;
import com.fuyouj.sword.scabard.Asserts;
import com.fuyouj.sword.scabard.Lists2;
import com.fuyouj.sword.scabard.NotImplementedYet;

public class UniqueIndexedArrayDataStream implements ArrayDataStream<String>, ArrayDataStreamWrapper {
    private final ArrayDataStream<String> stream;
    private final Map<String, Long> uniqueIndex;

    public UniqueIndexedArrayDataStream(final ArrayDataStream<String> stream) {
        this.stream = stream;
        this.uniqueIndex = new ConcurrentHashMap<>();

        this.buildUniqueIndex();
    }

    @Override
    @NotImplementedYet(comments = "需要返回的是新增行的序号，而非vector的容量")
    public long add(final String value) {
        Asserts.hasArg(value, "unique index array can NOT add null value");

        final Long theIndex = uniqueIndex.get(value);
        if (theIndex != null) {
            throw duplicateException(value, theIndex);
        }

        final long addedIndex = this.stream.add(value);
        if (addedIndex >= 0) {
            this.uniqueIndex.put(value, addedIndex);
        }

        return addedIndex;
    }

    @Override
    public List<Long> addAll(final List<String> strings) {
        final List<Long> indexes = Lists2.fixed(strings.size());

        for (String string : strings) {
            indexes.add(add(string));
        }

        return indexes;
    }

    @Override
    public void addHook(final ArrayDataStreamHook<String> hook) {
        this.stream.addHook(hook);
    }

    @Override
    public int bufferCount() {
        return this.stream.bufferCount();
    }

    @Override
    public void clear() {
        this.stream.clear();
        this.uniqueIndex.clear();
    }

    @Override
    public void close() {
        this.stream.close();
        this.uniqueIndex.clear();
    }

    @Override
    public int count() {
        return this.stream.count();
    }

    @Override
    public void deleteByIndex(final long index) {
        IndexedValue<String> indexedValue = this.stream.getByIndex(index);
        if (indexedValue.exists()) {
            this.stream.deleteByIndex(indexedValue.getIndex());
            this.uniqueIndex.remove(indexedValue.getValue());
        }
    }

    @Override
    public boolean fillBufferHolyWithNullTo(final long index) {
        return this.stream.fillBufferHolyWithNullTo(index);
    }

    @Override
    public long getBufferSize() {
        return this.stream.getBufferSize();
    }

    @Override
    public IndexedValue<String> getByIndex(final long index) {
        return this.stream.getByIndex(index);
    }

    @Override
    public ArrayDataStream<?> getDataStream() {
        return this.stream;
    }

    @Override
    public Class<?> getDataType() {
        return this.stream.getDataType();
    }

    public long getIndexByPrimaryValue(final String primaryValue) {
        return uniqueIndex.getOrDefault(primaryValue, -1L);
    }

    @Override
    public String getName() {
        return this.stream.getName();
    }

    @Override
    public boolean isEmpty(final long index) {
        return this.stream.isEmpty(index);
    }

    @Override
    public boolean isPrimaryKey() {
        return true;
    }

    @Override
    public Iterator<IndexedValue<String>> iterator(final IndexStream indexStream) {
        return this.stream.iterator(indexStream);
    }

    @Override
    public Iterator<IndexedValue<String>> iterator(final int skip, final int limit) {
        return this.stream.iterator(skip, limit);
    }

    @Override
    public Iterator<IndexedValue<String>> iterator() {
        return this.stream.iterator();
    }

    @Override
    public Iterator<IndexedValue<String>> iteratorBackward(final long start, final int limit) {
        return this.stream.iteratorBackward(start, limit);
    }

    @Override
    public Iterator<IndexedValue<String>> iteratorForward(final long start, final int limit) {
        return this.stream.iteratorForward(start, limit);
    }

    @Override
    public Iterator<IndexedValue<String>> iteratorWithDeletes() {
        return this.stream.iteratorWithDeletes();
    }

    @Override
    public long latestIndex() {
        return this.stream.latestIndex();
    }

    @Override
    public QueryResult<String> query(final QueryOption<String> query) {
        return this.stream.query(query);
    }

    @Override
    public void removeHook(final ArrayDataStreamHook<String> hook) {
        this.stream.removeHook(hook);
    }

    @Override
    public boolean replaceAll(final IndexedValues<String> values) {
        final boolean success = this.stream.replaceAll(values);
        if (success) {
            this.uniqueIndex.clear();
            if (values != null) {
                for (IndexedValue<String> value : values) {
                    this.uniqueIndex.put(value.getValue(), value.getIndex());
                }
            }
        }
        return success;
    }

    @Override
    public List<Long> replaceAll(final List<String> values) {
        final List<Long> indexes = this.stream.replaceAll(values);

        this.uniqueIndex.clear();
        int currentIndex = -1;
        for (Long index : indexes) {
            currentIndex++;
            if (index == null || index < 0) {
                continue;
            }

            this.uniqueIndex.put(values.get(currentIndex), indexes.get(currentIndex));
        }

        return indexes;
    }

    @Override
    public Iterator<IndexedValue<String>> reverseIterator() {
        return this.stream.reverseIterator();
    }

    @Override
    public boolean setByIndex(final long index, final String value) {
        Long theIndex = this.uniqueIndex.get(value);
        if (theIndex != null) {
            //与原值相同，忽略
            if (theIndex == index) {
                return true;
            } else {
                throw duplicateException(value, theIndex);
            }
        }

        String oldValue = this.stream.getByIndex(index).getValue();
        final boolean updated = this.stream.setByIndex(index, value);
        if (updated) {
            if (oldValue != null) {
                this.uniqueIndex.remove(oldValue);
            }

            this.uniqueIndex.put(value, index);
        }

        return updated;
    }

    private void buildUniqueIndex() {
        for (IndexedValue<String> indexedValue : stream) {
            this.uniqueIndex.put(indexedValue.getValue(), indexedValue.getIndex());
        }
    }

    private ItemDuplicated duplicateException(final String duplicatedValue, final Object conflictKey) {
        return Exceptions.duplicated(
                String.format("duplicated primary value:[%s],conflictKey:[%s]", duplicatedValue, conflictKey),
                conflictKey, duplicatedValue
        );
    }

}
