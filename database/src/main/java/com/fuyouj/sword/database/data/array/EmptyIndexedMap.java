package com.fuyouj.sword.database.data.array;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import com.fuyouj.sword.scabard.Maps2;

public class EmptyIndexedMap<Key, Value> implements IndexedMap<Key, Value> {
    private static final IndexedMap EMPTY = new EmptyIndexedMap();

    public static <Key, Value> IndexedMap<Key, Value> empty() {
        return (IndexedMap<Key, Value>) EMPTY;
    }

    @Override
    public void clear() {

    }

    @Override
    public boolean containsKey(final Object key) {
        return false;
    }

    @Override
    public boolean containsValue(final Object value) {
        return false;
    }

    @Override
    public Set<Entry<Key, Value>> entrySet() {
        return Collections.emptySet();
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj == this) {
            return true;
        }

        if (obj instanceof Map) {
            return Objects.equals(this.getMap(), obj);
        }

        return false;
    }

    @Override
    public Value get(final Object key) {
        return null;
    }

    @Override
    public long getIndex() {
        return -1L;
    }

    @Override
    public Map<Key, Value> getMap() {
        return Maps2.staticEmpty();
    }

    @Override
    public Map<Key, Value> getMemorySharedMap() {
        return Maps2.staticEmpty();
    }

    @Override
    public boolean isEmpty() {
        return true;
    }

    @Override
    public Set<Key> keySet() {
        return Collections.emptySet();
    }

    @Override
    public Value put(final Key key, final Value value) {
        return null;
    }

    @Override
    public void putAll(final Map<? extends Key, ? extends Value> m) {

    }

    @Override
    public Value remove(final Object key) {
        return null;
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public Collection<Value> values() {
        return Collections.emptyList();
    }
}

