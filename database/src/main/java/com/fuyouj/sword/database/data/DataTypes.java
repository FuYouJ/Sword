package com.fuyouj.sword.database.data;

import java.util.HashMap;
import java.util.Map;

import com.fuyouj.sword.database.exception.DataTypeNotSupportedYet;
import com.fuyouj.sword.scabard.Asserts;

public class DataTypes {
    private static final Map<Class<?>, DataType> DATA_TYPE_MAPPER = new HashMap<>();
    private static final Map<Byte, DataType> BYTE_TYPE_MAPPER = new HashMap<>();

    static {
        DATA_TYPE_MAPPER.put(String.class, DataType.Text);
        DATA_TYPE_MAPPER.put(Long.class, DataType.Int64);
    }

    static {
        BYTE_TYPE_MAPPER.put((byte) 0, DataType.Text);
        BYTE_TYPE_MAPPER.put((byte) 1, DataType.Int64);
    }

    public static DataType check(final Object obj) {
        DataType dataType = identify(obj);

        if (dataType == null) {
            throw new DataTypeNotSupportedYet();
        }

        return dataType;
    }

    public static DataType fromByte(final byte bt) {
        DataType dataType = BYTE_TYPE_MAPPER.get(bt);

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
