package com.fuyouj.sword.scabard;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class Maps2 {
    public static <K, V> Map<K, V> staticEmpty() {
        return Collections.emptyMap();
    }
    public static <K, V> Map<K, V> of(final K key1, final V value1) {
        HashMap<K, V> hashMap = new HashMap<>(1);

        hashMap.put(key1, value1);

        return hashMap;
    }
    public static <K, V> Map<K, V> of(final Object... keysAndValues) {
        if (keysAndValues == null || keysAndValues.length == 0) {
            return staticEmpty();
        }

        int length = keysAndValues.length;
        if (length % 2 == 1) {
            length--;
        }

        HashMap<K, V> hashMap = new LinkedHashMap<>(length / 2);

        for (int i = 0; i < length; i++) {
            @SuppressWarnings("unchecked")
            K key = (K) keysAndValues[i++];
            @SuppressWarnings("unchecked")
            V value = (V) keysAndValues[i];

            hashMap.put(key, value);
        }

        return hashMap;
    }
    public static <K, V> Map<K, V> empty() {
        return new HashMap<>();
    }

    public static boolean isNullOrEmpty(final Map map) {
        return map == null || map.isEmpty();
    }
}
