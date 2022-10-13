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
        public void clean(final String key) {

        }

        @Override
        public long count(final String key) {
            return 0L;
        }

        @Override
        public void fsync(final String key) {

        }

        @Override
        public Map<String, Iterator<Command>> readWALs() {
            return Maps2.staticEmpty();
        }

        @Override
        public long size(final String key) {
            return 0;
        }
    };

    @Atomic
    long append(Command cmd);

    @Atomic
    void clean(String key);

    long count(String key);

    @Atomic
    void fsync(String key);

    Map<String, Iterator<Command>> readWALs();

    long size(String key);
}
