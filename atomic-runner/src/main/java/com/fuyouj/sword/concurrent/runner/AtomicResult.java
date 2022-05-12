package com.fuyouj.sword.concurrent.runner;

import com.fuyouj.sword.concurrent.runner.exception.CommandFailedException;

public class AtomicResult<T> {
    private static final AtomicResult<?> FAILED = new AtomicResult<>(null, AtomicResultState.Failed);
    private static final AtomicResult<?> CANCELED = new AtomicResult<>(null, AtomicResultState.Canceled);
    private static final AtomicResult<?> TIMEOUT = new AtomicResult<>(null, AtomicResultState.Timeout);
    private final T result;
    private final AtomicResultState state;
    private final Throwable exception;

    public AtomicResult(final T result, final AtomicResultState state) {
        this(result, state, null);
    }

    public AtomicResult(final T result, final AtomicResultState state, final Throwable exception) {
        this.result = result;
        this.state = state;
        this.exception = exception;
    }

    @SuppressWarnings("unchecked")
    public static <T> AtomicResult<T> canceled() {
        return (AtomicResult<T>) CANCELED;
    }

    public static <T> AtomicResult<T> failed(final Throwable e) {
        return new AtomicResult<>(null, AtomicResultState.Failed, e);
    }


    public static <T> AtomicResult<T> success(final T result) {
        return new AtomicResult<>(result, AtomicResultState.Success);
    }

    @SuppressWarnings("unchecked")
    public static <T> AtomicResult<T> timeout() {
        return (AtomicResult<T>) TIMEOUT;
    }

    public T getOrThrow() {
        if (this.state == AtomicResultState.Success) {
            return result;
        }

        if (this.exception != null) {
            throw new CommandFailedException(this.exception);
        }

        throw new CommandFailedException("atomic command failed to execute");
    }

    public T getResult() {
        return result;
    }

    public boolean hasEmptyResult() {
        return this.result == null;
    }

    public boolean isSuccess() {
        return this.state == AtomicResultState.Success;
    }
}