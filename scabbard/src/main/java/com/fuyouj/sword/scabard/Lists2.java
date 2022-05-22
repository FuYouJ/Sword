package com.fuyouj.sword.scabard;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;

public class Lists2 {
    public static <T> List<T> concat(final Collection<? extends T>... lists) {
        int totalSize = Stream.of(lists).mapToInt(coll -> {
            if (coll != null) {
                return coll.size();
            } else {
                return 0;
            }
        }).sum();

        List<T> result = Lists2.fixed(totalSize);

        for (Collection<? extends T> list : lists) {
            if (!isNullOrEmpty(list)) {
                result.addAll(list);
            }
        }

        return result;
    }

    public static <T> List<T> fixed(final int size) {
        return new ArrayList<>(size);
    }

    public static <T> void foreach(final List<T> values, final Consumer<T> consumer) {
        if (isNullOrEmpty(values)) {
            return;
        }

        values.forEach(consumer);
    }

    public static <T, K> Map<K, List<T>> group(final Collection<T> list, final Function<T, K> keyMapper) {
        return group(list, keyMapper, v -> v);
    }

    /**
     * 获取Collection的分组
     */
    public static <T, K, U> Map<K, List<U>> group(final Collection<T> list,
                                                  final Function<T, K> keyMapper,
                                                  final Function<T, U> valueMapper) {
        if (Lists2.isNullOrEmpty(list)) {
            return Maps2.staticEmpty();
        }

        Map<K, List<U>> hashMap = new HashMap<>(list.size());

        list.forEach(item -> {
            K key = keyMapper.apply(item);
            if (key == null) {
                return;
            }

            U mappedItem = valueMapper.apply(item);
            if (mappedItem == null) {
                return;
            }

            List<U> tList = hashMap.get(key);

            if (isNullOrEmpty(tList)) {
                hashMap.put(key, items(mappedItem));
            } else {
                tList.add(mappedItem);
            }
        });

        return hashMap;
    }

    public static <T> boolean isNullOrEmpty(final Collection<T> list) {
        return list == null || list.isEmpty();
    }

    public static <T> boolean isSingle(final List<T> items) {
        return !isNullOrEmpty(items) && items.size() == 1;
    }

    @SafeVarargs
    public static <T> List<T> items(final T... item) {
        if (item == null || item.length == 0) {
            return Lists2.newList();
        }

        return new ArrayList<>(Arrays.asList(item));
    }

    public static <T> Optional<T> last(final List<T> items) {
        if (isNullOrEmpty(items)) {
            return Optional.empty();
        }

        return Optional.ofNullable(items.get(items.size() - 1));
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

    public static <T, R> List<R> map(final Collection<T> list, final Function<T, R> mapper) {
        return doIndexMap(list, (item, index) -> mapper.apply(item), false, true);
    }

    public static <T, K, V> List<T> mapNotNull(final Map<K, V> map, final BiFunction<K, V, T> mapper) {
        if (map == null) {
            return newList();
        }

        return doIndexMap(
                map.entrySet(),
                (entry, index) -> mapper.apply(entry.getKey(), entry.getValue()),
                false,
                false);
    }

    /**
     * 将Collection里的元素转义后，以List返回，但是会过滤掉转义为null的元素
     */
    public static <T, R> List<R> mapNotNull(final Collection<T> list, final Function<T, R> mapper) {
        return doIndexMap(list, (item, index) -> mapper.apply(item), false, false);
    }

    public static <T> List<T> newList() {
        return new ArrayList<>();
    }

    public static <T> List<T> staticEmpty() {
        return Collections.emptyList();
    }

    public static <T> Stream<T> stream(final Collection<T> items) {
        if (isNullOrEmpty(items)) {
            return Lists2.<T>newList().stream();
        }

        return items.stream();
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
}
