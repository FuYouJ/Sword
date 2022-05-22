package com.fuyouj.sword.database.wal;

import com.fuyouj.sword.database.object.annotation.DataPropIgnored;

public abstract class Command {
    @DataPropIgnored
    String key;
    @DataPropIgnored
    EntryType type;

    public Command() {
    }

    public Command(final String key, final EntryType type) {
        this.key = key;
        this.type = type;
    }

    public EntryType getEntryType() {
        return this.type;
    }

    public String getKey() {
        return this.key;
    }
}
