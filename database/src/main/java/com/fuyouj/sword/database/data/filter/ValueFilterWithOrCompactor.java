package com.fuyouj.sword.database.data.filter;

import com.fuyouj.sword.scabard.Lists2;

import static com.fuyouj.sword.scabard.Lists2.items;

public class ValueFilterWithOrCompactor extends FilterCompactor<ValueFilter> {
    ValueFilterWithOrCompactor(final ValueFilter left) {
        super(left);
    }

    @Override
    Filter doCompact(final Filter right) {
        if (right instanceof OrFilter) {
            return new OrFilter(Lists2.appendHeader(left, ((OrFilter) right).getFilters()));
        }

        if (right instanceof AndFilter) {
            return new OrFilter(items(left, right));
        }

        return new OrFilter(items(left, right));
    }
}
