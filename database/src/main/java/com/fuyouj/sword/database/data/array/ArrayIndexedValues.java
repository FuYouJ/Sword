package com.fuyouj.sword.database.data.array;

import java.util.Iterator;

import com.fuyouj.sword.scabard.Asserts;

public class ArrayIndexedValues<Value> implements IndexedValues<Value> {
    private final long[] indexes;
    private final Value[] values;

    public ArrayIndexedValues(final long[] indexes, final Value[] values) {
        Asserts.hasArg(indexes, "indexes must not be null");
        Asserts.hasArg(indexes, "values must not be null");
        Asserts.isTrue(indexes.length == values.length, "indexes length must equal to length of values");

        this.indexes = indexes;
        this.values = values;
    }

    @Override
    public int getSize() {
        return indexes.length;
    }

    @Override
    public Iterator<IndexedValue<Value>> iterator() {
        return new Iterator<>() {
            private int cursor = -1;
            private SimpleIndexedValue<Value> currentValueHolder = null;

            @Override
            public boolean hasNext() {
                return cursor < indexes.length - 1;
            }

            @Override
            public IndexedValue<Value> next() {
                cursor++;

                if (currentValueHolder == null) {
                    currentValueHolder = new SimpleIndexedValue<>(indexes[cursor], values[cursor]);
                } else {
                    currentValueHolder.setIndex(indexes[cursor]);
                    currentValueHolder.setValue(values[cursor]);
                }

                return currentValueHolder;
            }
        };
    }

    @Override
    public long[] toIndexArray() {
        return this.indexes;
    }

    @Override
    public Value[] toValueArray(final Class<Value> tClass) {
        return this.values;
    }
}

