package com.fuyouj.sword.concurrent;

public class CommandFactory {
    public static <K, T> AtomicCommand<K, T> stateCommand(final K atomicKey, final Commander<T> commander) {
        return new StateAtomicCommand<K, T>(atomicKey, commander);
    }
}
