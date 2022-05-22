package com.fuyouj.sword.database.wal;

import java.util.Iterator;
import java.util.Map;

import com.fuyouj.sword.scabard.Atomic;
import com.fuyouj.sword.scabard.Maps2;

public interface WalManager {
    WalManager NO_OP_MANAGER = new WalManager() {

        @Override
        public long append(final Command cmd) {
            return 0;
        }

        @Override
        public void fsync(final String key) {

        }

        @Override
        public Map<String, Iterator<Command>> readWALs() {
            return Maps2.staticEmpty();
        }
    };

    @Atomic
    long append(Command cmd);

    @Atomic
    void fsync(String key);

    Map<String, Iterator<Command>> readWALs();
}
