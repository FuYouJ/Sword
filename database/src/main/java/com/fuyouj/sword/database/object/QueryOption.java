package com.fuyouj.sword.database.object;

import com.thingworks.jarvis.persistent.exception.Exceptions;

import net.thingworks.jarvis.utils.type.Asserts2;

public class QueryOption<T> {
    private Matcher<T> matcher;
    private Sorter<T> sorter;
    private long limit = -1;

    private QueryOption(final Matcher<T> matcher, final Sorter<T> sorter, final long limit) {
        this.matcher = matcher;
        this.sorter = sorter;
        this.limit = limit;
    }

    public static <T> QueryOptionBuilder<T> builder() {
        return new QueryOptionBuilder<>();
    }

    public static <T> QueryOption<T> of(final Matcher<T> matcher) {
        return new QueryOptionBuilder<T>().match(matcher).build();
    }

    public static <T> QueryOptionBuilder<T> where(final Matcher<T> matcher) {
        return new QueryOptionBuilder<T>().match(matcher);
    }

    public long getLimit() {
        return this.limit;
    }

    public boolean hasLimit() {
        return this.limit > 0;
    }

    public boolean hasSorter() {
        return this.sorter != null;
    }

    public boolean match(final T item) {
        return matcher.match(item);
    }

    public int sort(final T item1, final T item2) {
        if (this.sorter == null) {
            return 1;
        }

        return this.sorter.compare(item1, item2);
    }

    public static class QueryOptionBuilder<T> {
        private Matcher<T> matcher;
        private Sorter<T> sorter;
        private long limit = -1;

        public QueryOption<T> build() {
            Asserts2.hasArg(matcher, "QueryOption matcher must NOT be null");
            return new QueryOption<>(matcher, sorter, limit);
        }

        public QueryOptionBuilder<T> limit(final long limit) {
            if (limit < 0) {
                throw Exceptions.invalidQueryOption("limit must greater than 0");
            }

            this.limit = limit;
            return this;
        }

        public QueryOptionBuilder<T> match(final Matcher<T> matcher) {
            this.matcher = matcher;
            return this;
        }

        public QueryOptionBuilder<T> sorter(final Sorter<T> sorter) {
            this.sorter = sorter;
            return this;
        }

    }
}
