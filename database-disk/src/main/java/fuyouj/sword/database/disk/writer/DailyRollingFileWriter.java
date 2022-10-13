package fuyouj.sword.database.disk.writer;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import com.fuyouj.sword.scabard.DateTimes2;

public class DailyRollingFileWriter extends BaseFileWriter {
    private LocalDateTime nextRollingTime;
    private String dateStr;

    public DailyRollingFileWriter(final String fileName) {
        this.fileName = fileName;
        LocalDateTime now = LocalDateTime.now();
        nextRollingTime = initNextRollingTime(now);
        this.dateStr = DateTimes2.format(now, "yyyyMMdd");
        setFile(getFormatFileName());
    }

    @Override
    public void append(final Object object) {
        LocalDateTime now = LocalDateTime.now();
        if (shouldRolling(now)) {
            doRolling(now);
        }

        super.append(object);
    }

    @Override
    protected String getFormatFileName() {
        return String.format("%s/%s.json", fileName, dateStr);
    }

    private void doRolling(final LocalDateTime now) {
        this.dateStr = DateTimes2.format(now, "yyyyMMdd");
        this.nextRollingTime = initNextRollingTime(now);

        setFile(getFormatFileName());
    }

    private LocalDateTime initNextRollingTime(final LocalDateTime now) {
        LocalDateTime today = DateTimes2.truncatedToBeginningOf(now, ChronoUnit.DAYS);
        return today.plusDays(1);
    }

    private boolean shouldRolling(final LocalDateTime now) {
        return now.isAfter(nextRollingTime);
    }
}
