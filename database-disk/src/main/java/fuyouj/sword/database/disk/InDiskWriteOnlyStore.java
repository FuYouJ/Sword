package fuyouj.sword.database.disk;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.fuyouj.sword.concurrent.runner.AtomicConsumer;
import com.fuyouj.sword.concurrent.runner.AtomicRunner;
import com.fuyouj.sword.scabard.command.Loggable;

import fuyouj.sword.database.WriteOnlyStore;
import fuyouj.sword.database.disk.cleaner.DateBeforeCleaner;
import fuyouj.sword.database.disk.cleaner.FileCleaner;
import fuyouj.sword.database.disk.writer.DailyRollingFileWriter;
import fuyouj.sword.database.disk.writer.FileWriter;

public class InDiskWriteOnlyStore implements WriteOnlyStore, Loggable, AtomicConsumer<String> {
    public String root;
    public ConcurrentHashMap<String, FileWriter> writers;

    public ConcurrentHashMap<String, FileCleaner> cleaners;

    public AtomicRunner<String> runner;

    public InDiskWriteOnlyStore(final String root, final AtomicRunner<String> runner) {
        this.root = root;
        this.writers = new ConcurrentHashMap<>();
        this.runner = runner;
        this.cleaners = new ConcurrentHashMap<>();
    }

    @Override
    public <T extends WriteOnlyObject> void append(final T item) {
        String name = item.getName();
        FileWriter writer = writers.computeIfAbsent(name, this::createWriter);

        this.atomicRun(name, () -> doAppend(item, writer));
    }

    public void append(final String name, final Map properties) {
        FileWriter writer = writers.computeIfAbsent(name, this::createWriter);

        this.atomicRun(name, () -> doAppend(properties, writer));
    }

    @Override
    public String buildKey(final String atomicKey) {
        return "Disk:" + atomicKey;
    }

    @Override
    public void clear(final String name, final Object id) {
        FileCleaner cleaner = cleaners.computeIfAbsent(name, this::createCleaner);
        cleaner.clear(getPath(name), id);
    }

    @Override
    public AtomicRunner<String> getRunner() {
        return this.runner;
    }

    private FileCleaner createCleaner(final String s) {
        return new DateBeforeCleaner();
    }

    private FileWriter createWriter(final String name) {
        return this.atomicRun(name, () -> new DailyRollingFileWriter(getPath(name)));
    }

    private boolean doAppend(final Object item, final FileWriter writer) {
        writer.append(item);
        return true;
    }

    private String getPath(final String name) {
        return String.format("%s/%s", root, name);
    }
}

