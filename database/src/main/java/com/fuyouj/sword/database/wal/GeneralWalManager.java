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

import com.fuyouj.sword.concurrent.runner.AtomicRunner;
import com.fuyouj.sword.database.Deserializer;
import com.fuyouj.sword.database.PersistentConfiguration;
import com.fuyouj.sword.database.Serializer;
import com.fuyouj.sword.database.exception.UnableToAccessStoragePath;
import com.fuyouj.sword.scabard.Atomic;
import com.fuyouj.sword.scabard.Lists2;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GeneralWalManager implements WalManager {
    private final PersistentConfiguration configuration;
    private final Serializer<Command> serializer;
    private final Deserializer deserializer;
    private final AtomicRunner<String> atomicRunner;
    private final Map<String, WALs> keyedWALs = new ConcurrentHashMap<>();
    private WALState state = WALState.Unloaded;

    public GeneralWalManager(final AtomicRunner<String> atomicRunner,
                             final PersistentConfiguration configuration,
                             final Serializer<Command> serializer,
                             final Deserializer deserializer) {
        this.atomicRunner = atomicRunner;
        this.configuration = Optional.ofNullable(configuration).orElse(PersistentConfiguration.DEFAULT_CONFIG);
        this.serializer = serializer;
        this.deserializer = deserializer;
    }

    @Override
    public long append(final Command cmd) {
        if (!configuration.isPersistentEnabled()) {
            return 0;
        }

        return this.atomicRunner.run(
                this.buildAtomicKey(cmd.getKey()),
                () -> this.select(cmd.getKey()).append(cmd.getEntryType(), this.serializer.serialize(cmd))).getResult();
    }

    @Override
    public void fsync(final String key) {
        if (this.configuration.isPersistentEnabled()) {
            this.select(key).fsync();
        }
    }

    public Map<String, Iterator<Command>> readWALs() {
        log.info("Initial loading write ahead log base on configuration [{}]", configuration);
        this.initWALs();
        log.info("Initial Done, start to load commands from write ahead logs");

        return this.loadCommands();
    }

    private String buildAtomicKey(final String key) {
        return "WalManager:" + key;
    }

    @Atomic
    private void initWALs() {
        this.atomicRunner.run("Init:Wal", () -> {
            if (this.state == WALState.Loaded) {
                return null;
            }

            final Path storagePath = Path.of(this.configuration.getStoragePath());

            try {
                List<WalFile> walFiles = Files.list(storagePath).map(WalFile::new).collect(Collectors.toList());
                Map<String, List<WalFile>> walFileGroupedByKey = Lists2.group(walFiles, WalFile::getKey);

                walFileGroupedByKey.forEach((key, files) -> {
                    this.keyedWALs.put(key, WALs.init(key, storagePath, WalFiles.of(files)));
                });

                this.state = WALState.Loaded;
            } catch (IOException e) {
                throw new UnableToAccessStoragePath(e.getMessage());
            }

            return null;
        }).getOrThrow();
    }

    private Map<String, Iterator<Command>> loadCommands() {
        Map<String, Iterator<Command>> commands = new HashMap<>();

        keyedWALs.forEach((key, waLs) -> commands.put(key, waLs.toCommandIterator(this.deserializer)));

        return commands;
    }

    private WALs select(final String key) {
        return this.keyedWALs.computeIfAbsent(key, walKey -> new WALs(walKey, this.configuration.getStoragePath()));
    }

    private enum WALState {
        Unloaded, Loaded
    }
}
