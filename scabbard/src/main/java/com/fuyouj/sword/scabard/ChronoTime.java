package com.fuyouj.sword.scabard;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@EqualsAndHashCode
public class ChronoTime {
    @Getter
    private final ChronoUnit unit;
    @Getter
    private final LocalDateTime dateTime;
    @Getter
    private final long amount;

    public ChronoTime(final ChronoUnit unit, final LocalDateTime dateTime) {
        this(unit, dateTime, 1);
    }

    public ChronoTime(final ChronoUnit unit, final LocalDateTime dateTime, final long amount) {
        this.unit = unit;
        this.dateTime = dateTime;
        this.amount = amount;
    }

    public boolean isIntervalLessThan(final ChronoUnit unit) {
        if (unit.isDateBased() || this.unit.isDateBased()) {
            return false;
        }
        return !unit.getDuration().minus(Duration.of(amount, this.unit)).isNegative();
    }

    @Override
    public String toString() {
        return "ChronoTime{"
                + "unit=" + unit
                + ", dateTime=" + dateTime
                + '}';
    }

    public ChronoTime truncatedTo(final ChronoUnit unit) {
        return new ChronoTime(unit, DateTimes2.truncatedToBeginningOf(dateTime, unit));
    }

    public ChronoTime withSecondsInterval(final long seconds) {
        return new ChronoTime(ChronoUnit.SECONDS, dateTime, seconds);
    }
}

