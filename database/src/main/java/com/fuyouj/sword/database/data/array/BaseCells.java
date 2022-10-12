package com.fuyouj.sword.database.data.array;

import java.util.Iterator;

import com.fuyouj.sword.scabard.Strings;

public abstract class BaseCells implements Cells, Iterator<Cell> {
    protected String primaryValue;

    @Override
    public void genPrimaryValue(final String gen) {
        if (Strings.isNullOrEmpty(primaryValue)) {
            this.primaryValue = gen;
        }
    }

    @Override
    public String getPrimaryValue() {
        return primaryValue;
    }

}
