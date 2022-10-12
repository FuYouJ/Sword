package com.fuyouj.sword.cache;

final class TimedCacheData<T> {
    private final long unixTimestampAdded;
    private final long expiryMillis;
    private final T cacheData;

    TimedCacheData(final T cacheData, final long expiryMillis) {
        this.unixTimestampAdded = System.currentTimeMillis();
        this.expiryMillis = expiryMillis;
        this.cacheData = cacheData;
    }
    T getIfNotExpired() {
        if (hasExpired()) {
            return null;
        }

        return cacheData;
    }

    boolean hasExpired() {
        if (expiryMillis <= 0) {
            return true;
        }

        return System.currentTimeMillis() - unixTimestampAdded >= expiryMillis;
    }
}
