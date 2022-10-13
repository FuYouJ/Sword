package com.fuyouj.sword.database.snapshot;

import java.io.File;
import java.nio.file.Path;

import com.fuyouj.sword.database.utils.PersistentFiles;
import com.fuyouj.sword.scabard.Numbers2;
import com.fuyouj.sword.scabard.Strings;

import static com.fuyouj.sword.database.utils.PersistentFiles.validateFilePart;

public class SnapshotFile implements Comparable<SnapshotFile> {

    private static final String MAGIC = "snapshot";

    private static final int PARTS_WITH_VERSION = 4;
    private static final int KEY_INDEX = 0;
    private static final int MAGIC_INDEX = 1;
    private static final int TS_INDEX = 2;

    private static final int VERSION_INDEX = 3;
    private final File file;
    private final String filename;
    private final String absoluteName;
    private String[] parts;
    private boolean valid;

    public SnapshotFile(final Path file) {
        this.file = file.toFile();
        this.filename = file.toFile().getName();
        this.absoluteName = file.toAbsolutePath().toString();
        try {
            this.parts = this.validFilename();
            this.valid = true;
        } catch (SnapshotFileNameInvalid ex) {
            this.parts = null;
            this.valid = false;
        }
    }

    @Override
    public int compareTo(final SnapshotFile another) {
        if (Strings.equalIgnoreCase(this.getKey(), another.getKey())) {
            return Long.compare(getTs(), another.getTs());
        }

        return this.filename.compareTo(another.filename);
    }

    public String getAbsoluteName() {
        return this.absoluteName;
    }

    public File getFile() {
        return this.file;
    }

    public String getKey() {
        return this.parts[KEY_INDEX];
    }

    public long getTs() {
        return Numbers2.tryParseLong(parts[TS_INDEX]).get();
    }

    public int getVersion() {
        if (this.parts.length == PARTS_WITH_VERSION) {
            return Numbers2.tryParseInt(parts[VERSION_INDEX].substring(1)).get();
        } else {
            return 1;
        }
    }

    public boolean isValid() {
        return this.valid;
    }

    private static boolean isVersion(final String version) {
        return (version.startsWith("V") || version.startsWith("v")) && Numbers2.isNumber(version.substring(1));
    }

    private boolean doValidate(final String[] split) {
        if (split == null) {
            return false;
        }

        return validateFilePart(split, MAGIC_INDEX, false, this::isMagic)
                && validateFilePart(split, TS_INDEX, false, Numbers2::isNumber)
                && validateFilePart(split, VERSION_INDEX, true, SnapshotFile::isVersion);
    }

    private boolean isMagic(final String magic) {
        return Strings.equalIgnoreCase(magic, MAGIC);
    }

    private String[] validFilename() {
        String[] split = PersistentFiles.splitFileName(this.filename, ".", MAGIC, MAGIC_INDEX);

        if (!this.doValidate(split)) {
            throw new SnapshotFileNameInvalid(this.filename);
        }

        return split;
    }
}
