package com.fuyouj.sword.concurrent.runner;

import com.fuyouj.sword.concurrent.SingleCommandAtomicQueueMapper;

public class AtomicRunnerFactory {
    public static <K> AtomicRunner<K> noneThreadPoolSingleCommandRunner() {
        return new CurrentThreadAtomicRunner<K>(new SingleCommandAtomicQueueMapper<K>());
    }
}
