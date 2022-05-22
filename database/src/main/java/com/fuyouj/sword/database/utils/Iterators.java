package com.fuyouj.sword.database.utils;

import java.util.Iterator;

public class Iterators {
    private static final Iterator<?> EMPTY = new Iterator<>() {
        @Override
        public boolean hasNext() {
            return false;
        }

        @Override
        public Object next() {
            return null;
        }
    };

    @SuppressWarnings("unchecked")
    public static <T> Iterator<T> empty() {
        return (Iterator<T>) EMPTY;
    }
}
