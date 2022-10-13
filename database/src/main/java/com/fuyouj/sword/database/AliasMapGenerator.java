package com.fuyouj.sword.database;

import java.util.Map;

import com.fuyouj.sword.database.object.DataObjectMapperProvider;

public class AliasMapGenerator {
    private final DataObjectMapperProvider mapperProvider;

    public AliasMapGenerator(final DataObjectMapperProvider mapperProvider) {
        this.mapperProvider = mapperProvider;
    }

    public String generate() {
        final StringBuilder stringBuilder = new StringBuilder();
        final Map<String, Class<?>> aliasMapper = mapperProvider.getAllAliasMapper();
        for (String key : aliasMapper.keySet()) {
            stringBuilder.append(key).append("=").append(aliasMapper.get(key).getName()).append("\n");
        }
        return stringBuilder.toString();
    }
}
