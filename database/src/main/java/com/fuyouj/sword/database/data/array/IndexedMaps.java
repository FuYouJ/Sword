package com.fuyouj.sword.database.data.array;

import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * 表格数据
 *
 * @param <Key>
 * @param <Value>
 */
public interface IndexedMaps<Key, Value> extends Iterable<IndexedMap<Key, Value>> {
    IndexedValues<Value> get(Key key);

    Map<Key, IndexedValues<Value>> getAll();

    Set<String> getKeys();

    int getSize();

    default boolean isEmpty() {
        return this.getSize() <= 0;
    }

    default Stream<IndexedMap<Key, Value>> stream() {
        return StreamSupport.stream(this.spliterator(), false);
    }
}
