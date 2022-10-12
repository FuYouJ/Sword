package com.fuyouj.sword.database.data.filter;

import com.fuyouj.sword.scabard.Lists2;

public class OrFilterWithAndCompactor extends FilterCompactor<OrFilter> {
    OrFilterWithAndCompactor(final OrFilter left) {
        super(left);
    }

    @Override
    Filter doCompact(final Filter right) {
        if (right instanceof OrFilter) {
            return new OrFilter(Lists2.combine(
                    left.getFilters(),
                    ((OrFilter) right).getFilters(),
                    (l, r) -> FilterCompactor.compact(l, r, FilterOp.AND)
            ));
        }

        if (right instanceof AndFilter) {
            return new OrFilter(Lists2.map(left.getFilters(),
                    filter -> FilterCompactor.compact(filter, right, FilterOp.AND)
            ));
        }

        return new OrFilter(Lists2.map(left.getFilters(), filter -> FilterCompactor.compact(filter, right, FilterOp.AND)));
    }

}
