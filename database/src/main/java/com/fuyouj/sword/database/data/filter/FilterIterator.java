package com.fuyouj.sword.database.data.filter;

import java.util.function.BiConsumer;

/**
 * @author hangwen
 * @date 2020/10/14
 */
public class FilterIterator {

    public static void iterate(final Filter filter, final BiConsumer<Filter, CompositeFilter> consumer) {
        if (filter == null || consumer == null) {
            return;
        }

        doIterate(filter, null, consumer);

    }

    private static void doIterate(final Filter filter,
                                  final CompositeFilter parent,
                                  final BiConsumer<Filter, CompositeFilter> consumer) {
        consumer.accept(filter, parent);

        if (filter instanceof CompositeFilter) {
            ((CompositeFilter) filter).getFilters().forEach(childFilter ->
                    doIterate(childFilter, (CompositeFilter) filter, consumer));
        }
    }
}
