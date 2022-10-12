package com.fuyouj.sword.database.data.array;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import com.fuyouj.sword.database.utils.Iterators;
import com.fuyouj.sword.scabard.Asserts;

public abstract class IndexStream implements Iterable<Long> {

    public static final IndexStream EMPTY = new IndexStream() {
        @Override
        public Iterator<Long> iterator() {
            return Iterators.empty();
        }
    };

    public static IndexStream empty() {
        return EMPTY;
    }

    public static IndexStream of(final Iterator<Long> itr) {
        return new IndexIterator(itr);
    }

    public static IndexStream of(final long from, final long to) {
        return of(from, to, true);
    }

    public static IndexStream of(final long from, final long to, final boolean asc) {
        Asserts.isTrue(from >= 0, "from should >= 0");
        Asserts.isTrue(from <= to, "from should <= to");

        return new IndexRange(from, to, asc);
    }

    public static IndexStream of(final List<Long> indexes) {
        return new IndexArray(indexes);
    }

    private static class IndexIterator extends IndexStream {
        private final Iterator<Long> itr;

        private IndexIterator(final Iterator<Long> itr) {
            this.itr = itr;
        }

        @Override
        public Iterator<Long> iterator() {
            return itr;
        }
    }

    private static class IndexArray extends IndexStream {
        private final long[] indexes;

        private IndexArray(final List<Long> indexes) {
            this.indexes = indexes.stream().filter(Objects::nonNull).mapToLong(Long::longValue).toArray();
        }

        @Override
        public Iterator<Long> iterator() {
            return Arrays.stream(indexes).iterator();
        }
    }

    private static class IndexRange extends IndexStream {
        private final long from;
        private final long to;
        private final boolean asc;

        private final Itr itr = new Itr();

        private IndexRange(final long from, final long to, final boolean asc) {
            this.from = from;
            this.to = to;
            this.asc = asc;
        }

        @Override
        public Iterator<Long> iterator() {
            itr.reset();
            return itr;
        }

        private class Itr implements Iterator<Long> {
            long cur = -1;

            @Override
            public boolean hasNext() {
                if (asc) {
                    return ++cur <= to;
                } else {
                    return --cur >= from;
                }
            }

            @Override
            public Long next() {
                return cur;
            }

            public void reset() {
                if (asc) {
                    cur = from - 1;
                } else {
                    cur = to + 1;
                }
            }
        }
    }
}
