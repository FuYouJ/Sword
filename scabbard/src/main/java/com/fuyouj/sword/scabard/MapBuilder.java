package com.fuyouj.sword.scabard;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import lombok.Getter;

public class MapBuilder<K, V> {
    private final List<Entry<K, V>> entries;

    private MapBuilder(final List<Entry<K, V>> entries) {
        this.entries = entries;
    }

    public static <K, V> MapBuilder<K, V> builder(final K key, final V value) {
        if (key != null) {
            return new MapBuilder<>(Lists2.items(new Entry<>(key, value)));
        }

        return instance();
    }

    public static <K, V> MapBuilder<K, V> instance() {
        return new MapBuilder<>(Lists2.newList());
    }

    public Map<K, V> build() {
        return Lists2.map(entries, Entry::getKey, Entry::getValue);
    }

    public Map<K, V> buildLinked() {
        var result = new LinkedHashMap<K, V>();

        Lists2.foreach(entries, entry -> result.put(entry.key, entry.value));

        return result;
    }

    public MapBuilder<K, V> entry(final K key, final V value) {
        if (key == null) {
            return this;
        }

        this.entries.add(new Entry<>(key, value));
        return this;
    }

    @Getter
    private static class Entry<K, V> {
        private final K key;
        private final V value;

        Entry(final K key, final V value) {
            this.key = key;
            this.value = value;
        }
    }
}
