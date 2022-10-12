package com.fuyouj.sword.database.object;

import java.util.Map;
import java.util.Set;

import com.fuyouj.sword.scabard.Maps2;

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

    default Map<Class<?>, Set<Class<?>>> getSubClasses() {
        return Maps2.staticEmpty();
    }
}
