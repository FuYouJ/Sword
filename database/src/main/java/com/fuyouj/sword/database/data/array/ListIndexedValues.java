package com.fuyouj.sword.database.data.array;

import java.lang.reflect.Array;
import java.util.Iterator;
import java.util.List;

import com.fuyouj.sword.scabard.Lists2;

public class ListIndexedValues<Value> implements IndexedValues<Value> {
    private final List<IndexedValue<Value>> values;

    public ListIndexedValues(final List<IndexedValue<Value>> values) {
        this.values = values;
    }

    @Override
    public int getSize() {
        return values.size();
    }

    @Override
    public Iterator<IndexedValue<Value>> iterator() {
        return values.iterator();
    }

    @Override
    public long[] toIndexArray() {
        final long[] longs = new long[Lists2.size(values)];

        for (int index = 0; index < Lists2.size(values); index++) {
            longs[index] = values.get(index).getIndex();
        }
        return longs;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Value[] toValueArray(final Class<Value> tClass) {
        final int size = Lists2.size(values);

        final Value[] newValueArray = (Value[]) Array.newInstance(tClass, size);

        for (int index = 0; index < size; index++) {
            newValueArray[index] = values.get(index).getValue();
        }

        return newValueArray;
    }
}
