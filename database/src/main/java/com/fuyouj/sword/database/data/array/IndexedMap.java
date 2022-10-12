package com.fuyouj.sword.database.data.array;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fuyouj.sword.scabard.Lists2;
import com.fuyouj.sword.scabard.Maps2;

/**
 * 一行数据
 *
 * @param <Key>
 * @param <Value>
 */
public interface IndexedMap<Key, Value> extends Map<Key, Value> {
    default IndexedMap<Key, Value> copy() {
        return new ImmutableIndexedMap<>(getIndex(), this.getMap());
    }

    default IndexedMap<Key, Value> copy(List<Key> keys) {
        if (Lists2.isNullOrEmpty(keys)) {
            return new ImmutableIndexedMap<>(getIndex(), Maps2.staticEmpty());
        }

        final HashMap<Key, Value> copied = new HashMap<>();

        for (Key key : keys) {
            copied.put(key, get(key));
        }

        return new ImmutableIndexedMap<>(getIndex(), copied);
    }

    default boolean exists() {
        return getIndex() > -1;
    }

    long getIndex();

    Map<Key, Value> getMap();

    Map<Key, Value> getMemorySharedMap();
}

