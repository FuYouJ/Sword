package com.fuyouj.sword.concurrent.runner;

import java.util.concurrent.ExecutorService;

import com.fuyouj.sword.concurrent.AtomicCommand;
import com.fuyouj.sword.concurrent.AtomicQueueMapper;
import com.fuyouj.sword.concurrent.CommandFactory;
import com.fuyouj.sword.concurrent.Commander;
import com.fuyouj.sword.scabard.Asserts;

public class CurrentThreadAtomicRunner<K> implements AtomicRunner<K> {
    private final AtomicQueueMapper<K> sequenceQueueMapper;
    private final ExecutorService executorService;

    public CurrentThreadAtomicRunner(final AtomicQueueMapper<K> sequenceQueueMapper) {
        this.sequenceQueueMapper = sequenceQueueMapper;
        this.executorService = MainThreadExecutorService.INSTANCE;
    }

    public <T> AtomicResult<T> run(final AtomicCommand<K, T> cmd) {
        Asserts.hasArg(cmd.atomicKey(), "cmd must have atomic key");
        return sequenceQueueMapper.group(cmd.atomicKey()).pushAndGet(this.executorService, cmd);
    }

    @Override
    public <T> AtomicResult<T> run(final K atomicKey, final Commander<T> commander) {
        return this.run(CommandFactory.stateCommand(atomicKey, commander));
    }
}
