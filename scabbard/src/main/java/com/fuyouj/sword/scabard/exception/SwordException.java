package com.fuyouj.sword.scabard.exception;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import com.fuyouj.sword.scabard.Asserts;
import com.fuyouj.sword.scabard.Lists2;
import com.fuyouj.sword.scabard.error.ErrorCode;

import lombok.Getter;

public class SwordException extends RuntimeException {
    @Getter
    private final ErrorCode errorCode;
    @Getter
    private final Integer statusCode;
    @Getter
    private final CommonError error;
    @Getter
    private Object[] patternArgs;
    @Getter
    private Object[] msgArgs;

    public SwordException(final String messageTemplate, final Object... msgArgs) {
        this(null, messageTemplate, msgArgs);
    }

    public SwordException(final ErrorCode errorCode,
                           final String messageTemplate,
                           final Integer statusCode,
                           final Object... args) {
        this(messageTemplate, errorCode, statusCode);
        this.msgArgs = args.clone();
    }

    public SwordException(final ErrorCode errorCode, final String messageTemplate, final Object... args) {
        this(messageTemplate, errorCode);
        this.msgArgs = args.clone();
    }

    public SwordException(final String message) {
        this(message, null, null);
    }

    public SwordException(final String message, final ErrorCode errorCode) {
        this(message, errorCode, null);
    }

    public SwordException(final String message, final ErrorCode errorCode, final Integer statusCode) {
        this(message, errorCode, statusCode, null);
    }

    public SwordException(final String message, final ErrorCode errorCode, final Integer statusCode,
                           final CommonError error) {
        super(message);
        this.errorCode = errorCode;
        this.statusCode = statusCode;
        this.error = error;
    }

    public boolean causeBy(final ErrorCode errorCode) {
        if (errorCode == null) {
            return false;
        }

        return Objects.equals(this.errorCode, errorCode);
    }

    public <T> List<T> extractErrorDetails(final Class<T> errorDetailType) {
        if (error == null) {
            return Lists2.staticEmpty();
        }

        Asserts.hasArg(errorDetailType, "parameter 'errorDetailType' is required");

        return Lists2.mapNotNull(error.getDetail(),
                detail -> Optional.ofNullable(detail).filter(errorDetailType::isInstance).map(errorDetailType
                        ::cast).orElse(null));
    }

    public List<Object> extractRawDetails() {
        if (error == null) {
            return Lists2.staticEmpty();
        }

        return error.getDetail();
    }

    @Override
    public String getMessage() {
        if (this.msgArgs != null) {
            return String.format(super.getMessage(), this.msgArgs);
        }

        return super.getMessage();
    }
}
