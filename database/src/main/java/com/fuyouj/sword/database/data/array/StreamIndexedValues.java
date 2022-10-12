package com.fuyouj.sword.database.data.array;

import java.lang.reflect.Array;
import java.util.Iterator;
import java.util.List;

/**
 * 提供一列数据的跌代器
 *
 * @param <T>
 */
public abstract class StreamIndexedValues<T> implements IndexedValues<T> {

    protected final ArrayDataStream<T> dataStream;
    protected int size;

    private StreamIndexedValues(final ArrayDataStream<T> dataStream) {
        this.dataStream = dataStream;
    }

    public static <T> IndexedValues<T> backwardStream(final ArrayDataStream<T> dataStream, final long start, final int limit) {
        return new BackwardValues<>(dataStream, start, limit);
    }

    public static <T> IndexedValues<T> forwardStream(final ArrayDataStream<T> dataStream, final long start, final int limit) {
        return new ForwardValues<>(dataStream, start, limit);
    }

    public static <T> IndexedValues<T> fullStream(final ArrayDataStream<T> dataStream) {
        return new FullValues<T>(dataStream);
    }

    public static <T> IndexedValues<T> fullStreamDeletes(final ArrayDataStream<T> dataStream) {
        return new FullValuesWithDeletes<T>(dataStream);
    }

    public static <T> IndexedValues<T> indexStream(final ArrayDataStream<T> dataStream, final IndexStream indexStream) {
        return new IndexStreamValues<T>(dataStream, indexStream);
    }

    public static <T> IndexedValues<T> indexStream(final ArrayDataStream<T> dataStream, final long from, final long to) {
        return new IndexStreamValues<T>(dataStream, IndexStream.of(from, to));
    }

    public static <T> IndexedValues<T> indexStream(final ArrayDataStream<T> dataStream, final List<Long> indexes) {
        return new IndexStreamValues<T>(dataStream, IndexStream.of(indexes));
    }

    public static <T> IndexedValues<T> reverseFullStream(final ArrayDataStream<T> dataStream) {
        return new FullReversedValues<>(dataStream);
    }

    public static <T> IndexedValues<T> skipLimitStream(final ArrayDataStream<T> dataStream, final int skip, final int limit) {
        return new SkipLimitValues<>(dataStream, skip, limit);
    }

    @Override
    public int getSize() {
        return this.size;
    }

    @Override
    public long[] toIndexArray() {
        Iterator<IndexedValue<T>> iterator = this.iterator();

        long[] indexes = new long[this.size];
        int seq = 0;

        while (iterator.hasNext()) {
            IndexedValue<T> next = iterator.next();

            indexes[seq++] = next.getIndex();
        }

        return indexes;

    }

    @Override
    public T[] toValueArray(final Class<T> tClass) {
        Iterator<IndexedValue<T>> iterator = this.iterator();

        @SuppressWarnings("unchecked") final T[] valueArray = (T[]) Array.newInstance(tClass, this.size);
        int seq = 0;

        while (iterator.hasNext()) {
            IndexedValue<T> next = iterator.next();

            valueArray[seq++] = next.getValue();
        }

        return valueArray;
    }

    protected final int countByIterator() {
        int count = 0;

        Iterator<IndexedValue<T>> iterator = this.iterator();
        while (iterator.hasNext()) {
            count++;
        }

        return count;
    }

    private static class BackwardValues<T> extends StreamIndexedValues<T> {
        private final long start;
        private final int limit;

        private BackwardValues(final ArrayDataStream<T> dataStream, final long start, final int limit) {
            super(dataStream);

            this.start = start;
            this.limit = limit;
            this.size = countByIterator();
        }

        @Override
        public Iterator<IndexedValue<T>> iterator() {
            return this.dataStream.iteratorBackward(start, limit);
        }
    }

    private static class ForwardValues<T> extends StreamIndexedValues<T> {
        private final long start;
        private final int limit;

        private ForwardValues(final ArrayDataStream<T> dataStream, final long start, final int limit) {
            super(dataStream);

            this.start = start;
            this.limit = limit;
            this.size = countByIterator();
        }

        @Override
        public Iterator<IndexedValue<T>> iterator() {
            return this.dataStream.iteratorForward(start, limit);
        }
    }

    private static class SkipLimitValues<T> extends StreamIndexedValues<T> {
        private final int skip;
        private final int limit;

        private SkipLimitValues(final ArrayDataStream<T> dataStream, final int skip, final int limit) {
            super(dataStream);

            this.skip = skip;
            this.limit = limit;
            this.size = countByIterator();
        }

        @Override
        public Iterator<IndexedValue<T>> iterator() {
            return this.dataStream.iterator(skip, limit);
        }
    }

    private static class IndexStreamValues<T> extends StreamIndexedValues<T> {
        private final IndexStream indexStream;

        private IndexStreamValues(final ArrayDataStream<T> dataStream, final IndexStream indexStream) {
            super(dataStream);

            this.indexStream = indexStream;
            this.size = countByIterator();
        }

        @Override
        public Iterator<IndexedValue<T>> iterator() {
            return this.dataStream.iterator(this.indexStream);
        }
    }

    private static class FullValuesWithDeletes<T> extends StreamIndexedValues<T> {
        private FullValuesWithDeletes(final ArrayDataStream<T> dataStream) {
            super(dataStream);
            this.size = dataStream.bufferCount();
        }

        @Override
        public Iterator<IndexedValue<T>> iterator() {
            return this.dataStream.iteratorWithDeletes();
        }
    }

    private static class FullValues<T> extends StreamIndexedValues<T> {

        private FullValues(final ArrayDataStream<T> dataStream) {
            super(dataStream);
            this.size = dataStream.count();
        }

        @Override
        public Iterator<IndexedValue<T>> iterator() {
            return this.dataStream.iterator();
        }
    }

    private static class FullReversedValues<T> extends StreamIndexedValues<T> {

        private FullReversedValues(final ArrayDataStream<T> dataStream) {
            super(dataStream);
            this.size = dataStream.count();
        }

        @Override
        public Iterator<IndexedValue<T>> iterator() {
            return this.dataStream.reverseIterator();
        }
    }

}

