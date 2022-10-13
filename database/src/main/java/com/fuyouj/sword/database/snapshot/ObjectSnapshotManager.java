package com.fuyouj.sword.database.snapshot;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import com.fuyouj.sword.concurrent.runner.AtomicConsumer;
import com.fuyouj.sword.concurrent.runner.AtomicRunner;
import com.fuyouj.sword.database.Deserializer;
import com.fuyouj.sword.database.PersistentConfiguration;
import com.fuyouj.sword.database.Serializer;
import com.fuyouj.sword.database.exception.UnableToAccessStoragePath;
import com.fuyouj.sword.scabard.Lists2;
import com.fuyouj.sword.scabard.command.Loggable;

public class ObjectSnapshotManager implements SnapshotManager<GeneralSnapshotData>, Loggable, AtomicConsumer<String> {
    private final PersistentConfiguration configuration;
    private final Serializer serializer;
    private final Deserializer deserializer;
    private final AtomicRunner<String> atomicRunner;

    private final Map<String, GeneralSnapshots> keyedSnapshots = new ConcurrentHashMap<>();
    private SnapshotState state = SnapshotState.UnLoaded;

    public ObjectSnapshotManager(final PersistentConfiguration configuration,
                                 final Serializer serializer,
                                 final Deserializer deserializer,
                                 final AtomicRunner<String> atomicRunner) {
        this.configuration = configuration;
        this.serializer = serializer;
        this.deserializer = deserializer;
        this.atomicRunner = atomicRunner;
    }

    @Override
    public String buildKey(final String atomicKey) {
        return "GeneralSnapshot:" + atomicKey;
    }

    @Override
    public void clean(final String key) {
        //对象快照不删除
    }

    @Override
    public void endRead(final String key) {
        GeneralSnapshots snapshots = this.keyedSnapshots.get(key);
        if (snapshots != null) {
            snapshots.endRead();
        }
    }

    @Override
    public AtomicRunner<String> getRunner() {
        return this.atomicRunner;
    }

    @Override
    public GeneralSnapshotData readSnapshotData(final String key) {
        GeneralSnapshots snapshots = this.keyedSnapshots.get(key);
        if (snapshots == null) {
            return null;
        }

        boolean ignoreError = this.configuration.isIgnoreErrorWhileReplaying();
        final byte[] bytes = snapshots.readAndSelectActive(ignoreError);
        if (bytes == null) {
            return null;
        }

        return this.deserializer.deserialize(bytes, GeneralSnapshotData.class,
                !ignoreError);
    }

    @Override
    public Set<String> snapshotKeys() {
        info("Initial loading snapshots base on configuration [{}]", configuration);

        this.initSnapshots();

        info("Initial Done, start to load snapshots");

        return this.keyedSnapshots.keySet();
    }

    @Override
    public void write(final String key, final GeneralSnapshotData snapshotData) {
        if (snapshotData == null) {
            return;
        }

        GeneralSnapshots snapshots = this.selectSnapshot(key);

        snapshots.startWrite();

        try {
            snapshots.write(key, serializer.serialize(snapshotData));
        } catch (Exception ex) {
            this.warn("occurs error while snapshot [{}] write,reason:[{}]", key, ex.getMessage());

            snapshots.invalid();
            return;
        }

        snapshots.endWrite();
    }

    private void initSnapshots() {
        this.atomicRun("init", () -> {
            if (this.state == SnapshotState.Loaded) {
                return null;
            }

            final Path storagePath = Path.of(this.configuration.getStoragePath());

            try {
                List<SnapshotFile> snapshotFiles = Files.list(storagePath)
                        .map(SnapshotFile::new)
                        .filter(SnapshotFile::isValid)
                        .collect(Collectors.toList());
                Map<String, List<SnapshotFile>> fileGroupedByKey =
                        Lists2.group(snapshotFiles, SnapshotFile::getKey);

                fileGroupedByKey.forEach((key, files) -> {
                    this.keyedSnapshots.put(key, GeneralSnapshots.init(key,
                            storagePath, SnapshotFiles.of(files)));
                });

                this.state = SnapshotState.Loaded;
            } catch (IOException e) {
                throw new UnableToAccessStoragePath(e.getMessage());
            }

            return null;
        });
    }

    private GeneralSnapshots selectSnapshot(final String key) {
        return this.keyedSnapshots.computeIfAbsent(key, k -> new GeneralSnapshots(k,
                this.configuration.getStoragePath()));
    }
}
