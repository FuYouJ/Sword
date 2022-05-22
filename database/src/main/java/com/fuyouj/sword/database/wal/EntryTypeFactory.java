package com.fuyouj.sword.database.wal;

import java.util.HashMap;
import java.util.Map;

import com.fuyouj.sword.database.wal.exception.EntryTypeNotValid;

class EntryTypeFactory {
    private static final Map<Byte, EntryType> ENTRY_TYPE_MAP = new HashMap<>();

    static {
        for (EntryType entryType : EntryType.values()) {
            ENTRY_TYPE_MAP.put(entryType.getType(), entryType);
        }
    }

    static EntryType check(final byte entryCode) {
        EntryType found = ENTRY_TYPE_MAP.get(entryCode);

        if (found == null) {
            throw new EntryTypeNotValid();
        }

        return found;
    }
}
