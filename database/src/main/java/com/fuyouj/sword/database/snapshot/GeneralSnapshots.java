package com.fuyouj.sword.database.snapshot;

import java.nio.file.Path;
import java.util.List;

import com.fuyouj.sword.scabard.Lists2;
import com.fuyouj.sword.scabard.funtion.Invoke;

public class GeneralSnapshots extends BaseSnapshots<byte[], GeneralSnapshot> {

    public GeneralSnapshots(final String key, final String path) {
        super(key, path);
    }

    public GeneralSnapshots(final String key, final Path path, final List<BaseSnapshot<byte[]>> snapshotList) {
        super(key, path, snapshotList);
    }

    public static GeneralSnapshots init(final String key, final Path storagePath, final SnapshotFiles snapshotFiles) {
        List<BaseSnapshot<byte[]>> snapshots = Lists2.mapNotNull(
                snapshotFiles.getSnapshotFiles(),
                file -> Invoke.start(() -> GeneralSnapshot.init(file)).call()
        );

        return new GeneralSnapshots(key, storagePath, snapshots);
    }

    @Override
    protected GeneralSnapshot createSnapshot() {
        return new GeneralSnapshot(this.key, buildAbsoluteFilename(this.key), System.currentTimeMillis());
    }
}
