package com.fuyouj.sword.database.data.array;

/**
 * 一个单元格
 */
public interface IndexedValue<Value> {
    IndexedValue EMPTY = new IndexedValue() {
        @Override
        public boolean exists() {
            return false;
        }

        @Override
        public long getIndex() {
            return -1L;
        }

        @Override
        public Object getValue() {
            return null;
        }
    };

    @SuppressWarnings("unchecked")
    static <Value> IndexedValue<Value> empty() {
        return EMPTY;
    }

    default boolean exists() {
        return getIndex() > -1;
    }

    long getIndex();

    Value getValue();
}

