package com.fuyouj.sword.database.data.filter;

import com.fuyouj.sword.scabard.Lists2;

public class OrFilterWithOrCompactor extends FilterCompactor<OrFilter> {
    OrFilterWithOrCompactor(final OrFilter left) {
        super(left);
    }

    @Override
    Filter doCompact(final Filter right) {
        if (right instanceof OrFilter) {
            return new OrFilter(Lists2.concat(left.getFilters(), ((OrFilter) right).getFilters()));
        }

        if (right instanceof AndFilter) {
            return new OrFilter(Lists2.appendTail(right, left.getFilters()));
        }

        return new OrFilter(Lists2.appendTail(right, left.getFilters()));
    }
}

