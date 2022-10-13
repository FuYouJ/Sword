package com.fuyouj.sword.database;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Optional;

import com.fuyouj.sword.database.exception.UnableToAccessStoragePath;
import com.fuyouj.sword.scabard.Strings;

public class PersistentConfiguration {
    public static final String[] JARVIS_PACKAGE = new String[]{
            "com.thingworks.jarvis",
            "net.thingworks.jarvis"
    };
    public static final int ONE_K = 1024;
    public static final int ONE_HUNDRED = 100;
    public static final int FIVE_MINUTE = 300;
    public static final int FIFTEEN_MINUTE = 900;
    private static final int DEFAULT_FLUSH_SECONDS = 30;
    public static final PersistentConfiguration DEFAULT_CONFIG = new PersistentConfiguration(
            false,
            Path.of(".").toAbsolutePath().toString(),
            false,
            DEFAULT_FLUSH_SECONDS,
            false,
            JARVIS_PACKAGE
    );

    private final String storagePath;
    private final boolean forceFsync;
    private final int flushEverySeconds;
    private final String[] dataObjectPackageName;
    private final boolean enabledPersistent;
    private final boolean ignoreErrorWhileReplaying;
    /**
     * 对象存储的wal文件最大值，超过该值将会被归档
     */
    private int objectStoreWalFileMaxSize = ONE_K * ONE_K * ONE_HUNDRED;
    /**
     * 对象存储归档检查频率（秒）
     */
    private int objectStoreArchiveRunPerSeconds = FIFTEEN_MINUTE;

    public PersistentConfiguration(final boolean enablePersistent,
                                   final String storagePath,
                                   final boolean forceFsync,
                                   final int flushEverySeconds,
                                   final String[] dataObjectPackageName) {
        this(enablePersistent, storagePath, forceFsync, flushEverySeconds, false, dataObjectPackageName);
    }

    public PersistentConfiguration(final boolean enablePersistent,
                                   final String storagePath,
                                   final boolean forceFsync,
                                   final int flushEverySeconds,
                                   final boolean ignoreErrorWhileReplaying,
                                   final String[] dataObjectPackageName) {
        this.enabledPersistent = enablePersistent;
        this.storagePath = storagePath;
        this.forceFsync = forceFsync;
        this.flushEverySeconds = flushEverySeconds;
        this.ignoreErrorWhileReplaying = ignoreErrorWhileReplaying;
        this.dataObjectPackageName = Optional.ofNullable(dataObjectPackageName).orElse(JARVIS_PACKAGE);

        this.createStoragePathIfNotExists();
    }

    public PersistentConfiguration(final boolean enablePersistent,
                                   final String storagePath,
                                   final boolean forceFsync,
                                   final int flushEverySeconds) {
        this(enablePersistent, storagePath, forceFsync, flushEverySeconds, false, JARVIS_PACKAGE);
    }

    public PersistentConfiguration(final boolean enablePersistent,
                                   final String storagePath,
                                   final boolean forceFsync) {
        this(enablePersistent, storagePath, forceFsync, DEFAULT_FLUSH_SECONDS, false, JARVIS_PACKAGE);
    }

    public PersistentConfiguration(final boolean enablePersistent, final String storagePath) {
        this(enablePersistent, storagePath, true, DEFAULT_FLUSH_SECONDS, false, JARVIS_PACKAGE);
    }

    public String[] getDataObjectPackageName() {
        return dataObjectPackageName;
    }

    public int getFlushEverySeconds() {
        return flushEverySeconds;
    }

    public int getObjectStoreArchiveRunPerSeconds() {
        return Math.max(this.objectStoreArchiveRunPerSeconds, FIVE_MINUTE);
    }

    public void setObjectStoreArchiveRunPerSeconds(final int objectStoreArchiveRunPerSeconds) {
        this.objectStoreArchiveRunPerSeconds = objectStoreArchiveRunPerSeconds;
    }

    public int getObjectStoreWalFileMaxSize() {
        return objectStoreWalFileMaxSize;
    }

    public void setObjectStoreWalFileMaxSize(final int objectStoreWalFileMaxSize) {
        this.objectStoreWalFileMaxSize = objectStoreWalFileMaxSize;
    }

    public String getStoragePath() {
        return storagePath;
    }

    public boolean isForceFsync() {
        return forceFsync;
    }

    public boolean isIgnoreErrorWhileReplaying() {
        return ignoreErrorWhileReplaying;
    }

    public boolean isPersistentEnabled() {
        return enabledPersistent;
    }

    public PersistentConfiguration subPath(final String subPath) {
        return new PersistentConfiguration(
                this.enabledPersistent, Path.of(this.storagePath).resolve(subPath).toAbsolutePath().toString(),
                this.forceFsync,
                this.flushEverySeconds,
                this.ignoreErrorWhileReplaying,
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

    private void createStoragePathIfNotExists() {
        try {
            if (this.isPersistentEnabled() && !Strings.isNullOrEmpty(this.storagePath)) {
                final Path path = Path.of(this.storagePath);

                if (!Files.exists(path)) {
                    Files.createDirectories(path);
                }
            }
        } catch (IOException e) {
            throw new UnableToAccessStoragePath(e.getMessage());
        }
    }
}
