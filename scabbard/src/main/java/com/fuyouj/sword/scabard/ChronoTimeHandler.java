package com.fuyouj.sword.scabard;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Year;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.fuyouj.sword.scabard.funtion.Invoke;

import lombok.Getter;

import static java.time.format.DateTimeFormatter.ofPattern;

public class ChronoTimeHandler {

    private static final Map<String, ChronoTimeHandler> NAMED_PARSERS = ChronoTimeHandlers.NAMED_PARSERS;
    private static final Map<Integer, List<ChronoTimeHandler>> SIZED_PARSERS = ChronoTimeHandlers.SIZED_PARSERS;

    private static final DateTimeFormatter DEFAULT_FORMAT = ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final DateTimeFormatter[] ISO_FORMATTERS = {
            DateTimeFormatter.ISO_LOCAL_DATE_TIME,
            DateTimeFormatter.ISO_ZONED_DATE_TIME,
    };
    private static final ThreadLocal<DateFormat> SIMPLE_DATE_FORMAT_THREAD_SELF = ThreadLocal.withInitial(
            () -> new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    );
    @Getter
    private final ChronoUnit unit;
    @Getter
    private final String pattern;
    private final boolean zonedTime;
    private final Formatter formatter;
    private final boolean candidate;
    private final DataPatternMatcher dataPatternMatcher;
    private final Parser parser;

    public ChronoTimeHandler(final ChronoUnit unit, final String pattern) {
        this(unit, pattern, ofPattern(pattern), true);
    }

    public ChronoTimeHandler(final ChronoUnit unit, final String pattern, final boolean candidate) {
        this(unit, pattern, ofPattern(pattern), candidate);
    }

    ChronoTimeHandler(final ChronoUnit unit, final String pattern, final DateTimeFormatter formatter) {
        this(unit, pattern, formatter, true);
    }

    ChronoTimeHandler(final ChronoUnit unit, final String pattern, final DateTimeFormatter formatter, final boolean candidate) {
        this.unit = unit;
        this.pattern = pattern;
        this.zonedTime = this.pattern.contains("Z");
        this.formatter = datetime -> datetime.format(formatter);
        this.candidate = candidate;
        this.dataPatternMatcher = new DataPatternMatcher();
        this.parser = dateString -> {
            try {
                if (this.unit.isDateBased()) {
                    if (this.unit == ChronoUnit.YEARS) {
                        return Optional.of(Year.parse(dateString, formatter).atMonth(1).atDay(1).atStartOfDay());
                    }

                    if (this.unit == ChronoUnit.MONTHS) {
                        return Optional.of(YearMonth.parse(dateString, formatter).atDay(1).atStartOfDay());
                    }

                    if (zonedTime) {
                        return Optional.of(
                                ZonedDateTime.parse(dateString, formatter)
                                        .withZoneSameInstant(ZoneId.systemDefault())
                                        .toLocalDateTime()
                                        .toLocalDate()
                                        .atStartOfDay()
                        );
                    } else {
                        return Optional.of(LocalDate.parse(dateString, formatter).atStartOfDay());
                    }
                }

                if (zonedTime) {
                    return Optional.of(
                            ZonedDateTime.parse(dateString, formatter)
                                    .withZoneSameInstant(ZoneId.systemDefault())
                                    .toLocalDateTime()
                    );
                } else {
                    return Optional.of(LocalDateTime.parse(dateString, formatter));
                }
            } catch (Exception e) {
                return Optional.empty();
            }
        };
    }

    ChronoTimeHandler(final ChronoUnit unit,
                      final String pattern,
                      final Formatter formatter,
                      final Parser parser,
                      final boolean candidate) {
        this.unit = unit;
        this.pattern = pattern;
        this.zonedTime = this.pattern.contains("Z");
        this.formatter = formatter;
        this.dataPatternMatcher = new DataPatternMatcher();
        this.parser = parser;
        this.candidate = candidate;
    }

    /**
     * 以一个低成本的方式，快速判断不可能是日期
     *
     * @param str
     * @return
     */
    public static boolean dateTimeImpossible(final String str) {
        if (Strings.isNullOrEmpty(str)) {
            return true;
        }

        String trim = str.trim();

        if (!Character.isDigit(trim.charAt(0))) {
            return true;
        }

        for (int i = 1; i < trim.length(); i++) {
            char c = trim.charAt(i);

            if (Character.isDigit(c)
                    || c == '-' || c == '/' || c == ':' || c == ' ' || c == 'T' || c == '.' || c == 'Z' || c == '+') {
                continue;
            }

            return true;
        }

        return false;
    }

    public static String defaultFormat(final Date date) {
        if (date == null) {
            return null;
        }
        return getDefaultDateFormat().format(date);
    }

    public static String defaultFormat(final LocalDateTime dateTime) {
        if (dateTime == null) {
            return null;
        }

        return dateTime.atZone(ZoneId.systemDefault()).format(DEFAULT_FORMAT);
    }

    public static String format(final Date date) {
        return SIMPLE_DATE_FORMAT_THREAD_SELF.get().format(date);
    }


    public static String format(final LocalDateTime time, final String pattern) {
        if (time == null) {
            return "";
        }

        if (Strings.isBlank(pattern)) {
            return time.format(DEFAULT_FORMAT);
        }

        if (Strings.isNotBlank(pattern) && NAMED_PARSERS.containsKey(pattern.toLowerCase())) {
            return NAMED_PARSERS.get(pattern.toLowerCase()).format(time);
        }

        try {
            return time.format(ofPattern(pattern));
        } catch (Exception e) {
            return "";
        }
    }

