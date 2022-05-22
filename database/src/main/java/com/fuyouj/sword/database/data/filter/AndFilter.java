package com.fuyouj.sword.database.data.filter;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.fuyouj.sword.scabard.Lists2;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@EqualsAndHashCode
public class AndFilter implements CompositeFilter {
    @Getter
    private final List<Filter> filters;

    public AndFilter(final List<Filter> filters) {
        this.filters = filters;
    }

    public static Filter of(final List<Filter> filters) {
        if (Lists2.isNullOrEmpty(filters)) {
            return new AndFilter(filters);
        }

        return Lists2.stream(filters).reduce(Filter.staticFilter(true), Filter::and);
    }

    public static Filter of(final Filter... filters) {
        if (filters == null) {
            return new AndFilter(null);
        }
        return Arrays.stream(filters).reduce(Filter.staticFilter(true), Filter::and);
    }

    @Override
    public Filter and(final Filter filter) {
        if (filter == null) {
            return this;
        }

        if (filter instanceof StaticFilter) {
            return filter.and(this);
        }

        return new AndFilter(Lists2.concat(getFilters(), Lists2.items(filter)));
    }

    @Override
    public Filter asEqualityFilter() {
        if (Lists2.isNullOrEmpty(this.filters)) {
            return null;
        }

        List<Filter> result = Lists2.fixed(this.filters.size());

        for (Filter filter : this.filters) {
            if (StaticFilter.FALSE.equals(filter)) {
                return StaticFilter.FALSE;
            }

            if (!StaticFilter.TRUE.equals(filter)) {
                result.add(filter);
            }
        }

        if (Lists2.isSingle(result)) {
            return result.get(0);
        }

        return new AndFilter(result);
    }

    @Override
    public boolean isSimpleComposite() {
        return filters.stream().allMatch(filter -> filter instanceof SimpleFilter);
    }

    @Override
    public boolean match(final Map<String, Object> entry) {
        if (Lists2.isNullOrEmpty(filters)) {
            return true;
        }

        return filters.stream().allMatch(filter -> filter.match(entry));
    }

    @Override
    public FilterOp op() {
        return FilterOp.AND;
    }

    @Override
    public Filter or(final Filter filter) {
        if (filter == null) {
            return this;
        }

        if (filter instanceof StaticFilter) {
            return filter.or(this);
        }

        return new OrFilter(Lists2.items(this, filter));
    }
}
