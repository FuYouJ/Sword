package com.fuyouj.sword.scabard;

import java.util.concurrent.TimeUnit;

import com.fuyouj.sword.scabard.error.CommonErrorCode;

import static com.fuyouj.sword.scabard.exception.IllegalCommandException.badCommand;

public class Timestamp {
    private static final long LOWER_BOUND = -99999999999999L;
    private static final long HIGHER_BOUND = 9999999999999L;
    private static final long TRANSLATOR_POINT = 9999999999L;
    private static final long SECONDS_MS = 1000L;

    private long timestamp;

    private Timestamp(final long timestamp) {
        this.setTimestamp(timestamp);
    }

    public static Timestamp of(final long timestamp) {
        return new Timestamp(timestamp);
    }

    public boolean isAfter(final Timestamp timestamp) {
        return ms() > timestamp.ms();
    }

    public boolean isNotBefore(final Timestamp timestamp) {
        return ms() >= timestamp.ms();
    }

    public boolean isSecond() {
        return Math.abs(timestamp) <= TRANSLATOR_POINT;
    }

    public long ms() {
        if (isSecond()) {
            return TimeUnit.SECONDS.toMillis(timestamp);
        }

        return timestamp;
    }

    public long seconds() {
        if (isSecond()) {
            return timestamp;
        }

        return TimeUnit.MILLISECONDS.toSeconds(timestamp);
    }

    public Timestamp secondsAfter(final Long coolDownTime) {
        if (coolDownTime == null) {
            return Timestamp.of(timestamp);
        }

        if (isSecond()) {
            return Timestamp.of(timestamp + coolDownTime);
        } else {
            return Timestamp.of(timestamp + coolDownTime * SECONDS_MS);
        }

    }

    public Timestamp secondsBefore(final long offset) {

        if (isSecond()) {
            return Timestamp.of(timestamp - offset);
        }

        return Timestamp.of(timestamp - offset * SECONDS_MS);
    }

    @Override
    public String toString() {
        return DateTimes2.format(timestamp);
    }

    private void setTimestamp(final long timestamp) {
        if (timestamp < LOWER_BOUND || timestamp > HIGHER_BOUND) {
            throw badCommand(CommonErrorCode.PROPERTY_INVALID,
                    "Timestamp [%s] is invalid, it must be in the range of [-99999999999999, 9999999999999]", timestamp);
        }

        this.timestamp = timestamp;
    }
}

