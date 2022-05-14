package com.fuyouj.sword.cache;

import java.util.List;

import com.fuyouj.sword.scabard.Lists2;

public class KeyFieldCache<K,V>{
    private final SwordCache<String, V> jarvisCache;
    private final String key;

    public KeyFieldCache(final String key, final SwordCacheProvider cacheProvider) {
        this.key = key;
        this.jarvisCache = cacheProvider.get();
    }

    public void deleteKeys(final List<K> keys) {
        this.jarvisCache.invalidateAll(Lists2.map(keys, this::buildKey));
    }

    public V get(final K name) {
        return this.jarvisCache.getIfPresent(buildKey(name));
    }

    public void put(final K name, final V value) {
        jarvisCache.put(buildKey(name), value);
    }

    public void remove(final K name) {
        jarvisCache.invalidate(buildKey(name));
    }

    private String buildKey(final K name) {
        return key + name.toString();
    }
}
