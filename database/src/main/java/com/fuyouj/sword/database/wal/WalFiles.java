package com.fuyouj.sword.database.wal;

import java.util.List;

import com.fuyouj.sword.scabard.Lists2;
import com.fuyouj.sword.scabard.funtion.Invoke;

public class WalFiles {
    private final List<WalFile> walFiles;

    private WalFiles(final List<WalFile> walFiles) {
        this.walFiles = walFiles;
        this.walFiles.sort(WalFile::compareTo);
    }

    public static WalFiles of(final List<WalFile> walFiles) {
        return new WalFiles(walFiles);
    }

    public List<WalFile> getWalFiles() {
        return walFiles;
    }

    public List<WAL> init() {
        return Lists2.mapNotNull(this.walFiles, walFile -> Invoke.start(() -> WAL.init(walFile)).call());
    }
}
