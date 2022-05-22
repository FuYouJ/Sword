package com.fuyouj.sword.database.data.filter;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@EqualsAndHashCode
@Getter
public class TrueFilter implements StaticFilter {
    private final boolean result = true;

    @Override
    public boolean isTrue() {
        return this.result;
    }
}
