package com.fuyouj.sword.concurrent;

public class StateAtomicCommand<K, T> implements AtomicCommand<K, T> {
    private final K atomicKey;
    private final Commander<T> commander;
    private CommandState state;
    private T result;

    public StateAtomicCommand(final K atomicKey, final Commander<T> commander) {
        this.atomicKey = atomicKey;
        this.commander = commander;
        this.state = CommandState.NotStarted;
    }

    @Override
    public K atomicKey() {
        return this.atomicKey;
    }

    @Override
    public T run() {
        T result = this.commander.run();

        this.finished(result);

        return result;
    }

    private void finished(final T result) {
        if (this.state != CommandState.Finished) {
            this.result = result;
            this.state = CommandState.Finished;
        }
    }
}
