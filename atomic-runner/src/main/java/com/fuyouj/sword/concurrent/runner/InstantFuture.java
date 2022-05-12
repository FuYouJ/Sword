package com.fuyouj.sword.concurrent.runner;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import com.fuyouj.sword.concurrent.runner.exception.NullTaskException;
import com.fuyouj.sword.concurrent.runner.exception.RunnerException;

public class InstantFuture<T> implements Future<T> {
    private static final Future<?> CANCELLED = new InstantFuture<>(null, true);
    private static final Future<?> NULL_TASK = new InstantFuture<>(null, true, new NullTaskException());
    private static final Future<?> EMPTY = new InstantFuture<>(null, false);
    private final T result;
    private final boolean cancelled;
    private final Exception exception;

    public InstantFuture(final T result) {
        this(result, false, null);
    }

    private InstantFuture(final T result, final boolean cancelled) {
        this(result, cancelled, null);
    }

    private InstantFuture(final T result, final boolean cancelled, final Exception exception) {
        this.result = result;
        this.cancelled = cancelled;
        this.exception = exception;
    }

    @SuppressWarnings("unchecked")
    public static <T> Future<T> cancelled(final Exception e) {
        return new InstantFuture<>(null, true, e);
    }

    public static Future<?> completeEmpty() {
        return EMPTY;
    }

    @SuppressWarnings("unchecked")
    public static <T> Future<T> nullTask() {
        return (Future<T>) NULL_TASK;
    }

    @Override
    public boolean cancel(final boolean mayInterruptIfRunning) {
        return false;
    }

    @Override
    public T get() throws InterruptedException, ExecutionException {
        return this.result;
    }

    @Override
    public T get(final long timeout, final TimeUnit unit)
            throws InterruptedException, ExecutionException, TimeoutException {
        if (this.exception != null) {
            throw new RunnerException(this.exception);
        }
        return this.result;
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public boolean isDone() {
        return !this.cancelled;
    }
}

