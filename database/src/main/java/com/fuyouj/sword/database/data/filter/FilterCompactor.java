package com.fuyouj.sword.database.data.filter;

import java.util.List;

import com.fuyouj.sword.scabard.Lists2;

public abstract class FilterCompactor<T extends Filter> {

    protected final T left;

    public FilterCompactor(final T left) {
        this.left = left;
    }

    public static Filter compact(final Filter filter) {
        if (filter == null) {
            return null;
        }

        Filter equalityFilter = filter.asEqualityFilter();
        if (equalityFilter == null) {
            return null;
        }

        if (equalityFilter instanceof CompositeFilter) {
            CompositeFilter compositeFilter = (CompositeFilter) equalityFilter;
            return compositeCompact(compositeFilter.getFilters(), compositeFilter.op());
        } else {
            return equalityFilter;
        }
    }

    static Filter compact(final Filter left, final Filter right, final FilterOp operator) {
        return getCompactor(left, operator).doCompact(right);
    }

    abstract Filter doCompact(Filter right);

    private static Filter compositeCompact(final List<Filter> filters, final FilterOp operator) {
        List<Filter> filterList = Lists2.mapNotNull(filters, FilterCompactor::compact);
        if (Lists2.isNullOrEmpty(filterList)) {
            return null;
        }

        Filter baseFilter = filterList.get(0);

        return filterList.stream().skip(1)
                .reduce(baseFilter, (f1, f2) -> compact(f1, f2, operator));
    }

    private static FilterCompactor<?> getAndFilter(final Filter left) {
        if (left instanceof OrFilter) {
            return new OrFilterWithAndCompactor((OrFilter) left);
        }

        if (left instanceof AndFilter) {
            return new AndFilterWithAndCompactor((AndFilter) left);
        }

        return new ValueFilterWithAndCompactor((ValueFilter) left);
    }

    private static FilterCompactor<?> getCompactor(final Filter left, final FilterOp operator) {
        if (FilterOp.OR.equals(operator)) {
            return getOrFilter(left);
        } else {
            return getAndFilter(left);
        }
    }

    private static FilterCompactor<?> getOrFilter(final Filter left) {
        if (left instanceof OrFilter) {
            return new OrFilterWithOrCompactor((OrFilter) left);
        }

        if (left instanceof AndFilter) {
            return new AndFilterWithOrCompactor((AndFilter) left);
        }

        return new ValueFilterWithOrCompactor((ValueFilter) left);
    }
}


