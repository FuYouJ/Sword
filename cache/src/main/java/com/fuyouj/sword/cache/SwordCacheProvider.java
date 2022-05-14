package com.fuyouj.sword.cache;

public class SwordCacheProvider {
    private final SwordCache<?, ?> unboundedCache;

    public <V, K> SwordCacheProvider(final SwordCache<K, V> unboundedCache) {
        this.unboundedCache = unboundedCache;
    }

    @SuppressWarnings("unchecked")
    public <K, V> SwordCache<K, V> get() {
        return (SwordCache<K, V>) this.unboundedCache;
    }
}
