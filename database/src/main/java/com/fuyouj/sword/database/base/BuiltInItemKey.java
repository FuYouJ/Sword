package com.fuyouj.sword.database.base;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import lombok.Getter;

public enum BuiltInItemKey {

    INDEX("_index"),
    SUBMIT_AT("submit_at"),
    SUBMIT_BY("submit_by");

    public static final Set<String> ALL_KEYS;

    static {
        ALL_KEYS = Arrays.stream(BuiltInItemKey.values()).map(BuiltInItemKey::getKey).collect(Collectors.toUnmodifiableSet());
    }

    @Getter
    private String key;

    BuiltInItemKey(final String key) {
        this.key = key;
    }

    public static boolean isBuildInKey(final String key) {
        if (key == null) {
            return false;
        }

        return ALL_KEYS.contains(key);
    }
}
