package com.fuyouj.sword.database.snapshot;

import java.util.Collections;
import java.util.Set;

public interface SnapshotManager<TSnapshotData extends SnapshotData> {
    SnapshotManager NO_OP_MANAGER = new SnapshotManager<>() {
        @Override
        public void clean(final String key) {

        }

        @Override
        public void endRead(final String key) {

        }

        @Override
        public SnapshotData readSnapshotData(final String key) {
            return null;
        }

        @Override
        public Set<String> snapshotKeys() {
            return Collections.emptySet();
        }

        @Override
        public void write(final String key, final SnapshotData source) {

        }
    };

    void clean(String key);

    void endRead(String key);

    TSnapshotData readSnapshotData(String key);

    Set<String> snapshotKeys();

    void write(String key, TSnapshotData source);
}
