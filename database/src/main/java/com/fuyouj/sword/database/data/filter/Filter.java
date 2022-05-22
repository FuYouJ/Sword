package com.fuyouj.sword.database.data.filter;

import java.util.Iterator;
import java.util.Map;

import com.fuyouj.sword.scabard.Lists2;
import com.fuyouj.sword.scabard.Maps2;

public interface Filter {
    Filter TRUE = Filter.staticFilter(true);
    Filter FALSE = Filter.staticFilter(false);

    static Filter staticFilter(Boolean aBoolean) {
        if (aBoolean) {
            return new TrueFilter();
        }

        return new FalseFilter();
    }

    Filter and(Filter filter);

    default Filter asEqualityFilter() {
        return this;
    }

    default String asFormula() {
        return null;
    }

    default Map<String, Object> asPlainObj() {
        return Maps2.empty();
    }

    default Iterator<Filter> iterator() {
        return Lists2.items(this).iterator();
    }

    boolean match(Map<String, Object> entry);

    Filter or(Filter filter);
}

