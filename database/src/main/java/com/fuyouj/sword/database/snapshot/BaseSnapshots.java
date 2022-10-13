package com.fuyouj.sword.database.snapshot;

import java.nio.file.Path;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

public abstract class BaseSnapshots<TData, TSnapshot extends BaseSnapshot<TData>> {
    protected final Path path;
    protected final LinkedList<BaseSnapshot<TData>> snapshotList;
    protected final String key;
    protected BaseSnapshot<TData> activeSnapshot = null;

    protected BaseSnapshots(final String key, final String path) {
        this.key = key;
        this.path = Path.of(path);
        this.snapshotList = new LinkedList<>();
    }

    protected BaseSnapshots(final String key, final Path path, final List<BaseSnapshot<TData>> snapshotList) {
        this.key = key;
        this.path = path;
        this.snapshotList = new LinkedList<>(snapshotList);
    }

    public void clean() {
        for (BaseSnapshot<TData> snapshot : snapshotList) {
            snapshot.clean();
        }
    }

    public void endRead() {
        if (this.activeSnapshot != null) {
            this.activeSnapshot.endRead();
        }
    }

    public void endWrite() {
        Iterator<BaseSnapshot<TData>> iterator = snapshotList.iterator();
        while (iterator.hasNext()) {
            BaseSnapshot<TData> snapshot = iterator.next();

            if (snapshot == this.activeSnapshot) {
                snapshot.endWrite();
                break;
            }

            snapshot.clean();
            iterator.remove();
        }
    }

    public void invalid() {
        if (this.activeSnapshot == null) {
            return;
        }

        this.activeSnapshot.clean();
        this.snapshotList.remove(this.activeSnapshot);
        this.activeSnapshot = null;
    }

    public TData readAndSelectActive(final boolean ignoreError) {
        TData data = null;

        //从最新的开始读取，返回第一个合法的快照数据
        ListIterator<BaseSnapshot<TData>> iterator = this.snapshotList.listIterator(this.snapshotList.size());
        while (iterator.hasPrevious()) {
            BaseSnapshot<TData> snapshot = iterator.previous();

            //读取快照数据
            data = snapshot.readAndValidEntry(ignoreError);
            if (data == null) {
                //不合法，删除
                snapshot.clean();
                iterator.remove();

                continue;
            }

            //记录合法快照
            this.activeSnapshot = snapshot;
            break;
        }

        //删除老的快照
        while (iterator.hasPrevious()) {
            BaseSnapshot<TData> snapshot = iterator.previous();

            snapshot.clean();
            iterator.remove();
        }

        return data;
    }

    public void startWrite() {
        this.newSnapshot();
    }

    public void write(final String key, final TData data) {
        this.activeSnapshot.write(key, data);
    }

    protected String buildAbsoluteFilename(final String name) {
        return this.path.resolve(name).toString();
    }

    protected abstract TSnapshot createSnapshot();

    private void newSnapshot() {
        final BaseSnapshot<TData> snapshot = this.createSnapshot();

        this.activeSnapshot = snapshot;
        this.snapshotList.add(snapshot);
    }
}
