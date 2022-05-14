package com.fuyouj.sword.cache;

import com.github.benmanes.caffeine.cache.stats.CacheStats;

public class SwordCacheStats {
    private final CacheStats stats;

    private SwordCacheStats(final CacheStats stats) {
        this.stats = stats;
    }

    public static SwordCacheStats of(final CacheStats stats) {
        return new SwordCacheStats(stats);
    }

    public long hitCount() {
        return stats.hitCount();
    }

    public double hitRate() {
        long requestCount = requestCount();

        if (requestCount == 0) {
            return 1.0;
        }

        return (double) stats.hitCount() / requestCount;
    }

    public long missCount() {
        return stats.missCount();
    }

    public double missRate() {
        long requestCount = requestCount();
        if (requestCount == 0) {
            return 0.0;
        }
        return (double) stats.missCount() / requestCount;
    }

    public long requestCount() {
        return saturatedAdd(stats.hitCount(), stats.missCount());
    }

    private static long saturatedAdd(final long a, final long b) {
        long naiveSum = a + b;
        if (((a ^ b) < 0) | ((a ^ naiveSum) >= 0)) {
            return naiveSum;
        }
        return Long.MAX_VALUE + ((naiveSum >>> (Long.SIZE - 1)) ^ 1);
    }
}
