package com.fuyouj.sword.concurrent.runner;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class InstantFuture<T> implements Future<T> {
    private static final Future<?> CANCELLED = new InstantFuture<>(null, true);
    private static final Future<?> EMPTY = new InstantFuture<>(null, false);
    private final T result;
    private final boolean cancelled;

    public InstantFuture(final T result) {
        this(result, false);
    }

    private InstantFuture(final T result, final boolean cancelled) {
        this.result = result;
        this.cancelled = cancelled;
    }

    @SuppressWarnings("unchecked")
    public static <T> Future<T> cancelled() {
        return (Future<T>) CANCELLED;
    }

    public static Future<?> completeEmpty() {
        return EMPTY;
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
    public T get(final long timeout, final TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
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
