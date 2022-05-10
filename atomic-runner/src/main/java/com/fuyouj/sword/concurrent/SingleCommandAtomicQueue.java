package com.fuyouj.sword.concurrent;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fuyouj.sword.concurrent.runner.AtomicResult;

public class SingleCommandAtomicQueue<K> implements AtomicQueue<K> {
    private static final Logger LOGGER = LoggerFactory.getLogger(SingleCommandAtomicQueue.class);

    private final K atomicKey;
    private final Object lock = new Object();

    public SingleCommandAtomicQueue(final K atomicKey) {
        this.atomicKey = atomicKey;
    }

    @Override
    public K getKey() {
        return this.atomicKey;
    }

    @Override
    public <T> AtomicResult<T> pushAndGet(final ExecutorService executorService, final AtomicCommand<K, T> cmd) {
        synchronized (this.lock) {
            try {
                return AtomicResult.success(executorService.submit(cmd::run)
                        .get(AtomicRunnerConfiguration.DEFAULT.getCommandTimeoutSeconds(), TimeUnit.SECONDS));
            } catch (ExecutionException e) {
                e.printStackTrace();
                LOGGER.error("Failed to run command with atomic key [{}] because it failed", cmd.atomicKey());
                return AtomicResult.failed();
            } catch (InterruptedException e) {
                e.printStackTrace();
                LOGGER.error("Failed to run command with atomic key [{}] because it has been canceled", cmd.atomicKey());
                return AtomicResult.canceled();
            } catch (TimeoutException e) {
                e.printStackTrace();
                LOGGER.error("Failed to run command with atomic key [{}] because it's timeout", cmd.atomicKey());
                return AtomicResult.timeout();
            }
        }
    }
}
