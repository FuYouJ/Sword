package com.fuyouj.sword.concurrent;

public interface AtomicCommand<K, V> {
    K atomicKey();

    V run();
}
