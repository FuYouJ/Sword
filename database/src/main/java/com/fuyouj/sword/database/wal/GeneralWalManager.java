package com.fuyouj.sword.database.wal;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import com.fuyouj.sword.concurrent.runner.AtomicConsumer;
import com.fuyouj.sword.concurrent.runner.AtomicRunner;
import com.fuyouj.sword.database.Deserializer;
import com.fuyouj.sword.database.PersistentConfiguration;
import com.fuyouj.sword.database.Serializer;
import com.fuyouj.sword.database.exception.UnableToAccessStoragePath;
import com.fuyouj.sword.scabard.Atomic;
import com.fuyouj.sword.scabard.Lists2;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GeneralWalManager implements WalManager, AtomicConsumer<String> {
    private final PersistentConfiguration configuration;
    private final Serializer serializer;
    private final Deserializer deserializer;
    private final AtomicRunner<String> atomicRunner;
    private final Map<String, WALs> keyedWALs = new ConcurrentHashMap<>();
    private WALState state = WALState.UnLoaded;

    public GeneralWalManager(final AtomicRunner<String> atomicRunner,
                             final PersistentConfiguration configuration,
                             final Serializer serializer,
                             final Deserializer deserializer) {
        this.atomicRunner = atomicRunner;
        this.configuration = Optional.ofNullable(configuration).orElse(PersistentConfiguration.DEFAULT_CONFIG);
        this.serializer = serializer;
        this.deserializer = deserializer;
    }

    @Override
    @Atomic
    public long append(final Command cmd) {
        if (!configuration.isPersistentEnabled()) {
            return 0;
        }

        return doAppend(cmd);
    }

    @Override
    public String buildKey(final String atomicKey) {
        return "GeneralWal:" + atomicKey;
    }

    @Override
    public void clean(final String key) {
        if (this.configuration.isPersistentEnabled()) {
            WALs waLs = this.trySelect(key);
            if (waLs != null) {
                waLs.clean();
            }
        }
    }

    @Override
    public long count(final String key) {
        if (!this.configuration.isPersistentEnabled()) {
            return 0L;
        }

        WALs waLs = this.keyedWALs.get(key);
        if (waLs == null) {
            return 0L;
        }

        return waLs.count();
    }

    @Override
    public void fsync(final String key) {
        if (this.configuration.isPersistentEnabled()) {
            this.select(key).fsync();
        }
    }

    @Override
    public AtomicRunner<String> getRunner() {
        return this.atomicRunner;
    }

    public Map<String, Iterator<Command>> readWALs() {
        log.info("Initial loading write ahead log base on configuration [{}]", configuration);
        this.initWALs();
        log.info("Initial Done, start to load commands from write ahead logs");

        return this.loadCommands();
    }

    @Override
    public long size(final String key) {
        return this.select(key).size();
    }

    private String buildAtomicKey(final String key) {
        return key;
    }

    private long doAppend(final Command cmd) {
        final WALs waLs = this.select(cmd.getKey());
        final long appended = waLs.append(cmd.getEntryType(), this.serializer.serialize(cmd));
        if (configuration.isForceFsync()) {
            waLs.fsync();
        }
        return appended;
    }

    private void ensureStoragePathExists(final Path storagePath) throws IOException {
        if (!Files.exists(storagePath)) {
            Files.createDirectories(storagePath);
        }
    }

    @Atomic
    private void initWALs() {
        this.atomicRun("init", () -> {
            if (this.state == WALState.Loaded) {
                return null;
            }

            final Path storagePath = Path.of(this.configuration.getStoragePath());

            try {
                this.ensureStoragePathExists(storagePath);

                List<WalFile> walFiles = Files.list(storagePath)
                        .map(WalFile::new)
                        .filter(WalFile::isValid)
                        .collect(Collectors.toList());
                Map<String, List<WalFile>> walFileGroupedByKey = Lists2.group(walFiles, WalFile::getKey);

                walFileGroupedByKey.forEach((key, files) -> {
                    this.keyedWALs.put(key, WALs.init(key, storagePath, WalFiles.of(files)));
                });

                this.state = WALState.Loaded;
            } catch (IOException e) {
                throw new UnableToAccessStoragePath(e.getMessage());
            }

            return null;
        });
    }

    private Map<String, Iterator<Command>> loadCommands() {
        Map<String, Iterator<Command>> commands = new HashMap<>();

        keyedWALs.forEach((key, waLs) -> commands.put(key, waLs.toCommandIterator(this.deserializer, this.configuration)));

        return commands;
    }

    private WALs select(final String key) {
        return this.keyedWALs.computeIfAbsent(key, walKey -> new WALs(walKey, this.configuration.getStoragePath()));
    }

    private WALs trySelect(final String key) {
        return this.keyedWALs.get(key);
    }

}
