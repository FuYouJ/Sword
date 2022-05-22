package com.fuyouj.sword.database.object;

import java.util.function.Function;

public class Sorters<T> {
    public static <T, R extends Comparable<? super R>> Sorter<T> ascByKey(final Function<T, R> keyExtractor) {
        return nullSafeAscSorter(keyExtractor);
    }

    public static <T, R extends Comparable<? super R>> Sorter<T> descByKey(final Function<T, R> keyExtractor) {
        return (o1, o2) -> nullSafeAscSorter(keyExtractor).compare(o1, o2) * -1;
    }

    private static <T, R extends Comparable<? super R>> Sorter<T> nullSafeAscSorter(final Function<T, R> keyExtractor) {
        return (left, right) -> {
            if (left == null && right == null) {
                return 0;
            }

            if (left == null) {
                return -1;
            }

            if (right == null) {
                return 1;
            }

            final R leftKey = keyExtractor.apply(left);
            final R rightKey = keyExtractor.apply(right);

            if (leftKey == null && rightKey == null) {
                return 0;
            }

            if (leftKey == null) {
                return -1;
            }

            if (rightKey == null) {
                return 1;
            }

            return leftKey.compareTo(rightKey);
        };
    }
}
