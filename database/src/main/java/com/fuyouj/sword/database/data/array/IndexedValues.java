package com.fuyouj.sword.database.data.array;

import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/*
一列数据
 */
public interface IndexedValues<Value> extends Iterable<IndexedValue<Value>> {

    default long getMaxIndex() {
        long maxIndex = 0;

        for (IndexedValue<Value> indexedValue : this) {
            final long currentIndexValue = indexedValue.getIndex();
            if (currentIndexValue > maxIndex) {
                maxIndex = currentIndexValue;
            }
        }

        return maxIndex;
    }

    default long getMinIndex() {
        long minIndex = 0;

        for (IndexedValue<Value> indexedValue : this) {
            final long currentIndexValue = indexedValue.getIndex();
            if (currentIndexValue < minIndex) {
                minIndex = currentIndexValue;
            }
        }

        return minIndex;
    }

    int getSize();

    default Stream<IndexedValue<Value>> stream() {
        return StreamSupport.stream(this.spliterator(), false);
    }

    long[] toIndexArray();

    Value[] toValueArray(Class<Value> tClass);
}

