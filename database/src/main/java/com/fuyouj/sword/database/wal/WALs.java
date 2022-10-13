package com.fuyouj.sword.database.wal;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import com.fuyouj.sword.database.Deserializer;
import com.fuyouj.sword.database.PersistentConfiguration;
import com.fuyouj.sword.database.utils.Iterators;
import com.fuyouj.sword.scabard.Lists2;

class WALs {
    private final Path walPath;
    private final List<WAL> walList;
    private final String key;
    private WAL activeWal = null;

    WALs(final String key, final String walPath) {
        this.key = key;
        this.walPath = Path.of(walPath);
        this.walList = new CopyOnWriteArrayList<>();
    }

    private WALs(final String key, final Path walPath, final List<WAL> walList) {
        this.key = key;
        this.walPath = walPath;
        this.walList = new CopyOnWriteArrayList<>(walList);
    }

    public void clean() {
        if (Lists2.isNullOrEmpty(walList)) {
            return;
        }

        for (WAL wal : walList) {
            wal.clean();
        }

        this.activeWal = null;
        this.walList.clear();
    }

    public long size() {
        long size = 0;
        for (WAL wal : walList) {
            size += wal.size();
        }

        return size;
    }

    long append(final EntryType entryType, final byte[] bytes) {
        this.ensureActiveWal();
        return this.activeWal.append(entryType, bytes);
    }

    long count() {
        if (Lists2.isNullOrEmpty(walList)) {
            return 0L;
        }

        return walList.stream().mapToLong(WAL::count).sum();
    }

    void fsync() {
        this.activeWal.fsync();
    }

    List<WALEntry> getWalEntries() {
        return null;
    }

    /**
     * 加载指定的WAL日志文件
     *
     * @param key         WAL的唯一key
     * @param storagePath 存储地址
     * @param walFiles    该Key的所有现有的Wal日志文件Reference
     * @return WALs
     */
    static WALs init(final String key, final Path storagePath, final WalFiles walFiles) {
        return new WALs(key, storagePath, walFiles.init());
    }

    Iterator<Command> toCommandIterator(final Deserializer deserializer, final PersistentConfiguration configuration) {
        if (this.walList.isEmpty()) {
            return Iterators.empty();
        }

        return new Iterator<>() {
            private int walCursor = 0;
            private Iterator<Command> currentIterator = walList.get(0).toCommandIterator(deserializer, configuration);

            @Override
            public boolean hasNext() {
                if (currentIterator == null) {
                    return false;
                }

                if (currentIterator.hasNext()) {
                    return true;
                }

                this.walCursor++;
                this.currentIterator = null;

                if (this.walCursor >= walList.size()) {
                    return false;
                } else {
                    currentIterator = walList.get(walCursor).toCommandIterator(deserializer, configuration);
                }

                return hasNext();
            }

            @Override
            public Command next() {
                if (currentIterator == null) {
                    return null;
                }

                return currentIterator.next();
            }
        };
    }

    private String buildAbsoluteFilename(final String name) {
        return this.walPath.resolve(name).toString();
    }

    private void ensureActiveWal() {
        if (this.activeWal == null) {
            if (this.walList.isEmpty()) {
                this.newWal(this.key);
            } else {
                Lists2.last(this.walList).map(w -> this.activeWal = w);
            }
        }

        if (this.activeWal.isInvalid()) {
            this.newWal(this.key);
        }
    }

    private void newWal(final String name) {
        final WAL wal = new WAL(buildAbsoluteFilename(name), System.currentTimeMillis());
        this.activeWal = wal;
        this.walList.add(wal);
    }
}

