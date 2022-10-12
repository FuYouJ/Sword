package com.fuyouj.sword.database.data.filter;

import com.fuyouj.sword.scabard.Lists2;

public class AndFilterWithAndCompactor extends FilterCompactor<AndFilter> {

    AndFilterWithAndCompactor(final AndFilter left) {
        super(left);
    }

    @Override
    Filter doCompact(final Filter right) {
        if (right instanceof OrFilter) {
            return new OrFilter(Lists2.map(((OrFilter) right).getFilters(),
                    filter -> new AndFilter(Lists2.appendTail(filter, left.getFilters()))));
        }

        if (right instanceof AndFilter) {
            return new AndFilter(Lists2.concat(left.getFilters(), ((AndFilter) right).getFilters()));
        }

        return new AndFilter(Lists2.appendTail(right, left.getFilters()));
    }
}
