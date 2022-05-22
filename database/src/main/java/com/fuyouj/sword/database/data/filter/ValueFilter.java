package com.fuyouj.sword.database.data.filter;

import com.fuyouj.sword.scabard.Lists2;

public interface ValueFilter extends Filter {
    @Override
    default Filter and(final Filter filter) {
        if (filter == null) {
            return this;
        }

        if (filter instanceof StaticFilter) {
            filter.and(this);
        }

        return new AndFilter(Lists2.items(this, filter));
    }

    String getReferenceKey();

    @Override
    default Filter or(final Filter filter) {
        if (filter == null) {
            return this;
        }

        if (filter instanceof StaticFilter) {
            return filter.or(this);
        }

        return new OrFilter(Lists2.items(this, filter));
    }
}
