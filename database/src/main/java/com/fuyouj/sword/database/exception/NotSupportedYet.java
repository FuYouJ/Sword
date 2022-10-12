package com.fuyouj.sword.database.exception;

import com.fuyouj.sword.scabard.exception.SwordException;

public class NotSupportedYet extends SwordException {
    public NotSupportedYet(final String message) {
        super(message);
    }

    public NotSupportedYet(final String messageTemplate, final Object... msgArgs) {
        super(messageTemplate, msgArgs);
    }
}
