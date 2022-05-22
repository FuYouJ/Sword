package com.fuyouj.sword.database.object;

import java.util.Map;

public interface DataObjectMapperProvider {
    Map<String, Class<?>> getAllAliasMapper();

    default Class<?> getByAlias(String alias) {
        final Map<String, Class<?>> allAliasMapper = getAllAliasMapper();

        if (allAliasMapper == null) {
            return null;
        }

        return allAliasMapper.get(alias);
    }

    Map<String, Class<?>> getDataObjectMapper();
}
