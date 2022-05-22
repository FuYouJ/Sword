package com.fuyouj.sword.database;

import java.nio.file.Path;
import java.util.Arrays;

public class PersistentConfiguration {
    private static final int DEFAULT_FLUSH_SECONDS = 30;
    private static final String[] JARVIS_PACKAGE = new String[]{
            "com.thingworks.jarvis",
            "net.thingworks.jarvis"
    };
    public static final PersistentConfiguration DEFAULT_CONFIG = new PersistentConfiguration(
            false,
            Path.of(".").toAbsolutePath().toString(),
            false,
            DEFAULT_FLUSH_SECONDS,
            JARVIS_PACKAGE
    );
    private final String storagePath;
    private final boolean forceFsync;
    private final int flushEverySeconds;
    private final String[] dataObjectPackageName;
    private final boolean enabledPersistent;

    public PersistentConfiguration(final boolean enablePersistent,
                                   final String storagePath,
                                   final boolean forceFsync,
                                   final int flushEverySeconds,
                                   final String[] dataObjectPackageName) {
        this.enabledPersistent = enablePersistent;
        this.storagePath = storagePath;
        this.forceFsync = forceFsync;
        this.flushEverySeconds = flushEverySeconds;
        this.dataObjectPackageName = dataObjectPackageName;
    }

    public String[] getDataObjectPackageName() {
        return dataObjectPackageName;
    }

    public int getFlushEverySeconds() {
        return flushEverySeconds;
    }

    public String getStoragePath() {
        return storagePath;
    }

    public boolean isForceFsync() {
        return forceFsync;
    }

    public boolean isPersistentEnabled() {
        return enabledPersistent;
    }

    public PersistentConfiguration subPath(final String subPath) {
        return new PersistentConfiguration(
                false, Path.of(this.storagePath).resolve(subPath).toAbsolutePath().toString(),
                this.forceFsync,
                this.flushEverySeconds,
                this.dataObjectPackageName
        );
    }

    @Override
    public String toString() {
        return "PersistentConfiguration{"
                + "storagePath='"
                + storagePath
                + '\''
                + ", forceFsync="
                + forceFsync
                + ", flushEverySeconds="
                + flushEverySeconds
                + ", dataObjectPackageName="
                + Arrays.toString(dataObjectPackageName)
                + '}';
    }
}
