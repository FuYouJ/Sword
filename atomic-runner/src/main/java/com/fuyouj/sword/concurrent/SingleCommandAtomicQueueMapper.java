package com.fuyouj.sword.concurrent;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SingleCommandAtomicQueueMapper<K> implements AtomicQueueMapper<K> {
    private final Map<K, SingleCommandAtomicQueue<K>> mapper;

    public SingleCommandAtomicQueueMapper() {
        mapper = new ConcurrentHashMap<>();
    }

    @Override
    public AtomicQueue<K> group(final K atomicKey) {
        return mapper.compute(atomicKey, (s, simpleSequenceQueue) -> {
            if (simpleSequenceQueue != null) {
                return simpleSequenceQueue;
            }

            return new SingleCommandAtomicQueue<K>(atomicKey);
        });
    }
}
