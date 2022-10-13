package com.fuyouj.sword.database.snapshot;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.time.Duration;

import com.fuyouj.sword.database.snapshot.exception.FailedToCreateSnapshotFile;
import com.fuyouj.sword.database.snapshot.exception.SnapshotFileAlreadyExists;
import com.fuyouj.sword.database.snapshot.exception.UnableToAccessSnapshotFile;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class BaseSnapshot<TData> {

    protected final String key;
    protected final File file;
    protected final long createAt;

    protected BaseSnapshot(final String key, final File file, final long createAt) {
        this.key = key;
        this.file = file;
        this.createAt = createAt;

    }

    public void clean() {
        this.deleteFile();
    }

    public void endRead() {

    }

    public void endWrite() {

    }

    protected static String buildSnapshotName(final String absoluteName, final long createAt) {
        return absoluteName + ".snapshot." + createAt;
    }

    protected static String buildSnapshotName(final String absoluteName, final long createAt, final int version) {
        return absoluteName + ".snapshot." + createAt + ".V" + version;
    }

    protected void closeInputStream(final InputStream in) {
        try {
            if (in != null) {
                in.close();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    protected void closeOutputStream(final OutputStream out) {
        try {
            if (out != null) {
                out.close();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    protected static String cost(final long start) {
        return Duration.ofMillis(System.currentTimeMillis() - start).toString();
    }

    protected static File createFile(final String path) {
        File file = new File(path);

        initialFile(file);

        return file;
    }

    protected static File createFile(final File parent, final String path) {
        File file = new File(parent, path);

        initialFile(file);

        return file;
    }

    protected static File createFolder(final String path) {
        File folder = new File(path);

        initialFolder(folder);

        return folder;
    }

    protected abstract InputStream createInputStream(File file) throws IOException;

    protected abstract OutputStream createOutStream(File file) throws IOException;

    protected void fsync(final OutputStream out) {
        try {
            if (out != null) {
                out.flush();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    protected TData handleException(final boolean ignoreError,
                                    final File file,
                                    final String message,
                                    final TData returnValue) {
        if (ignoreError) {
            log.error(message);
            return returnValue;
        } else {
            throw new UnableToAccessSnapshotFile(file, message);
        }
    }

    protected abstract TData readAndValidEntry(boolean ignoreError);

    protected abstract void write(String key, TData snapshotData);

    private static void deleteFile(final File file) {
        if (!file.exists()) {
            return;
        }

        if (file.isDirectory()) {
            for (File child : file.listFiles()) {
                deleteFile(child);
            }
        }

        file.delete();
    }

    private static void initialFile(final File file) {
        final String path = file.getPath();
        if (file.exists()) {
            throw new SnapshotFileAlreadyExists(path);
        }

        try {
            if (!file.createNewFile()) {
                throw new FailedToCreateSnapshotFile("failed to create " + path);
            }
        } catch (IOException e) {
            throw new FailedToCreateSnapshotFile("failed to create " + path + ", because of " + e.getMessage());
        }
    }

    private static void initialFolder(final File folder) {
        final String path = folder.getPath();
        if (folder.exists()) {
            throw new SnapshotFileAlreadyExists(path);
        }

        if (!folder.mkdirs()) {
            throw new FailedToCreateSnapshotFile("failed to create " + path);
        }
    }

    private void deleteFile() {
        deleteFile(this.file);
    }

}
