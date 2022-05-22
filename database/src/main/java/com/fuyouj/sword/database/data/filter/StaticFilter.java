package com.fuyouj.sword.database.data.filter;

import java.util.Map;

public interface StaticFilter extends ValueFilter {

    @Override
    default Filter and(final Filter filter) {
        if (this.isTrue()) {
            return filter;
        }

        return this;
    }

    @Override
    default String getReferenceKey() {
        return null;
    }

    boolean isTrue();

    @Override
    default boolean match(final Map<String, Object> entry) {
        return isTrue();
    }

    @Override
    default Filter or(Filter filter) {
        if (this.isTrue()) {
            return this;
        }

        return filter;
    }
}
