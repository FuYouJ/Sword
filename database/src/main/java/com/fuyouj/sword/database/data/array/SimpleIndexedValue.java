package com.fuyouj.sword.database.data.array;

import java.util.Objects;

public class SimpleIndexedValue<Value> implements IndexedValue<Value> {
    private long index;
    private Value value;

    public SimpleIndexedValue(final long index, final Value value) {
        this.index = index;
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
        SimpleIndexedValue<?> that = (SimpleIndexedValue<?>) obj;
        return index == that.index && Objects.equals(value, that.value);
    }

    public long getIndex() {
        return index;
    }

    void setIndex(final long index) {
        this.index = index;
    }

    public Value getValue() {
        return value;
    }

    void setValue(final Value value) {
        this.value = value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(index, value);
    }

    @Override
    public String toString() {
        return "SimpleIndexedValue{" + "index=" + index + ", value=" + value + '}';
    }
}

