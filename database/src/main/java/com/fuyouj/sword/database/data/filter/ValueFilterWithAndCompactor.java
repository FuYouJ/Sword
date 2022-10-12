package com.fuyouj.sword.database.data.filter;

import com.fuyouj.sword.scabard.Lists2;

public class ValueFilterWithAndCompactor extends FilterCompactor<ValueFilter> {
    ValueFilterWithAndCompactor(final ValueFilter left) {
        super(left);
    }

    @Override
    Filter doCompact(final Filter right) {
        if (right instanceof OrFilter) {
            return new OrFilter(Lists2.map(((OrFilter) right).getFilters(), filter -> new AndFilter(Lists2.items(left, filter))));
        }

        if (right instanceof AndFilter) {
            return new AndFilter(Lists2.appendHeader(left, ((AndFilter) right).getFilters()));
        }

        return new AndFilter(Lists2.items(left, right));
    }
}