    @MaybeNull
    public static String format(final LocalDateTime localDateTime, final ChronoUnit unit) {
        return format(localDateTime, unit, false);
    }

    @MaybeNull
    public static String format(final LocalDateTime localDateTime, final ChronoUnit unit, final boolean zonedTime) {
        if (localDateTime == null) {
            return null;
        }

        Collection<ChronoTimeHandler> values = NAMED_PARSERS.values();

        for (ChronoTimeHandler handler : values) {
            if (handler.unit != unit) {
                continue;
            }

            if (!zonedTime && handler.zonedTime) {
                continue;
            }

            try {
                return handler.format(localDateTime);
            } catch (Exception ignored) {

            }
        }

        return defaultFormat(localDateTime);

    }

    public static DateFormat getDefaultDateFormat() {
        return SIMPLE_DATE_FORMAT_THREAD_SELF.get();
    }

    public static Optional<ChronoTime> parse(final String str, final String pattern) {
        if (Strings.isNotBlank(pattern)) {
            if (NAMED_PARSERS.containsKey(pattern.toLowerCase())) {
                return NAMED_PARSERS.get(pattern.toLowerCase()).doParse(str, true);
            }

            try {
                DateTimeFormatter specificPattern = ofPattern(pattern);
                try {
                    return Optional.of(new ChronoTime(null, LocalDateTime.parse(str, specificPattern)));
                } catch (Exception ex) {
                    return Optional.of(new ChronoTime(null, LocalDate.parse(str, specificPattern).atStartOfDay()));
                }

            } catch (Exception e) {
                return Optional.empty();
            }
        }

        return parse(str);
    }

    public static Optional<ChronoTime> parse(final String str) {
        if (Strings.isBlank(str)) {
            return Optional.empty();
        }

        String trimmedString = str;

        if (Strings.needToTrim(str)) {
            trimmedString = str.trim();
        }

        List<ChronoTimeHandler> handlers = SIZED_PARSERS.get(trimmedString.length());

        Optional<ChronoTime> optionalParsedTime = Optional.empty();

        if (handlers != null) {
            String strPattern = getStrPattern(str);

            for (ChronoTimeHandler handler : handlers) {
                if (!handler.candidate) {
                    continue;
                }

                if (!handler.dataPatternMatcher.match(strPattern)) {
                    continue;
                }

                optionalParsedTime = handler.doParse(str, false);

                if (optionalParsedTime.isPresent()) {
                    break;
                }
            }
        }

        if (optionalParsedTime.isPresent()) {
            return optionalParsedTime;
        }

        for (DateTimeFormatter isoFormatter : ISO_FORMATTERS) {
            ZonedDateTime zonedDateTime = Invoke.start(() -> ZonedDateTime.parse(str, isoFormatter)).call();

            if (zonedDateTime != null) {
                return Optional.of(new ChronoTime(
                        ChronoUnit.MILLIS,
                        zonedDateTime.withZoneSameInstant(ZoneId.systemDefault()).toLocalDateTime()
                ));
            }
        }

        return Optional.empty();
    }

    public String format(final LocalDateTime dateTime) {
        return formatter.format(dateTime);
    }

    private static String getStrPattern(final String str) {
        char[] chars = new char[str.length()];

        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);

            if (Character.isDigit(c)) {
                chars[i] = '0';
            } else {
                chars[i] = c;
            }
        }

        return new String(chars);
    }

    private Optional<ChronoTime> doParse(final String dateTimeString, final boolean alignWithPattern) {
        if (Strings.isBlank(dateTimeString)) {
            return Optional.empty();
        }

        String processedString = dateTimeString;

        if (alignWithPattern && processedString.length() > getPattern().length()) {
            processedString = processedString.substring(0, getPattern().length());
        }

        return this.parser.tryParse(processedString).map(dateTime -> new ChronoTime(unit, dateTime));
    }

    private class DataPatternMatcher {
        private static final int ZONED_LENGTH_DELTA = 4;

        private final String dataPattern;

        DataPatternMatcher() {
            String pattern = ChronoTimeHandler.this.pattern;

            int length = pattern.length() - Strings.containsTimes(pattern, '\'');
            int patternEnd = pattern.length();
            boolean hasZone = false;
            boolean hasBB = false;

            if (pattern.endsWith("ZZ")) {
                //+07:00
                length += ZONED_LENGTH_DELTA;
                patternEnd -= "ZZ".length();
                hasZone = true;
            } else if (pattern.endsWith("BB")) {
                patternEnd -= "BB".length();
                hasBB = true;
            }

            char[] chars = new char[length];

            int j = 0;
            for (int i = 0; i < patternEnd; i++) {
                char c = pattern.charAt(i);

                if (c == '\'') {
                    continue;
                }

                if (c == 'Y' || c == 'y'
                        || c == 'M' || c == 'm'
                        || c == 'D' || c == 'd'
                        || c == 'H' || c == 'h'
                        || c == 'S' || c == 's'
                        || c == 'W' || c == 'w') {
                    chars[j] = '0';
                } else {
                    chars[j] = c;
                }

                j++;
            }

            if (hasZone) {
                //+07:00
                chars[j++] = '+';
                chars[j++] = '0';
                chars[j++] = '0';
                chars[j++] = ':';
                chars[j++] = '0';
                chars[j] = '0';
            } else if (hasBB) {
                chars[j++] = 'H';
                chars[j] = '0';
            }

            this.dataPattern = new String(chars);
        }

        public boolean match(final String dateStrPattern) {
            return this.dataPattern.equals(dateStrPattern);
        }
    }
}