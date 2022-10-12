package com.fuyouj.sword.cache;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import com.fuyouj.sword.concurrent.runner.AtomicRunner;
import com.fuyouj.sword.concurrent.runner.AtomicRunnerFactory;
import com.github.benmanes.caffeine.cache.Cache;

public class CaffeineSwordCache<K, V> implements SwordCache<K, V> {
    private static final AtomicRunner ATOMIC_RUNNER = AtomicRunnerFactory.noneThreadPoolSingleCommandRunner();
    private final Cache<K, Object> caffeineCache;

    public CaffeineSwordCache(final Cache<K, Object> caffeineCache) {
        this.caffeineCache = caffeineCache;
    }

    @Override
    public void cleanUp() {
        this.caffeineCache.cleanUp();
    }

    @Override
    public long estimatedSize() {
        return this.caffeineCache.estimatedSize();
    }

    @Override
    public V get(final K key, final Function<? super K, ? extends V> mappingFunction) {
        final Object result = ATOMIC_RUNNER.syncRun(getAtomicKey(key), () -> {
            Object cachedData = caffeineCache.getIfPresent(key);

            if (cachedData == null) {
                return caffeineCache.get(key, mappingFunction);
            }

            if (cachedData instanceof TimedCacheData) {
                if (((TimedCacheData<V>) cachedData).hasExpired()) {
                    V mappedData = mappingFunction.apply(key);
                    caffeineCache.put(key, mappedData);
                    return mappedData;
                }
            }

            return cachedData;
        });

        if (result == null) {
            return null;
        }

        return (V) result;
    }

    @Override
    public Map<K, V> getAllPresent(final Iterable<? extends K> keys) {
        Map<K, V> result = new HashMap<>();

        for (K key : keys) {
            Object atomicResult = ATOMIC_RUNNER.syncRun(getAtomicKey(key), () -> {
                Object cachedData = caffeineCache.getIfPresent(key);

                if (cachedData == null) {
                    return null;
                }

                if (cachedData instanceof TimedCacheData) {
                    return ((TimedCacheData<?>) cachedData).getIfNotExpired();
                }

                return cachedData;
            });

            if (atomicResult != null) {
                result.put(key, (V) atomicResult);
            }
        }

        return result;
    }

    @Override
    public V getIfPresent(final K key) {
        if (key == null) {
            return null;
        }

        Object cachedData = caffeineCache.getIfPresent(key);

        if (cachedData == null) {
            return null;
        }

        if (cachedData instanceof TimedCacheData) {
            return (V) ((TimedCacheData<?>) cachedData).getIfNotExpired();
        }

        return (V) cachedData;
    }

    @Override
    public void invalidate(final K key) {
        caffeineCache.invalidate(key);
    }

    @Override
    public void invalidateAll(final Iterable<? extends K> keys) {
        caffeineCache.invalidateAll(keys);
    }

    @Override
    public void invalidateAll() {
        caffeineCache.invalidateAll();
    }

    @Override
    public Set<K> keys() {
        return caffeineCache.asMap().keySet();
    }

    @Override
    public void put(final K key, final V value) {
        if (value == null || key == null) {
            return;
        }
        caffeineCache.put(key, value);
    }

    @Override
    public void put(final K key, final V value, final long timeout, final TimeUnit unit) {
        caffeineCache.put(key, new TimedCacheData<V>(value, unit.toMillis(timeout)));
    }

    @Override
    public void putAll(final Map<? extends K, ? extends V> map) {
        caffeineCache.putAll(map);
    }

    @Override
    public boolean putIfAbsent(final K key, final V value, final long timeout, final TimeUnit unit) {
        return (boolean) ATOMIC_RUNNER.syncRun(getAtomicKey(key), () -> {
            Object cachedData = caffeineCache.getIfPresent(key);

            if (cachedData == null
                    || (cachedData instanceof TimedCacheData && ((TimedCacheData<?>) cachedData).hasExpired())) {
                caffeineCache.put(key, new TimedCacheData<V>(value, unit.toMillis(timeout)));
                return true;
            }

            return false;
        });
    }

    @Override
    public SwordCacheStats stats() {
        return SwordCacheStats.of(caffeineCache.stats());
    }

    private K getAtomicKey(final K key) {
        if (key instanceof String) {
            return (K) ("Cache:" + key);
        }

        return key;
    }
}
