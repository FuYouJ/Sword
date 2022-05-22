package com.fuyouj.sword.scabard;

import java.time.temporal.ChronoUnit;
import java.util.Arrays;

import lombok.Getter;

public enum DateUnit {
    Second(1, ChronoUnit.SECONDS),
    Minute(1, ChronoUnit.MINUTES),
    Hour(1, ChronoUnit.HOURS),
    Day(1, ChronoUnit.DAYS),
    Week(1, ChronoUnit.WEEKS),
    Month(1, ChronoUnit.MONTHS),
    Season(3, ChronoUnit.MONTHS),
    SemiYear(6, ChronoUnit.MONTHS),
    Year(1, ChronoUnit.YEARS);

    @Getter
    private final int count;
    @Getter
    private final ChronoUnit unit;

    DateUnit(final int count, final ChronoUnit unit) {
        this.count = count;
        this.unit = unit;
    }

    public static DateUnit of(final ChronoUnit unit) {
        return Arrays.stream(values())
                .filter(dateUnit -> dateUnit.count == 1 && dateUnit.unit == unit)
                .findFirst()
                .orElse(DateUnit.Day);
    }
}

