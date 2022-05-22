package com.fuyouj.sword.database.data.filter;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@EqualsAndHashCode
@Getter
public class FalseFilter implements StaticFilter {
    private final boolean result = false;

    @Override
    public boolean isTrue() {
        return this.result;
    }
}
