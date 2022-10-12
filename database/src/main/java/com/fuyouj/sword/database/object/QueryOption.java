package com.fuyouj.sword.database.object;

import com.fuyouj.sword.database.exception.Exceptions;
import com.fuyouj.sword.scabard.Asserts;

public class QueryOption<T> {
    private static final QueryOption ALL = QueryOption.builder().match(i -> true).build();
    private final Matcher<T> matcher;
    private final Sorter<T> sorter;
    private long limit = -1;
    private long skip = -1;

    private QueryOption(final Matcher<T> matcher, final Sorter<T> sorter, final long limit, final long skip) {
        this.matcher = matcher;
        this.sorter = sorter;
        this.limit = limit;
        this.skip = skip;
    }

    @SuppressWarnings("unchecked")
    public static <T> QueryOption<T> all() {
        return (QueryOption<T>) ALL;
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

    public long getSkip() {
        return this.skip;
    }

    public boolean hasLimit() {
        return this.limit > 0;
    }

    public boolean hasSkip() {
        return this.skip > 0;
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
        private long skip = -1;

        public QueryOption<T> build() {
            Asserts.hasArg(matcher, "QueryOption matcher must NOT be null");
            return new QueryOption<>(matcher, sorter, limit, skip);
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

        public QueryOptionBuilder<T> skip(final long skip) {
            if (skip < 0) {
                throw Exceptions.invalidQueryOption("skip must greater than 0");
            }

            this.skip = skip;
            return this;
        }

        public QueryOptionBuilder<T> sorter(final Sorter<T> sorter) {
            this.sorter = sorter;
            return this;
        }

    }
}
