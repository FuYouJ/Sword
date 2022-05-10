package com.fuyouj.sword.concurrent.runner;

public class AtomicResult<T> {
    private static final AtomicResult<?> FAILED = new AtomicResult<>(null, AtomicResultState.Failed);
    private static final AtomicResult<?> CANCELED = new AtomicResult<>(null, AtomicResultState.Canceled);
    private static final AtomicResult<?> TIMEOUT = new AtomicResult<>(null, AtomicResultState.Timeout);
    private final T result;
    private final AtomicResultState state;

    public AtomicResult(final T result, final AtomicResultState state) {
        this.result = result;
        this.state = state;
    }

    @SuppressWarnings("unchecked")
    public static <T> AtomicResult<T> canceled() {
        return (AtomicResult<T>) CANCELED;
    }

    @SuppressWarnings("unchecked")
    public static <T> AtomicResult<T> failed() {
        return (AtomicResult<T>) FAILED;
    }

    public static <T> AtomicResult<T> success(final T result) {
        return new AtomicResult<>(result, AtomicResultState.Success);
    }

    @SuppressWarnings("unchecked")
    public static <T> AtomicResult<T> timeout() {
        return (AtomicResult<T>) TIMEOUT;
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
