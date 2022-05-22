package com.fuyouj.sword.database.wal;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import com.thingworks.jarvis.concurrent.utils.Exceptions2;
import com.thingworks.jarvis.persistent.exception.WalFileNameInvalid;
import com.thingworks.jarvis.persistent.wal.exception.UnableToAccessWalFile;

import net.thingworks.jarvis.utils.type.Numbers2;
import net.thingworks.jarvis.utils.type.Strings;

class WalFile implements Comparable<WalFile> {
    private static final int PARTS = 3;
    private static final int KEY_INDEX = 0;
    private static final int LOG_INDEX = 1;
    private static final int ENTRY_INDEX = 2;
    private final File file;
    private final String filename;
    private final String[] parts;
    private final String absoluteName;

    WalFile(final Path file) {
        this.file = file.toFile();
        this.filename = file.toFile().getName();
        this.absoluteName = file.toAbsolutePath().toString();
        this.parts = this.validFilename();
    }

    @Override
    public int compareTo(final WalFile anotherWalFile) {
        if (Strings.equalIgnoreCase(this.getKey(), anotherWalFile.getKey())) {
            return Long.compare(getFirstEntryIndex(), anotherWalFile.getFirstEntryIndex());
        }

        return this.filename.compareTo(anotherWalFile.filename);
    }

    public String getAbsoluteName() {
        return this.absoluteName;
    }

    public File getFile() {
        return this.file;
    }

    public long getFirstEntryIndex() {
        return Numbers2.tryParseLong(parts[ENTRY_INDEX]).get();
    }

    String getKey() {
        return this.parts[KEY_INDEX];
    }

    boolean isFileEmpty() {
        try {
            return Files.size(this.file.toPath()) == 0;
        } catch (IOException e) {
            throw new UnableToAccessWalFile(this.file, Exceptions2.extractMessage(e));
        }
    }

    boolean isSameKey(final String key) {
        return false;
    }

    private String[] validFilename() {
        String[] split = Strings.split(this.filename, ".");

        if (split == null
                || split.length != PARTS
                || !Strings.equalIgnoreCase(split[LOG_INDEX], "log")
                || !Numbers2.isNumber(split[ENTRY_INDEX])) {
            throw new WalFileNameInvalid(this.filename);
        }

        return split;
    }
}
