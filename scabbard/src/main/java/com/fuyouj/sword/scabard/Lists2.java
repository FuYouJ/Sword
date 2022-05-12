package com.fuyouj.sword.scabard;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class Lists2 {
    public static <T> List<T> fixed(final int size) {
        return new ArrayList<>(size);
    }

    public static <T> boolean isNullOrEmpty(final Collection<T> list) {
        return list == null || list.isEmpty();
    }

    public static <T, R> List<R> map(final Collection<T> list, final Function<T, R> mapper) {
        return doIndexMap(list, (item, index) -> mapper.apply(item), false, true);
    }

    //    将Collection构建成一个Map，如果已经存在相同的key则覆盖前者
    public static <T, K, V> Map<K, V> map(final Collection<T> list,
                                          final Function<T, K> keyMapper,
                                          final Function<T, V> valueMapper) {
        if (Lists2.isNullOrEmpty(list)) {
            return Maps2.staticEmpty();
        }

        HashMap<K, V> hashMap = new HashMap<>(list.size());

        list.forEach(item -> {
            K key = keyMapper.apply(item);
            if (key != null) {
                hashMap.put(key, valueMapper.apply(item));
            }
        });

        return hashMap;
    }

    public static <T> List<T> staticEmpty() {
        return Collections.emptyList();
    }

    private static <T, R> List<R> doIndexMap(final Collection<T> list,
                                             final StreamFunction<T, R> mapper,
                                             final boolean skipNull,
                                             final boolean exportNull) {
        if (Lists2.isNullOrEmpty(list)) {
            return Lists2.newList();
        }

        List<R> result = fixed(list.size());

        int index = 0;
        for (T item : list) {
            if (skipNull && item == null) {
                index++;
                continue;
            }

            R mappedItem = mapper.apply(item, index);
            if (!exportNull && mappedItem == null) {
                index++;
                continue;
            }

            result.add(mappedItem);
            index++;
        }

        return result;
    }

    public static <T> List<T> newList() {
        return new ArrayList<>();
    }
}
