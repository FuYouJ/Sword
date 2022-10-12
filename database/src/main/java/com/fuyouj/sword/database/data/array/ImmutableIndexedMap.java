package com.fuyouj.sword.database.data.array;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

public class ImmutableIndexedMap<Key, Value> implements IndexedMap<Key, Value> {
    private final long index;
    private final Map<Key, Value> value;

    public ImmutableIndexedMap(final long index, final Map<Key, Value> value) {
        this.index = index;
        this.value = value;
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException("ImmutableIndexedMap is immutable");
    }

    @Override
    public boolean containsKey(final Object key) {
        return this.value.containsKey(key);
    }

    @Override
    public boolean containsValue(final Object value) {
        return this.value.containsValue(value);
    }

    @Override
    public Set<Entry<Key, Value>> entrySet() {
        return this.value.entrySet();
    }

    @Override
    public Value get(final Object key) {
        return this.value.get(key);
    }

    @Override
    public long getIndex() {
        return this.index;
    }

    @Override
    public Map<Key, Value> getMap() {
        return this.value;
    }

    @Override
    public Map<Key, Value> getMemorySharedMap() {
        return this.value;
    }

    @Override
    public boolean isEmpty() {
        return this.value.isEmpty();
    }

    @Override
    public Set<Key> keySet() {
        return this.value.keySet();
    }

    @Override
    public Value put(final Key key, final Value value) {
        throw new UnsupportedOperationException("ImmutableIndexedMap is immutable");
    }

    @Override
    public void putAll(final Map<? extends Key, ? extends Value> m) {
        throw new UnsupportedOperationException("ImmutableIndexedMap is immutable");
    }

    @Override
    public Value remove(final Object key) {
        throw new UnsupportedOperationException("ImmutableIndexedMap is immutable");
    }

    @Override
    public int size() {
        return this.value.size();
    }

    @Override
    public Collection<Value> values() {
        return this.value.values();
    }
}

