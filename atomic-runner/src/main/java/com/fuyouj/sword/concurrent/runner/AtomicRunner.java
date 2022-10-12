package com.fuyouj.sword.concurrent.runner;

import com.fuyouj.sword.concurrent.Commander;

public interface AtomicRunner<K> {
    <T> AtomicResult<T> run(K atomicKey, Commander<T> commander);

    <T> T syncRun(K atomicKey, Commander<T> commander);
}
