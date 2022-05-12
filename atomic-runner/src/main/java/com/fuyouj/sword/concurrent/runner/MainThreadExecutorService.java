package com.fuyouj.sword.concurrent.runner;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import com.fuyouj.sword.scabard.Lists2;

public class MainThreadExecutorService implements ExecutorService {
    public static final MainThreadExecutorService INSTANCE = new MainThreadExecutorService();

    private MainThreadExecutorService() {

    }

    @Override
    public boolean awaitTermination(final long timeout, final TimeUnit unit) throws InterruptedException {
        return false;
    }

    @Override
    public void execute(final Runnable command) {
        command.run();
    }

    @Override
    public <T> List<Future<T>> invokeAll(final Collection<? extends Callable<T>> tasks) throws InterruptedException {
        if (Lists2.isNullOrEmpty(tasks)) {
            return Lists2.staticEmpty();
        }

        return Lists2.map(tasks, task -> {
            try {
                return new InstantFuture<>(task.call());
            } catch (Exception e) {
                return InstantFuture.cancelled(e);
            }
        });
    }

    @Override
    public <T> List<Future<T>> invokeAll(final Collection<? extends Callable<T>> tasks, final long timeout,
                                         final TimeUnit unit) throws InterruptedException {
        return this.invokeAll(tasks);
    }

    @Override
    public <T> T invokeAny(final Collection<? extends Callable<T>> tasks) throws InterruptedException,
            ExecutionException {
        if (Lists2.isNullOrEmpty(tasks)) {
            return null;
        }

        for (Callable<T> task : tasks) {
            try {
                return task.call();
            } catch (Exception ignored) {
            }
        }

        return null;
    }

    @Override
    public <T> T invokeAny(final Collection<? extends Callable<T>> tasks,
                           final long timeout,
                           final TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        return invokeAny(tasks);
    }

    @Override
    public boolean isShutdown() {
        return false;
    }

    @Override
    public boolean isTerminated() {
        return false;
    }

    @Override
    public void shutdown() {

    }

    @Override
    public List<Runnable> shutdownNow() {
        return Lists2.staticEmpty();
    }

    @Override
    public <T> Future<T> submit(final Callable<T> task) {
        if (task == null) {
            return InstantFuture.nullTask();
        }

        try {
            return new InstantFuture<>(task.call());
        } catch (Exception e) {
            return InstantFuture.cancelled(e);
        }
    }

    @Override
    public <T> Future<T> submit(final Runnable task, final T result) {
        if (task == null) {
            return InstantFuture.nullTask();
        }

        task.run();

        return new InstantFuture<>(result);
    }

    @Override
    public Future<?> submit(final Runnable task) {
        if (task == null) {
            return InstantFuture.nullTask();
        }

        task.run();

        return InstantFuture.completeEmpty();
    }
}
