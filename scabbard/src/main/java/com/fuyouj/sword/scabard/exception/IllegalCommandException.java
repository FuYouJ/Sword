package com.fuyouj.sword.scabard.exception;

import com.fuyouj.sword.scabard.error.ErrorCode;

public class IllegalCommandException extends SwordException {
    private static final int FORBIDDEN = 403;
    private static final int NOT_FOUND = 404;
    private static final int BAD_REQUEST = 400;
    private static final int NOT_AUTHORIZED = 401;
    private static final int GONE = 410;

    public IllegalCommandException(final String message, final ErrorCode errorCode) {
        super(message, errorCode);
    }

    public IllegalCommandException(final String message, final ErrorCode errorCode, final int statusCode) {
        super(message, errorCode, statusCode);
    }

    public IllegalCommandException(final ErrorCode errorCode,
                                   final String messageTemplate,
                                   final Integer statusCode,
                                   final Object... args) {
        super(errorCode, messageTemplate, statusCode, args);
    }

    public IllegalCommandException(final ErrorCode errorCode, final String messageTemplate, final Object... args) {
        super(errorCode, messageTemplate, args);
    }

    public static IllegalCommandException badCommand(final ErrorCode errorCode,
                                                     final String messageTemplate,
                                                     final Object... args) {
        return new IllegalCommandException(errorCode, messageTemplate, args);
    }

    public static IllegalCommandException forbidden(final ErrorCode errorCode,
                                                    final String messageTemplate,
                                                    final Object... args) {
        return new IllegalCommandException(errorCode, messageTemplate, FORBIDDEN, args);
    }

    public static IllegalCommandException notAuthorized(final ErrorCode errorCode,
                                                        final String messageTemplate,
                                                        final Object... args) {
        return new IllegalCommandException(errorCode, messageTemplate, NOT_AUTHORIZED, args);
    }

    public static IllegalCommandException notFound(final ErrorCode errorCode,
                                                   final String messageTemplate,
                                                   final Object... args) {
        return new IllegalCommandException(errorCode, messageTemplate, NOT_FOUND, args);
    }
}

