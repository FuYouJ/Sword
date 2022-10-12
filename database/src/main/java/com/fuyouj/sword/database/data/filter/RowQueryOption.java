package com.fuyouj.sword.database.data.filter;

import java.util.List;

import com.fuyouj.sword.database.data.array.IndexedMap;
import com.fuyouj.sword.scabard.Lists2;

import lombok.Getter;

@Getter
public class RowQueryOption {
    private Filter filter;
    private Order order;
    private Integer limit;
    private Integer skip;

    public RowQueryOption(final Filter filter, final Order order, final Integer limit) {
        this.filter = FilterCompactor.compact(filter);
        this.order = order;
        this.limit = limit;
        this.skip = 0;
    }

    public RowQueryOption(final Filter filter, final Order order, final Integer skip, final Integer limit) {
        this.filter = FilterCompactor.compact(filter);
        this.order = order;
        this.skip = skip;
        this.limit = limit;
    }

    public List<String> getFilterKeys() {
        if (this.filter == null) {
            return Lists2.staticEmpty();
        }

        final List<String> filterKeys = Lists2.newList();

        findFilterKeys(this.filter, filterKeys);

        return filterKeys;
    }

    public List<String> getOrderKeys() {
        if (this.order == null) {
            return Lists2.staticEmpty();
        }

        final List<String> orderKeys = Lists2.newList();

        findOrderKeys(this.order, orderKeys);

        return orderKeys;
    }

    public boolean hasFilter() {
        return this.filter != null;
    }

    public boolean hasLimit() {
        return this.limit != null && this.limit >= 0;
    }

    public boolean hasOrder() {
        return this.order != null;
    }

    public boolean hasSkip() {
        return this.skip != null && this.skip > 0;
    }

    public boolean isEmpty() {
        return !hasLimit() && !hasOrder() && !hasFilter();
    }

    public boolean match(final IndexedMap<String, Object> currentRow) {
        if (currentRow == null) {
            return false;
        }

        if (this.filter == null) {
            return true;
        }

        return filter.match(currentRow);
    }

    private void findFilterKeys(final Filter rootFilter, final List<String> filterKeys) {
        if (rootFilter instanceof ValueFilter) {
            String referenceKey = ((ValueFilter) rootFilter).getReferenceKey();

            if (referenceKey != null) {
                filterKeys.add(referenceKey);
            }
        }

        if (rootFilter instanceof CompositeFilter) {
            List<Filter> filters = ((CompositeFilter) rootFilter).getFilters();

            for (Filter filter : filters) {
                findFilterKeys(filter, filterKeys);
            }
        }
    }

    private void findOrderKeys(final Order rootOrder, final List<String> orderKeys) {
        for (Order nestedOrder : rootOrder.getOrders()) {
            if (nestedOrder instanceof SimpleOrder) {
                orderKeys.add(((SimpleOrder) nestedOrder).getReferenceKey());
                continue;
            }

            if (nestedOrder instanceof CompositeOrder) {
                findOrderKeys(nestedOrder, orderKeys);
            }
        }
    }
}

