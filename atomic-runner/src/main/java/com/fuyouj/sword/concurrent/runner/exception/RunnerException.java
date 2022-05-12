package com.fuyouj.sword.concurrent.runner.exception;

import lombok.Getter;

public class RunnerException extends RuntimeException {
    @Getter
    private Exception cause;

    public RunnerException(final Exception cause) {
        this.cause = cause;
    }
}
