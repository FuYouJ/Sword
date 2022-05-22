package com.fuyouj.sword.database.wal;

public enum EntryType {
    DataCommand((byte) 1);

    private byte type;

    EntryType(final byte type) {
        this.type = type;
    }

    public byte getType() {
        return type;
    }
}
