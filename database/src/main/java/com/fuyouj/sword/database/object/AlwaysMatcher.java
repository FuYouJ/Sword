package com.fuyouj.sword.database.object;

public class AlwaysMatcher<T> implements Matcher<T> {
    private static final Matcher TRUE = new AlwaysMatcher<>(true);
    private static final Matcher FALSE = new AlwaysMatcher<>(false);
    private final boolean allMatch;

    private AlwaysMatcher(final boolean allMatch) {
        this.allMatch = allMatch;
    }

    public static <T> Matcher<T> allMatch() {
        return (Matcher<T>) TRUE;
    }

    public static <T> Matcher<T> noneMatch() {
        return (Matcher<T>) FALSE;
    }

    @Override
    public boolean match(final T item) {
        return this.allMatch;
    }
}
