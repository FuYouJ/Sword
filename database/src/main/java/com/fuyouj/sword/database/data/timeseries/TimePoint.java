package com.fuyouj.sword.database.data.timeseries;

import java.util.Objects;

public final class TimePoint<Value> {
    private final long timestamp;
    private final Value value;

    public TimePoint(final long timestamp, final Value value) {
        this.timestamp = timestamp;
        this.value = value;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        TimePoint<?> timePoint = (TimePoint<?>) obj;
        return timestamp == timePoint.timestamp && Objects.equals(value, timePoint.value);
    }

    public long getTimestamp() {
        return timestamp;
    }

    public Value getValue() {
        return value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(timestamp, value);
    }

    @Override
    public String toString() {
        return "{t=" + timestamp + ", v=" + value + '}';
    }
}
