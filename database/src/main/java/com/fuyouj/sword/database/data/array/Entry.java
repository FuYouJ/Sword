package com.fuyouj.sword.database.data.array;

import lombok.Getter;

@Getter
public class Entry {
    private final Object origin;
    private final Object updated;
    private final boolean isUpdate;

    public Entry(final Object origin, final Object updated, final boolean isUpdate) {
        this.origin = origin;
        this.updated = updated;
        this.isUpdate = isUpdate;
    }

    public static Entry insert(final Object origin, final Object updated) {
        return new Entry(origin, updated, false);
    }

    public static Entry update(final Object origin, final Object updated) {
        return new Entry(origin, updated, true);
    }
}

