package com.fuyouj.sword.scabard;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.util.Date;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class DateTimes2 {
    private static final long MS = 1000L;

    public static Temporal add(final Object leftObj, final Object rightObj) {
        if (leftObj instanceof Temporal) {
            return ((Temporal) leftObj).plus(Numbers2.toInt(rightObj), ChronoUnit.SECONDS);
        }

        if (leftObj instanceof Date) {
            return LocalDateTime.ofInstant(((Date) leftObj)
                    .toInstant().plusSeconds(Numbers2.toInt(rightObj)), ZoneId.systemDefault());
        }

        if (rightObj instanceof Temporal) {
            return ((Temporal) rightObj).plus(Numbers2.toInt(leftObj), ChronoUnit.SECONDS);
        }

        if (rightObj instanceof Date) {
            return LocalDateTime.ofInstant(((Date) rightObj)
                    .toInstant().plusSeconds(Numbers2.toInt(leftObj)), ZoneId.systemDefault());
        }

        return null;
    }

    public static ZonedDateTime asZoned(final LocalDateTime localDateTime) {
        Asserts.hasArg(localDateTime, "LocalDateTime must NOT be null");

        return localDateTime.atZone(ZoneId.systemDefault());
    }

    public static boolean compare(final Object target, final Object item) {
        if (item == null || target == null) {
            return false;
        }

        if (item instanceof LocalDateTime && target instanceof LocalDateTime) {
            return ((LocalDateTime) target).truncatedTo(ChronoUnit.MINUTES)
                    .isEqual(((LocalDateTime) item).truncatedTo(ChronoUnit.MINUTES));
        }

        if (item instanceof ZonedDateTime && target instanceof ZonedDateTime) {
            return ((ZonedDateTime) target).truncatedTo(ChronoUnit.MINUTES)
                    .isEqual(((ZonedDateTime) item).truncatedTo(ChronoUnit.MINUTES));
        }

        return false;
    }

    /**
     * 以一个低成本的方式，快速判断不可能是日期
     *
     * @param str
     * @return
     */
    public static boolean dateTimeImpossible(final String str) {
        return ChronoTimeHandler.dateTimeImpossible(str);
    }

    @Deprecated
    public static String defaultFormat(final Date date) {
        if (date == null) {
            return null;
        }

        if (date.getYear() < 0) {
            return String.format("%02d:%02d:%02d", date.getHours(), date.getMinutes(), date.getSeconds());
        }

        return format(date);
    }

    public static LocalDateTime endOfCurrent(final DateUnit unit) {
        return addUnit(truncatedToBeginningOf(LocalDateTime.now(), unit), unit).minusSeconds(1);
    }

    public static long epochMilli() {
        return System.currentTimeMillis();
    }

    public static long epochSecond() {
        return System.currentTimeMillis() / MS;
    }

    public static String format(final Date date) {
        if (date == null) {
            return null;
        }

        return ChronoTimeHandler.defaultFormat(date);
    }

    public static String format(final Instant instant) {
        if (instant == null) {
            return null;
        }

        return ChronoTimeHandler.defaultFormat(LocalDateTime.ofInstant(instant, ZoneId.systemDefault()));
    }

    public static String format(final long timestamp) {
        return ChronoTimeHandler.defaultFormat(toLocalOrNow(timestamp));
    }

    public static String format(final LocalDateTime dateTime, final String pattern) {
        return ChronoTimeHandler.format(dateTime, pattern);
    }

    public static String format(final LocalDateTime dateTime) {
        return ChronoTimeHandler.defaultFormat(dateTime);
    }

    public static String format(final ZonedDateTime dateTime) {
        if (dateTime == null) {
            return null;
        }

        return format(dateTime.toLocalDateTime());
    }

    public static String format(final Object obj) {
        if (obj == null) {
            return "";
        }

        if (obj instanceof LocalDateTime) {
            return format((LocalDateTime) obj);
        }

        if (obj instanceof ZonedDateTime) {
            return format((ZonedDateTime) obj);
        }

        if (obj instanceof Date) {
            return format((Date) obj);
        }

        if (obj instanceof Instant) {
            return format((Instant) obj);
        }

        if (obj instanceof Long) {
            return format((long) obj);
        }

        return "";
    }

    public static Optional<Instant> getInstant(final Object obj) {
        if (obj instanceof LocalDateTime) {
            return Optional.of(((LocalDateTime) obj).atZone(ZoneId.systemDefault()).toInstant());
        }

        if (obj instanceof ZonedDateTime) {
            return Optional.of(((ZonedDateTime) obj).toInstant());
        }

        if (obj instanceof Date) {
            return Optional.of(((Date) obj).toInstant());
        }

        return Optional.empty();
    }

    public static boolean isDateTime(final Object obj) {
        return obj instanceof Temporal || obj instanceof Date;
    }

    public static LocalDateTime localNow() {
        return LocalDateTime.now(ZoneId.systemDefault());
    }

    public static Long millisToSecond(final Long millis) {
        if (millis == null) {
            return null;
        }
        return millis / MS;
    }

    public static Double millisToSecondDouble(final Long millis) {
        if (millis == null) {
            return null;
        }
        Double millsDouble = Numbers2.toDouble(millis);
        return millsDouble / MS;
    }

    public static LocalDateTime minusUnit(final LocalDateTime dateTime, final int adjustAmount, final DateUnit unit) {
        return dateTime.minus(unit.getCount() * adjustAmount, unit.getUnit());
    }

    public static ZonedDateTime now() {
        return ZonedDateTime.now(ZoneId.systemDefault());
    }

    public static String nowToString() {
        return format(localNow());
    }

    public static Optional<ChronoTime> parse(final String str) {
        return ChronoTimeHandler.parse(str, null);
    }

    /**
     * parse time based on datetime text and it's pattern
     *
     * @param str     text of datetime
     * @param pattern pattern of this text
     * @return Optional of ChronoTime
     */
    public static Optional<ChronoTime> parse(final String str, final String pattern) {
        return ChronoTimeHandler.parse(str, pattern);
    }

    public static Optional<LocalDateTime> parseTime(final String str) {
        return ChronoTimeHandler.parse(str, null).map(ChronoTime::getDateTime);
    }

    public static Optional<LocalDateTime> parseTime(final String str, final String pattern) {
        return ChronoTimeHandler.parse(str, pattern).map(ChronoTime::getDateTime);
    }

    public static LocalDateTime plusUnit(final LocalDateTime dateTime, final int adjustAmount, final DateUnit unit) {
        return dateTime.plus(unit.getCount() * adjustAmount, unit.getUnit());
    }

    public static Long secondToMills(final Long second) {
        if (second == null) {
            return null;
        }
        return second * MS;
    }

    public static LocalDateTime startOfCurrent(final DateUnit unit) {
        return truncatedToBeginningOf(LocalDateTime.now(), unit);
    }

    public static Object sub(final Object leftObj, final Object rightObj) {
        if (!isDateTime(leftObj)) {
            return null;
        }

        return getInstant(leftObj)
                .map(leftInstant -> getInstant(rightObj)
                        .<Object>map(rightInstant -> Duration.between(rightInstant, leftInstant).getSeconds())
                        .orElseGet(() -> LocalDateTime.ofInstant(
                                leftInstant.minus(Numbers2.toInt(rightObj), ChronoUnit.SECONDS),
                                ZoneId.systemDefault())))
                .orElse(null);
    }

    public static <T> T timeSlide(final LocalDateTime start,
                                  final BiConsumer<ZonedDateTime, ZonedDateTime> function,
                                  final long days,
                                  final Function<LocalDateTime, T> onFinished) {
        LocalDateTime today = DateTimes2.startOfCurrent(DateUnit.Day);

        LocalDateTime dateTimeIndex = start.truncatedTo(ChronoUnit.MINUTES);
        LocalDateTime dateTimeNext;

        boolean cached = false;

        while (dateTimeIndex.isBefore(today)) {
            dateTimeNext = dateTimeIndex.plusDays(days).truncatedTo(ChronoUnit.DAYS);

            if (!dateTimeNext.isBefore(today)) {
                dateTimeNext = today;
            }

            LocalDateTime internalFrom = dateTimeIndex;
            LocalDateTime internalTo = dateTimeNext.minusSeconds(ChronoUnit.MINUTES.getDuration().getSeconds());

            function.accept(toZoned(internalFrom).orElse(null), toZoned(internalTo).orElse(null));

            dateTimeIndex = dateTimeNext;

            cached = true;
        }

        if (onFinished != null) {
            if (cached) {
                return onFinished.apply(dateTimeIndex.minusSeconds(ChronoUnit.MINUTES.getDuration().getSeconds()));
            } else {
                return onFinished.apply(null);
            }
        }

        return null;
    }

    public static LocalDateTime toLocal(final Instant instant) {
        return LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
    }

    public static LocalDateTime toLocal(final ZonedDateTime zonedDateTime) {
        return zonedDateTime.toLocalDateTime();
    }

    public static Optional<LocalDateTime> toLocal(final Object object, final String pattern) {
        if (object instanceof LocalDateTime) {
            return Optional.of((LocalDateTime) object);
        }

        if (object instanceof java.sql.Date) {
            Date date = new Date(((java.sql.Date) object).getTime());
            return Optional.of(toLocalOrNow((date.toInstant().toEpochMilli())));
        }

        if (object instanceof Date) {
            return Optional.of(toLocalOrNow(((Date) object).toInstant().toEpochMilli()));
        }

        if (object instanceof ZonedDateTime) {
            return Optional.of(toLocal((ZonedDateTime) object));
        }

        if (object instanceof String) {
            return parseTime((String) object, pattern);
        }

        return Optional.empty();
    }

    public static LocalDateTime toLocalOrNow(final Object object) {
        return toLocal(object, null).orElseGet(() -> localNow());
    }

    public static LocalDateTime toLocalOrNow(final Long timestamp) {
        if (timestamp == null) {
            return null;
        }

        return LocalDateTime.ofInstant(toInstant(timestamp), ZoneId.systemDefault());
    }

    public static long toMills(final LocalDateTime time) {
        return DateTimes2.asZoned(time).toInstant().toEpochMilli();
    }

    public static ZonedDateTime toZoned(final Long timestamp) {
        if (timestamp == null) {
            return null;
        }

        return ZonedDateTime.ofInstant(
                toInstant(timestamp),
                ZoneId.systemDefault()
        );
    }

    public static Optional<ZonedDateTime> toZoned(final LocalDateTime localDateTime) {
        if (localDateTime == null) {
            return Optional.empty();
        }
        return Optional.of(localDateTime.atZone(ZoneId.systemDefault()));
    }

    public static ZonedDateTime toZonedOrNow(final LocalDateTime dateTime) {
        return toZoned(dateTime).orElse(ZonedDateTime.now());
    }

    public static LocalDateTime truncatedToBeginningOf(final ZonedDateTime time, final ChronoUnit unit) {
        return truncatedToBeginningOf(toLocal(time), unit);
    }

    public static LocalDateTime truncatedToBeginningOf(final LocalDateTime time, final ChronoUnit unit) {
        if (unit == null) {
            return time;
        }

        if (unit.isDateBased()) {
            switch (unit) {
                case YEARS:
                    return time.truncatedTo(ChronoUnit.DAYS).withDayOfMonth(1).withMonth(1);
                case MONTHS:
                    return time.truncatedTo(ChronoUnit.DAYS).withDayOfMonth(1);
                case WEEKS:
                    return time.truncatedTo(ChronoUnit.DAYS).minusDays(time.getDayOfWeek().getValue() - 1);
                case DAYS:
                default:
                    return time.truncatedTo(ChronoUnit.DAYS);
            }
        }

        return time.truncatedTo(unit);
    }

    public static LocalDateTime truncatedToBeginningOf(final LocalDateTime dateTime, final DateUnit unit) {
        if (unit == null) {
            return truncatedToBeginningOf(dateTime, (ChronoUnit) null);
        }

        if (unit.getUnit() == ChronoUnit.MONTHS) {
            int startOfMonths = dateTime.getMonthValue() - (dateTime.getMonthValue() - 1) % unit.getCount();
            return dateTime.truncatedTo(ChronoUnit.DAYS).withMonth(startOfMonths).withDayOfMonth(1);
        }

        return truncatedToBeginningOf(dateTime, unit.getUnit());
    }

    public static LocalDateTime truncatedToEndOf(final LocalDateTime localDateTime, final DateUnit unit) {
        return addUnit(truncatedToBeginningOf(localDateTime, unit), unit).minusSeconds(1);
    }

    static LocalDateTime addUnit(final LocalDateTime dateTime, final DateUnit unit) {
        return dateTime.plus(unit.getCount(), unit.getUnit());
    }

    private static Instant toInstant(final Long timestamp) {
        return Instant.ofEpochMilli(Timestamp.of(timestamp).ms());
    }


}
