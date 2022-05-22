package com.fuyouj.sword.database.object;

import java.util.function.Consumer;

public interface StorageEngine<T> {
    T add(T item);

    void clean();

    Cursor<T> cursor(QueryOption<T> queryOption);

    void iterate(Consumer<T> consumer);

    void removeIf(Matcher<T> matcher);
}
