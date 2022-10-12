package com.fuyouj.sword.concurrent.runner;

import com.fuyouj.sword.concurrent.Commander;

public interface AtomicConsumer<Key> {
    AtomicRunner<Key> getRunner();

    Key buildKey(Key atomicKey);

    default <T> T atomicRun(Key resourceKey, Commander<T> commander) {
        return getRunner().syncRun(buildKey(resourceKey), commander);
    }
}
