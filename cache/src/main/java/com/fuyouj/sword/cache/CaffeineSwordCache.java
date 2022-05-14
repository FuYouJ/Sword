package com.fuyouj.sword.cache;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import com.fuyouj.sword.concurrent.runner.AtomicResult;
import com.fuyouj.sword.concurrent.runner.AtomicRunner;
import com.fuyouj.sword.concurrent.runner.AtomicRunnerFactory;
import com.github.benmanes.caffeine.cache.Cache;

public class CaffeineSwordCache<K,V> implements SwordCache<K,V> {
    private static final AtomicRunner ATOMIC_RUNNER = AtomicRunnerFactory.noneThreadPoolSingleCommandRunner();
    private final Cache<K,Object> caffeineCache;

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
        AtomicResult<V> atomicResult = ATOMIC_RUNNER.run(key,()->{

            Object cacheData = caffeineCache.getIfPresent(key);

            if(cacheData == null){
                //like java hashmap computeIfAbsent
                //if it doesn't exist,then put and return  it
                return (V)caffeineCache.get(key,mappingFunction);
            }

            if (cacheData instanceof TimedCacheData) {
                if (((TimedCacheData) cacheData).hasExpired()) {
                    //if it is expired,then put and return  it
                    //but the value is still there
                    //(V)caffeineCache.get(key,mappingFunction)
                    V mappedData = mappingFunction.apply(key);
                    caffeineCache.put(key, mappedData);
                    return mappedData;
                }
            }
            return (V)cacheData;
        });
        if (atomicResult.isSuccess()) {
            return atomicResult.getResult();
        }
        return null;
    }

    @Override
    public Map<K, V> getAllPresent(final Iterable<? extends K> keys) {
        Map<K,V> res = new HashMap<>();
        AtomicResult<Object> atomicResult;
        for (K key : keys) {
            atomicResult = ATOMIC_RUNNER.run(key,()->{
                Object cacheData = caffeineCache.getIfPresent(key);
                if(cacheData == null){
                    return null;
                }
                if (cacheData instanceof TimedCacheData) {
                    return ((TimedCacheData<?>) cacheData).getIfNotExpired();
                }
                return cacheData;
            });

            if (atomicResult.isSuccess() && atomicResult.hasEmptyResult() == false) {
                res.put(key, (V) atomicResult.getResult());
            }
        }
        return res;
    }

    @Override
    public V getIfPresent(final K key) {
        if (key == null) {
            return null;
        }
        Object cacheData = caffeineCache.getIfPresent(key);
        if (cacheData == null) {
            return null;
        }
        if (cacheData instanceof TimedCacheData) {
            return (V) ((TimedCacheData<?>) cacheData).getIfNotExpired();
        }
        return (V) cacheData;
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
    public void put(final K key, final V value) {
        if (key == null || value == null) {
            return;
        }
        caffeineCache.put(key, value);
    }

    @Override
    public void put(final K key, final V value, final long timeout, final TimeUnit unit) {
        caffeineCache.put(key, new TimedCacheData<V>(value, (int) unit.toMillis(timeout)));
    }

    @Override
    public void putAll(final Map<? extends K, ? extends V> map) {
        caffeineCache.putAll(map);
    }

    @Override
    public boolean putIfAbsent(final K key, final V value, final long timeout, final TimeUnit unit) {
        AtomicResult<Boolean> atomicResult = ATOMIC_RUNNER.run(key, () -> {
            Object cachedData = caffeineCache.getIfPresent(key);

            if (cachedData == null
                    || (cachedData instanceof TimedCacheData && ((TimedCacheData<?>) cachedData).hasExpired())) {
                caffeineCache.put(key, new TimedCacheData<V>(value, (int) unit.toMillis(timeout)));
                return true;
            }

            return false;
        });

        if (atomicResult.isSuccess() && !atomicResult.hasEmptyResult()) {
            return atomicResult.getResult();
        }

        return false;
    }

    @Override
    public SwordCacheStats stats() {
        return SwordCacheStats.of(caffeineCache.stats());
    }
}
