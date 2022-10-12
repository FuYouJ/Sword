package com.fuyouj.sword.cache;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

public interface SwordCache<K, V> {
    void cleanUp();

    long estimatedSize();

    V get(K key, Function<? super K, ? extends V> mappingFunction);

    Map<K, V> getAllPresent(Iterable<? extends K> keys);

    V getIfPresent(K key);

    void invalidate(K key);

    void invalidateAll(Iterable<? extends K> keys);

    void invalidateAll();

    Set<K> keys();

    void put(K key, V value);

    void put(K key, V value, long timeout, TimeUnit unit);

    void putAll(Map<? extends K, ? extends V> map);

    boolean putIfAbsent(K key, V value, long timeout, TimeUnit unit);

    SwordCacheStats stats();
}
