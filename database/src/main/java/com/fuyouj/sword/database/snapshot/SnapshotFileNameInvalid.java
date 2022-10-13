package com.fuyouj.sword.database.snapshot;

public class SnapshotFileNameInvalid extends RuntimeException {
    public SnapshotFileNameInvalid(final String filename) {
        super(String.format("snapshot file with name [%s] is invalid, it must be something like {keyOfTheResource}.snapshot"
                + ".{timestamp}", filename));
    }
}
