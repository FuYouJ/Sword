package com.fuyouj.sword.database.wal;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.fuyouj.sword.database.Deserializer;
import com.fuyouj.sword.database.utils.Iterators;
import com.fuyouj.sword.scabard.Lists2;

class WALs {
    private static final long VERY_FIRST_ENTRY_INDEX = 1L;
    private final Path walPath;
    private final List<WAL> walList;
    private final String key;
    private WAL activeWal = null;

    WALs(final String key, final String walPath) {
        this.key = key;
        this.walPath = Path.of(walPath);
        this.walList = new ArrayList<>();
    }

    private WALs(final String key, final Path walPath, final List<WAL> walList) {
        this.key = key;
        this.walPath = walPath;
        this.walList = walList;
    }

    long append(final EntryType entryType, final byte[] bytes) {
        this.ensureActiveWal();
        return this.activeWal.append(entryType, bytes);
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

    Iterator<Command> toCommandIterator(final Deserializer deserializer) {
        if (this.walList.isEmpty()) {
            return Iterators.empty();
        }

        return new Iterator<Command>() {
            private int walCursor = 0;
            private Iterator<Command> currentIterator = walList.get(0).toCommandIterator(deserializer);

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
                    currentIterator = walList.get(walCursor).toCommandIterator(deserializer);
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
                this.newWal(this.key, VERY_FIRST_ENTRY_INDEX);
            } else {
                Lists2.last(this.walList).map(w -> this.activeWal = w);
            }
        }
    }

    private void newWal(final String name, final long firstEntryIndex) {
        final WAL wal = new WAL(buildAbsoluteFilename(name), firstEntryIndex);
        this.activeWal = wal;
        this.walList.add(wal);
    }
}

