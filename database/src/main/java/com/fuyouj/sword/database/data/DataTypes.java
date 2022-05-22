package com.fuyouj.sword.database.data;

import java.util.HashMap;
import java.util.Map;

import com.fuyouj.sword.database.exception.DataTypeNotSupportedYet;
import com.fuyouj.sword.scabard.Asserts;

public class DataTypes {
    private static final Map<Class<?>, DataType> DATA_TYPE_MAPPER = new HashMap<>();

    static {
        DATA_TYPE_MAPPER.put(String.class, DataType.Text);
        DATA_TYPE_MAPPER.put(Long.class, DataType.Int64);
        DATA_TYPE_MAPPER.put(Integer.class, DataType.Int32);

    }

    public static DataType check(final Object obj) {
        DataType dataType = identify(obj);

        if (dataType == null) {
            throw new DataTypeNotSupportedYet();
        }

        return dataType;
    }

    public static DataType identify(final Object obj) {
        Asserts.hasArg(obj, "class must not be null");

        if (obj instanceof Class) {
            return DATA_TYPE_MAPPER.get(obj);
        }

        return DATA_TYPE_MAPPER.get(obj.getClass());
    }
}
