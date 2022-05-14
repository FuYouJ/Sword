package com.fuyouj.sword.cache;

import java.util.ArrayList;
import java.util.List;

import com.fuyouj.sword.scabard.Lists2;

public class KeyFieldListCache<K,V> {
    private final SwordCache<K, List<V>> swordCache;
    private final String key;
    public KeyFieldListCache(final String key, final SwordCacheProvider cacheProvider) {
        this.key = key;
        this.swordCache = cacheProvider.get();
    }

    public void append(final K name, final V value) {
        List<V> cachedList = this.swordCache.get(name, k -> Lists2.items(value));

        if (cachedList.size() == 1 && cachedList.get(0) == value) {
            return;
        }

        cachedList.add(value);
    }

    public List<V> get(final K name) {
        return this.swordCache.get(name, k -> new ArrayList<>());
    }

    public void pull(final K name, final List<V> values) {
        if (Lists2.isNullOrEmpty(values)) {
            return;
        }

        List<V> cachedList = this.swordCache.getIfPresent(name);

        if (Lists2.isNullOrEmpty(cachedList)) {
            return;
        }

        cachedList.removeAll(values);
    }

    public void pull(final K name, final V value) {
        pull(name, Lists2.items(value));
    }

    public void remove(final K name) {
        this.swordCache.invalidate(name);
    }
    
}
