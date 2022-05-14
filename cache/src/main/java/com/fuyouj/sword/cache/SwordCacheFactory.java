package com.fuyouj.sword.cache;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
//if you need to use the cache, you need to import the package
public class SwordCacheFactory {
    //for example,
    // SwordCache<String,Object> cache = SwordCacheFactory.unboundedCache();
    public static <K, V> SwordCache<K, V> unboundedCache() {
        Cache<K, Object> cache = Caffeine.newBuilder().build();

        return new CaffeineSwordCache<K, V>(cache);
    }
}
