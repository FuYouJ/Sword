package com.fuyouj.sword.database.data.array;

import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.fuyouj.sword.database.utils.Iterators;
import com.fuyouj.sword.scabard.Maps2;

public class EmptyIndexedMaps<Key, Value> implements IndexedMaps<Key, Value> {
    private static final IndexedMaps EMPTY = new EmptyIndexedMaps<>();

    @SuppressWarnings("unchecked")
    public static <Key, Value> IndexedMaps<Key, Value> empty() {
        return (IndexedMaps<Key, Value>) EMPTY;
    }

    @Override
    public IndexedValues<Value> get(final Key key) {
        return null;
    }

    @Override
    public Map<Key, IndexedValues<Value>> getAll() {
        return Maps2.staticEmpty();
    }

    @Override
    public Set<String> getKeys() {
        return Collections.emptySet();
    }

    @Override
    public int getSize() {
        return 0;
    }

    @Override
    public Iterator<IndexedMap<Key, Value>> iterator() {
        return Iterators.empty();
    }

}
