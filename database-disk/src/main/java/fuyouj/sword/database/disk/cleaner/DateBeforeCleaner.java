package fuyouj.sword.database.disk.cleaner;

import java.io.File;
import java.time.LocalDateTime;
import java.util.Optional;

import com.fuyouj.sword.scabard.DateTimes2;

public class DateBeforeCleaner implements FileCleaner {
    @Override
    public void clear(final String path, final Object id) {
        Optional<LocalDateTime> dateTime = DateTimes2.toLocal(id, "yyyyMMdd");
        if (dateTime.isEmpty()) {
            return;
        }

        File folder = new File(path);
        if (!folder.exists() || folder.isFile()) {
            return;
        }

        File[] files = folder.listFiles();
        if (files == null || files.length == 0) {
            return;
        }

        for (File file : files) {
            if (file.isDirectory()) {
                continue;
            }

            String fileName = file.getName().split("\\.")[0];
            Optional<LocalDateTime> fileDateTime = DateTimes2.toLocal(fileName, "yyyyMMdd");
            if (fileDateTime.isEmpty()) {
                continue;
            }

            if (fileDateTime.get().isBefore(dateTime.get())) {
                file.delete();
            }
        }
    }
}

