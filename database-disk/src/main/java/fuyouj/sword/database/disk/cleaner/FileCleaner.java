package fuyouj.sword.database.disk.cleaner;

import java.io.File;

public interface FileCleaner {
    default void clear(String fileName) {
        File file = new File(fileName);
        file.delete();
    }

    void clear(String path, Object id);
}
