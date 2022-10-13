package com.fuyouj.sword.database.wal;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import com.fuyouj.sword.database.utils.PersistentFiles;
import com.fuyouj.sword.database.wal.exception.UnableToAccessWalFile;
import com.fuyouj.sword.database.wal.exception.WalFileNameInvalid;
import com.fuyouj.sword.scabard.Exceptions2;
import com.fuyouj.sword.scabard.Numbers2;
import com.fuyouj.sword.scabard.Strings;

import static com.fuyouj.sword.database.utils.PersistentFiles.validateFilePart;

class WalFile implements Comparable<WalFile> {
    private static final int PARTS = 3;

    private static final String MAGIC = "log";
    private static final int KEY_INDEX = 0;
    private static final int MAGIC_INDEX = 1;
    private static final int TS_INDEX = 2;

    private final File file;
    private final String filename;
    private final String absoluteName;
    private String[] parts;
    private boolean valid;

    WalFile(final Path file) {
        this.file = file.toFile();
        this.filename = file.toFile().getName();
        this.absoluteName = file.toAbsolutePath().toString();

        try {
            this.parts = this.validFilename();
            this.valid = true;
        } catch (WalFileNameInvalid ex) {
            this.parts = null;
            this.valid = false;
        }
    }

    @Override
    public int compareTo(final WalFile anotherWalFile) {
        if (Strings.equalIgnoreCase(this.getKey(), anotherWalFile.getKey())) {
            return Long.compare(getTs(), anotherWalFile.getTs());
        }

        return this.filename.compareTo(anotherWalFile.filename);
    }

    public String getAbsoluteName() {
        return this.absoluteName;
    }

    public File getFile() {
        return this.file;
    }

    public long getTs() {
        return Numbers2.tryParseLong(parts[TS_INDEX]).get();
    }

    public boolean isValid() {
        return this.valid;
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

    private boolean doValidate(final String[] split) {
        if (split == null) {
            return false;
        }

        return split.length == PARTS
                && validateFilePart(split, MAGIC_INDEX, false, this::isMagic)
                && validateFilePart(split, TS_INDEX, false, Numbers2::isNumber);
    }

    private boolean isMagic(final String magic) {
        return Strings.equalIgnoreCase(magic, MAGIC);
    }

    private String[] validFilename() {
        final String[] split = PersistentFiles.splitFileName(this.filename, ".", MAGIC, MAGIC_INDEX);

        if (!this.doValidate(split)) {
            throw new WalFileNameInvalid(this.filename);
        }

        return split;
    }
}
