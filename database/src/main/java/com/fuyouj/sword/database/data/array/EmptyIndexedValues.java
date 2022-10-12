package com.fuyouj.sword.database.data.array;

import java.lang.reflect.Array;
import java.util.Iterator;

public class EmptyIndexedValues<Value> implements IndexedValues<Value> {
    private static final IndexedValues EMPTY = new EmptyIndexedValues();

    public static <Value> IndexedValues<Value> empty() {
        return (IndexedValues<Value>) EMPTY;
    }

    @Override
    public int getSize() {
        return 0;
    }

    @Override
    public Iterator<IndexedValue<Value>> iterator() {
        return new Iterator<>() {
            @Override
            public boolean hasNext() {
                return false;
            }

            @Override
            public IndexedValue<Value> next() {
                return null;
            }
        };
    }

    @Override
    public long[] toIndexArray() {
        return new long[0];
    }

    @Override
    @SuppressWarnings("unchecked")
    public Value[] toValueArray(final Class<Value> tClass) {
        return (Value[]) Array.newInstance(tClass, 0);
    }
}

