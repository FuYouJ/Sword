package com.fuyouj.sword.database.data.array;

import java.util.Map;
import java.util.Objects;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class AddOrUpdateResult {
    public static final AddOrUpdateResult FAILED = new AddOrUpdateResult(Status.UnChanged, -1, null, null);

    private final Status status;
    private final long index;
    private final String primaryValue;
    private final Map<String, Entry> diff;

    public static AddOrUpdateResult added(final long index, final String primaryValue) {
        return new AddOrUpdateResult(Status.Added, index, primaryValue, null);
    }

    public static AddOrUpdateResult changed(final long index, final String primaryValue, final Map<String, Entry> diff) {
        return new AddOrUpdateResult(Status.Changed, index, primaryValue, diff);
    }

    public static AddOrUpdateResult unchanged(final long index, final String primaryValue) {
        return new AddOrUpdateResult(Status.UnChanged, index, primaryValue, null);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        AddOrUpdateResult that = (AddOrUpdateResult) o;
        return status == that.status && index == that.index && Objects.equals(primaryValue, that.primaryValue);
    }

    public boolean hasChange() {
        return this.status == Status.Changed;
    }

    @Override
    public int hashCode() {
        return Objects.hash(status, index, primaryValue);
    }

    public boolean isAdded() {
        return this.status == Status.Added;
    }

    public boolean isFailed() {
        return this.index < 0;
    }

    @Override
    public String toString() {
        return "AddOrUpdateResult{" + "status=" + status + ", index=" + index + ", primaryValue=" + primaryValue + '}';
    }

    public enum Status {
        Added, Changed, UnChanged
    }
}

