package com.fuyouj.sword.database.data.array;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode
public class AddResult {
    public static final AddResult FAILED = new AddResult(-1, null);

    private final long index;
    private final String primaryValue;

    public static AddResult added(final long index, final String primaryValue) {
        return new AddResult(index, primaryValue);
    }

    public boolean isSuccess() {
        return this.index >= 0;
    }
}
