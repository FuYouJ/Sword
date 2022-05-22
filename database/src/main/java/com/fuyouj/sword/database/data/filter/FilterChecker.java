package com.fuyouj.sword.database.data.filter;

import java.util.List;

import com.fuyouj.sword.scabard.Lists2;
import com.fuyouj.sword.scabard.Objects2;

/**
 * filter条件检查
 */
public class FilterChecker {

    public static Filter checkFilter(final Filter base) {
        if (null == base) {
            return null;
        }

        if (base instanceof StaticFilter) {
            return base;
        }

        if (base instanceof AndFilter) {
            List<Filter> filters = checkFilters((CompositeFilter) base);
            return AndFilter.of(filters);
        }

        if (base instanceof OrFilter) {
            List<Filter> filters = checkFilters((CompositeFilter) base);
            return OrFilter.of(filters);
        }

        if (base instanceof SimpleFilter) {
            return emptyCheck((SimpleFilter) base);
        }

        return base;
    }

    private static List<Filter> checkFilters(final CompositeFilter base) {
        List<Filter> filters = Lists2.fixed(base.getFilters().size());
        base.getFilters().forEach(filter -> {
            filters.add(FilterChecker.checkFilter(filter));
        });
        return filters;
    }

    private static Filter emptyCheck(final SimpleFilter filter) {
        if (Objects2.isNullOrEmpty(filter.getValue())) {
            switch (filter.getOp()) {
                case BETWEEN_AND:
                case LT:
                    return Filter.FALSE;
                case GTE:
                    return Filter.TRUE;
                case LTE:
                case MATCH:
                    return SimpleFilter.of(filter.getReferenceKey(), ConditionOp.EQ, filter.getValue());
                case GT:
                case UNMATCH:
                    return SimpleFilter.of(filter.getReferenceKey(), ConditionOp.NE, filter.getValue());
                case EQ:
                case NE:
                default:
                    return filter;
            }
        }
        return filter;
    }
}
