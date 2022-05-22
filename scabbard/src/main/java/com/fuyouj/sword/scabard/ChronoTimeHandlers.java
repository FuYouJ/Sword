package com.fuyouj.sword.scabard;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoUnit;
import java.time.temporal.IsoFields;
import java.time.temporal.WeekFields;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class ChronoTimeHandlers {
    static final Map<String, ChronoTimeHandler> NAMED_PARSERS = new LinkedHashMap<>();
    static final Map<Integer, List<ChronoTimeHandler>> SIZED_PARSERS = new HashMap<>();
    private static final int ZONED_LENGTH_DELTA = 4;
    private static final Pattern H_1 = Pattern.compile("(\\d{4})-H1");
    private static final Pattern H_2 = Pattern.compile("(\\d{4})-H2");

    static {
        initNamedParsers();
        initSizedParsers();
    }

    private static void initDaysParsers() {
        NAMED_PARSERS.put("yyyy-m-d", new ChronoTimeHandler(ChronoUnit.DAYS, "yyyy-M-d"));
        NAMED_PARSERS.put("yyyy-m-dd", new ChronoTimeHandler(ChronoUnit.DAYS, "yyyy-M-dd"));
        NAMED_PARSERS.put("yyyy-mm-d", new ChronoTimeHandler(ChronoUnit.DAYS, "yyyy-MM-d"));
        NAMED_PARSERS.put("yyyy/mm/dd", new ChronoTimeHandler(ChronoUnit.DAYS, "yyyy/MM/dd"));
        NAMED_PARSERS.put("yyyy/mm/d", new ChronoTimeHandler(ChronoUnit.DAYS, "yyyy/MM/d"));
        NAMED_PARSERS.put("yyyy/m/d", new ChronoTimeHandler(ChronoUnit.DAYS, "yyyy/M/d"));
        NAMED_PARSERS.put("yyyy/m/dd", new ChronoTimeHandler(ChronoUnit.DAYS, "yyyy/M/dd"));
        NAMED_PARSERS.put("dd/mm/yyyy", new ChronoTimeHandler(ChronoUnit.DAYS, "dd/MM/yyyy"));
        NAMED_PARSERS.put("d/m/yyyy", new ChronoTimeHandler(ChronoUnit.DAYS, "d/M/yyyy"));
        NAMED_PARSERS.put("dd/m/yyyy", new ChronoTimeHandler(ChronoUnit.DAYS, "dd/M/yyyy"));
        NAMED_PARSERS.put("d/mm/yyyy", new ChronoTimeHandler(ChronoUnit.DAYS, "d/MM/yyyy"));
    }

    private static void initHoursParsers() {
        NAMED_PARSERS.put("yyyy-mm-dd h", new ChronoTimeHandler(ChronoUnit.HOURS, "yyyy-MM-dd H"));
        //
        NAMED_PARSERS.put("yyyy-m-d hh", new ChronoTimeHandler(ChronoUnit.HOURS, "yyyy-M-d HH"));
        NAMED_PARSERS.put("yyyy-m-d h", new ChronoTimeHandler(ChronoUnit.HOURS, "yyyy-M-d H"));
        //
        NAMED_PARSERS.put("yyyy-m-dd hh", new ChronoTimeHandler(ChronoUnit.HOURS, "yyyy-M-dd HH"));
        NAMED_PARSERS.put("yyyy-m-dd h", new ChronoTimeHandler(ChronoUnit.HOURS, "yyyy-M-dd H"));
        //
        NAMED_PARSERS.put("yyyy-mm-d hh", new ChronoTimeHandler(ChronoUnit.HOURS, "yyyy-MM-d HH"));
        NAMED_PARSERS.put("yyyy-mm-d h", new ChronoTimeHandler(ChronoUnit.HOURS, "yyyy-MM-d H"));
        //
        NAMED_PARSERS.put("yyyy/mm/dd hh", new ChronoTimeHandler(ChronoUnit.HOURS, "yyyy/MM/dd HH"));
        NAMED_PARSERS.put("yyyy/mm/dd h", new ChronoTimeHandler(ChronoUnit.HOURS, "yyyy/MM/dd H"));
        //
        NAMED_PARSERS.put("yyyy/mm/d hh", new ChronoTimeHandler(ChronoUnit.HOURS, "yyyy/MM/d HH"));
        NAMED_PARSERS.put("yyyy/mm/d h", new ChronoTimeHandler(ChronoUnit.HOURS, "yyyy/MM/d H"));
        //
        NAMED_PARSERS.put("yyyy/m/d hh", new ChronoTimeHandler(ChronoUnit.HOURS, "yyyy/M/d HH"));
        NAMED_PARSERS.put("yyyy/m/d h", new ChronoTimeHandler(ChronoUnit.HOURS, "yyyy/M/d H"));
        //
        NAMED_PARSERS.put("yyyy/m/dd hh", new ChronoTimeHandler(ChronoUnit.HOURS, "yyyy/M/dd HH"));
        NAMED_PARSERS.put("yyyy/m/dd h", new ChronoTimeHandler(ChronoUnit.HOURS, "yyyy/M/dd H"));
        //
        NAMED_PARSERS.put("dd/mm/yyyy hh", new ChronoTimeHandler(ChronoUnit.HOURS, "dd/MM/yyyy HH"));
        NAMED_PARSERS.put("dd/mm/yyyy h", new ChronoTimeHandler(ChronoUnit.HOURS, "dd/MM/yyyy H"));
        //
        NAMED_PARSERS.put("d/m/yyyy hh", new ChronoTimeHandler(ChronoUnit.HOURS, "d/M/yyyy HH"));
        NAMED_PARSERS.put("d/m/yyyy h", new ChronoTimeHandler(ChronoUnit.HOURS, "d/M/yyyy H"));
        //
        NAMED_PARSERS.put("dd/m/yyyy hh", new ChronoTimeHandler(ChronoUnit.HOURS, "dd/M/yyyy HH"));
        NAMED_PARSERS.put("dd/m/yyyy h", new ChronoTimeHandler(ChronoUnit.HOURS, "dd/M/yyyy H"));
        //
        NAMED_PARSERS.put("d/mm/yyyy hh", new ChronoTimeHandler(ChronoUnit.HOURS, "d/MM/yyyy HH"));
        NAMED_PARSERS.put("d/mm/yyyy h", new ChronoTimeHandler(ChronoUnit.HOURS, "d/MM/yyyy H"));
    }

    private static void initMinutesParsers() {
        NAMED_PARSERS.put("yyyy-mm-dd h:m", new ChronoTimeHandler(ChronoUnit.MINUTES, "yyyy-MM-dd H:m"));
        NAMED_PARSERS.put("yyyy-mm-dd h:mm", new ChronoTimeHandler(ChronoUnit.MINUTES, "yyyy-MM-dd H:mm"));
        NAMED_PARSERS.put("yyyy-mm-dd hh:m", new ChronoTimeHandler(ChronoUnit.MINUTES, "yyyy-MM-dd HH:m"));
        //
        NAMED_PARSERS.put("yyyy-m-d h:m", new ChronoTimeHandler(ChronoUnit.MINUTES, "yyyy-M-d H:m"));
        NAMED_PARSERS.put("yyyy-m-d h:mm", new ChronoTimeHandler(ChronoUnit.MINUTES, "yyyy-M-d H:mm"));
        NAMED_PARSERS.put("yyyy-m-d hh:m", new ChronoTimeHandler(ChronoUnit.MINUTES, "yyyy-M-d HH:m"));
        NAMED_PARSERS.put("yyyy-m-d hh:mm", new ChronoTimeHandler(ChronoUnit.MINUTES, "yyyy-M-d HH:mm"));
        //
        NAMED_PARSERS.put("yyyy-m-dd h:m", new ChronoTimeHandler(ChronoUnit.MINUTES, "yyyy-M-dd H:m"));
        NAMED_PARSERS.put("yyyy-m-dd h:mm", new ChronoTimeHandler(ChronoUnit.MINUTES, "yyyy-M-dd H:mm"));
        NAMED_PARSERS.put("yyyy-m-dd hh:m", new ChronoTimeHandler(ChronoUnit.MINUTES, "yyyy-M-dd HH:m"));
        NAMED_PARSERS.put("yyyy-m-dd hh:mm", new ChronoTimeHandler(ChronoUnit.MINUTES, "yyyy-M-dd HH:mm"));
        //
        NAMED_PARSERS.put("yyyy-mm-d h:m", new ChronoTimeHandler(ChronoUnit.MINUTES, "yyyy-MM-d H:m"));
        NAMED_PARSERS.put("yyyy-mm-d h:mm", new ChronoTimeHandler(ChronoUnit.MINUTES, "yyyy-MM-d H:mm"));
        NAMED_PARSERS.put("yyyy-mm-d hh:m", new ChronoTimeHandler(ChronoUnit.MINUTES, "yyyy-MM-d HH:m"));
        NAMED_PARSERS.put("yyyy-mm-d hh:mm", new ChronoTimeHandler(ChronoUnit.MINUTES, "yyyy-MM-d HH:mm"));
        //
        NAMED_PARSERS.put("yyyy/mm/dd h:m", new ChronoTimeHandler(ChronoUnit.MINUTES, "yyyy/MM/dd H:m"));
        NAMED_PARSERS.put("yyyy/mm/dd h:mm", new ChronoTimeHandler(ChronoUnit.MINUTES, "yyyy/MM/dd H:mm"));
        NAMED_PARSERS.put("yyyy/mm/dd hh:m", new ChronoTimeHandler(ChronoUnit.MINUTES, "yyyy/MM/dd HH:m"));
        NAMED_PARSERS.put("yyyy/mm/dd hh:mm", new ChronoTimeHandler(ChronoUnit.MINUTES, "yyyy/MM/dd HH:mm"));
        //
        NAMED_PARSERS.put("yyyy/mm/d h:m", new ChronoTimeHandler(ChronoUnit.MINUTES, "yyyy/MM/d H:m"));
        NAMED_PARSERS.put("yyyy/mm/d h:mm", new ChronoTimeHandler(ChronoUnit.MINUTES, "yyyy/MM/d H:mm"));
        NAMED_PARSERS.put("yyyy/mm/d hh:m", new ChronoTimeHandler(ChronoUnit.MINUTES, "yyyy/MM/d HH:m"));
        NAMED_PARSERS.put("yyyy/mm/d hh:mm", new ChronoTimeHandler(ChronoUnit.MINUTES, "yyyy/MM/d HH:mm"));
        //
        NAMED_PARSERS.put("yyyy/m/d h:m", new ChronoTimeHandler(ChronoUnit.MINUTES, "yyyy/M/d H:m"));
        NAMED_PARSERS.put("yyyy/m/d h:mm", new ChronoTimeHandler(ChronoUnit.MINUTES, "yyyy/M/d H:mm"));
        NAMED_PARSERS.put("yyyy/m/d hh:m", new ChronoTimeHandler(ChronoUnit.MINUTES, "yyyy/M/d HH:m"));
        NAMED_PARSERS.put("yyyy/m/d hh:mm", new ChronoTimeHandler(ChronoUnit.MINUTES, "yyyy/M/d HH:mm"));
        //
        NAMED_PARSERS.put("yyyy/m/dd h:m", new ChronoTimeHandler(ChronoUnit.MINUTES, "yyyy/M/dd H:m"));
        NAMED_PARSERS.put("yyyy/m/dd h:mm", new ChronoTimeHandler(ChronoUnit.MINUTES, "yyyy/M/dd H:mm"));
        NAMED_PARSERS.put("yyyy/m/dd hh:m", new ChronoTimeHandler(ChronoUnit.MINUTES, "yyyy/M/dd HH:m"));
        NAMED_PARSERS.put("yyyy/m/dd hh:mm", new ChronoTimeHandler(ChronoUnit.MINUTES, "yyyy/M/dd HH:mm"));
        //
        NAMED_PARSERS.put("dd/mm/yyyy h:m", new ChronoTimeHandler(ChronoUnit.MINUTES, "dd/MM/yyyy H:m"));
        NAMED_PARSERS.put("dd/mm/yyyy h:mm", new ChronoTimeHandler(ChronoUnit.MINUTES, "dd/MM/yyyy H:mm"));
        NAMED_PARSERS.put("dd/mm/yyyy hh:m", new ChronoTimeHandler(ChronoUnit.MINUTES, "dd/MM/yyyy HH:m"));
        NAMED_PARSERS.put("dd/mm/yyyy hh:mm", new ChronoTimeHandler(ChronoUnit.MINUTES, "dd/MM/yyyy HH:mm"));
        //
        NAMED_PARSERS.put("d/m/yyyy h:m", new ChronoTimeHandler(ChronoUnit.MINUTES, "d/M/yyyy H:m"));
        NAMED_PARSERS.put("d/m/yyyy h:mm", new ChronoTimeHandler(ChronoUnit.MINUTES, "d/M/yyyy H:mm"));
        NAMED_PARSERS.put("d/m/yyyy hh:m", new ChronoTimeHandler(ChronoUnit.MINUTES, "d/M/yyyy HH:m"));
        NAMED_PARSERS.put("d/m/yyyy hh:mm", new ChronoTimeHandler(ChronoUnit.MINUTES, "d/M/yyyy HH:mm"));
        //
        NAMED_PARSERS.put("dd/m/yyyy h:m", new ChronoTimeHandler(ChronoUnit.MINUTES, "dd/M/yyyy H:m"));
        NAMED_PARSERS.put("dd/m/yyyy h:mm", new ChronoTimeHandler(ChronoUnit.MINUTES, "dd/M/yyyy H:mm"));
        NAMED_PARSERS.put("dd/m/yyyy hh:m", new ChronoTimeHandler(ChronoUnit.MINUTES, "dd/M/yyyy HH:m"));
        NAMED_PARSERS.put("dd/m/yyyy hh:mm", new ChronoTimeHandler(ChronoUnit.MINUTES, "dd/M/yyyy HH:mm"));
        //
        NAMED_PARSERS.put("d/mm/yyyy h:m", new ChronoTimeHandler(ChronoUnit.MINUTES, "d/MM/yyyy H:m"));
        NAMED_PARSERS.put("d/mm/yyyy h:mm", new ChronoTimeHandler(ChronoUnit.MINUTES, "d/MM/yyyy H:mm"));
        NAMED_PARSERS.put("d/mm/yyyy hh:m", new ChronoTimeHandler(ChronoUnit.MINUTES, "d/MM/yyyy HH:m"));
        NAMED_PARSERS.put("d/mm/yyyy hh:mm", new ChronoTimeHandler(ChronoUnit.MINUTES, "d/MM/yyyy HH:mm"));
    }

    private static void initMonthParsers() {
        NAMED_PARSERS.put("yyyy-m", new ChronoTimeHandler(ChronoUnit.MONTHS, "yyyy-M", false));
        NAMED_PARSERS.put("mm/yyyy", new ChronoTimeHandler(ChronoUnit.MONTHS, "MM/yyyy", false));
        NAMED_PARSERS.put("m/yyyy", new ChronoTimeHandler(ChronoUnit.MONTHS, "M/yyyy", false));
    }

    private static void initNamedParsers() {
        initZonedParsers();

        initNormalParsers();

        initSecondsParsers();

        initMinutesParsers();

        initHoursParsers();

        initDaysParsers();

        initMonthParsers();

        NAMED_PARSERS.put("yyyy-ww", new ChronoTimeHandler(
                        ChronoUnit.WEEKS,
                        "yyyy-ww",
                        new DateTimeFormatterBuilder()
                                .parseCaseInsensitive()
                                .appendValue(IsoFields.WEEK_BASED_YEAR)
                                .appendLiteral("-")
                                .parseDefaulting(WeekFields.ISO.dayOfWeek(), 1)
                                .appendValue(IsoFields.WEEK_OF_WEEK_BASED_YEAR, Week.Monday.getNumber())
                                .toFormatter(),
                        false
                )
        );
        NAMED_PARSERS.put("yyyy-bb", new ChronoTimeHandler(
                ChronoUnit.YEARS,
                "yyyy-BB",
                datetime -> {
                    if (datetime.getMonthValue() < DateConstants.JULY) {
                        return datetime.getYear() + "-H1";
                    }

                    return datetime.getYear() + "-H2";
                },
                dateString -> {
                    Matcher h1Matcher = H_1.matcher(dateString);
                    if (h1Matcher.matches()) {
                        return Optional.of(
                                LocalDateTime.of(Numbers2.toDouble(h1Matcher.group(1)).intValue(),
                                        DateConstants.JAN, 1, 0, 0, 0));
                    }

                    Matcher h2Matcher = H_2.matcher(dateString);
                    if (h2Matcher.matches()) {
                        return Optional.of(
                                LocalDateTime.of(Numbers2.toDouble(h2Matcher.group(1)).intValue(),
                                        DateConstants.JULY, 1, 0, 0, 0));
                    }

                    return Optional.empty();
                },
                false
        ));
    }

    private static void initNormalParsers() {
        NAMED_PARSERS.put("yyyy-mm-dd hh:mm:ss.sss", new ChronoTimeHandler(ChronoUnit.MILLIS, "yyyy-MM-dd HH:mm:ss.SSS"));
        NAMED_PARSERS.put("yyyy-mm-dd hh:mm:ss", new ChronoTimeHandler(ChronoUnit.SECONDS, "yyyy-MM-dd HH:mm:ss"));
        NAMED_PARSERS.put("yyyy-mm-dd hh:mm", new ChronoTimeHandler(ChronoUnit.MINUTES, "yyyy-MM-dd HH:mm"));
        NAMED_PARSERS.put("yyyy-mm-dd hh", new ChronoTimeHandler(ChronoUnit.HOURS, "yyyy-MM-dd HH"));
        NAMED_PARSERS.put("yyyy-mm-dd", new ChronoTimeHandler(ChronoUnit.DAYS, "yyyy-MM-dd"));
        NAMED_PARSERS.put("yyyy-mm", new ChronoTimeHandler(ChronoUnit.MONTHS, "yyyy-MM", false));
        NAMED_PARSERS.put("yyyy", new ChronoTimeHandler(ChronoUnit.YEARS, "yyyy", false));
        NAMED_PARSERS.put("yyyy-mm-dd't'hh:mm:ss.sss", new ChronoTimeHandler(ChronoUnit.MILLIS, "yyyy-MM-dd'T'HH:mm:ss.SSS"));
        NAMED_PARSERS.put("yyyy-mm-dd't'hh:mm:ss", new ChronoTimeHandler(ChronoUnit.SECONDS, "yyyy-MM-dd'T'HH:mm:ss"));
        NAMED_PARSERS.put("yyyy-mm-dd't'hh:mm", new ChronoTimeHandler(ChronoUnit.MINUTES, "yyyy-MM-dd'T'HH:mm"));
        NAMED_PARSERS.put("yyyy-mm-dd't'hh", new ChronoTimeHandler(ChronoUnit.HOURS, "yyyy-MM-dd'T'HH"));
    }

    private static void initSecondsParsers() {
        //
        NAMED_PARSERS.put("yyyy-mm-dd h:m:s", new ChronoTimeHandler(ChronoUnit.SECONDS, "yyyy-MM-dd H:m:s"));
        NAMED_PARSERS.put("yyyy-mm-dd h:m:ss", new ChronoTimeHandler(ChronoUnit.SECONDS, "yyyy-MM-dd H:m:ss"));
        NAMED_PARSERS.put("yyyy-mm-dd h:mm:s", new ChronoTimeHandler(ChronoUnit.SECONDS, "yyyy-MM-dd H:mm:s"));
        NAMED_PARSERS.put("yyyy-mm-dd hh:m:s", new ChronoTimeHandler(ChronoUnit.SECONDS, "yyyy-MM-dd HH:m:s"));
        NAMED_PARSERS.put("yyyy-mm-dd hh:mm:s", new ChronoTimeHandler(ChronoUnit.SECONDS, "yyyy-MM-dd HH:mm:s"));
        NAMED_PARSERS.put("yyyy-mm-dd hh:m:ss", new ChronoTimeHandler(ChronoUnit.SECONDS, "yyyy-MM-dd HH:m:ss"));
        NAMED_PARSERS.put("yyyy-mm-dd h:mm:ss", new ChronoTimeHandler(ChronoUnit.SECONDS, "yyyy-MM-dd H:mm:ss"));
        NAMED_PARSERS.put("yyyy-mm-dd hh:mm:ss", new ChronoTimeHandler(ChronoUnit.SECONDS, "yyyy-MM-dd HH:mm:ss"));
        //
        NAMED_PARSERS.put("yyyy-m-d h:m:s", new ChronoTimeHandler(ChronoUnit.SECONDS, "yyyy-M-d H:m:s"));
        NAMED_PARSERS.put("yyyy-m-d h:m:ss", new ChronoTimeHandler(ChronoUnit.SECONDS, "yyyy-M-d H:m:ss"));
        NAMED_PARSERS.put("yyyy-m-d h:mm:s", new ChronoTimeHandler(ChronoUnit.SECONDS, "yyyy-M-d H:mm:s"));
        NAMED_PARSERS.put("yyyy-m-d hh:m:s", new ChronoTimeHandler(ChronoUnit.SECONDS, "yyyy-M-d HH:m:s"));
        NAMED_PARSERS.put("yyyy-m-d hh:mm:s", new ChronoTimeHandler(ChronoUnit.SECONDS, "yyyy-M-d HH:mm:s"));
        NAMED_PARSERS.put("yyyy-m-d hh:m:ss", new ChronoTimeHandler(ChronoUnit.SECONDS, "yyyy-M-d HH:m:ss"));
        NAMED_PARSERS.put("yyyy-m-d h:mm:ss", new ChronoTimeHandler(ChronoUnit.SECONDS, "yyyy-M-d H:mm:ss"));
        NAMED_PARSERS.put("yyyy-m-d hh:mm:ss", new ChronoTimeHandler(ChronoUnit.SECONDS, "yyyy-M-d HH:mm:ss"));
        //
        NAMED_PARSERS.put("yyyy-m-dd h:m:s", new ChronoTimeHandler(ChronoUnit.SECONDS, "yyyy-M-dd H:m:s"));
        NAMED_PARSERS.put("yyyy-m-dd h:m:ss", new ChronoTimeHandler(ChronoUnit.SECONDS, "yyyy-M-dd H:m:ss"));
        NAMED_PARSERS.put("yyyy-m-dd h:mm:s", new ChronoTimeHandler(ChronoUnit.SECONDS, "yyyy-M-dd H:mm:s"));
        NAMED_PARSERS.put("yyyy-m-dd hh:m:s", new ChronoTimeHandler(ChronoUnit.SECONDS, "yyyy-M-dd HH:m:s"));
        NAMED_PARSERS.put("yyyy-m-dd hh:mm:s", new ChronoTimeHandler(ChronoUnit.SECONDS, "yyyy-M-dd HH:mm:s"));
        NAMED_PARSERS.put("yyyy-m-dd hh:m:ss", new ChronoTimeHandler(ChronoUnit.SECONDS, "yyyy-M-dd HH:m:ss"));
        NAMED_PARSERS.put("yyyy-m-dd h:mm:ss", new ChronoTimeHandler(ChronoUnit.SECONDS, "yyyy-M-dd H:mm:ss"));
        NAMED_PARSERS.put("yyyy-m-dd hh:mm:ss", new ChronoTimeHandler(ChronoUnit.SECONDS, "yyyy-M-dd HH:mm:ss"));
        //
        NAMED_PARSERS.put("yyyy-mm-d h:m:s", new ChronoTimeHandler(ChronoUnit.SECONDS, "yyyy-MM-d H:m:s"));
        NAMED_PARSERS.put("yyyy-mm-d h:m:ss", new ChronoTimeHandler(ChronoUnit.SECONDS, "yyyy-MM-d H:m:ss"));
        NAMED_PARSERS.put("yyyy-mm-d h:mm:s", new ChronoTimeHandler(ChronoUnit.SECONDS, "yyyy-MM-d H:mm:s"));
        NAMED_PARSERS.put("yyyy-mm-d hh:m:s", new ChronoTimeHandler(ChronoUnit.SECONDS, "yyyy-MM-d HH:m:s"));
        NAMED_PARSERS.put("yyyy-mm-d hh:mm:s", new ChronoTimeHandler(ChronoUnit.SECONDS, "yyyy-MM-d HH:mm:s"));
        NAMED_PARSERS.put("yyyy-mm-d hh:m:ss", new ChronoTimeHandler(ChronoUnit.SECONDS, "yyyy-MM-d HH:m:ss"));
        NAMED_PARSERS.put("yyyy-mm-d h:mm:ss", new ChronoTimeHandler(ChronoUnit.SECONDS, "yyyy-MM-d H:mm:ss"));
        NAMED_PARSERS.put("yyyy-mm-d hh:mm:ss", new ChronoTimeHandler(ChronoUnit.SECONDS, "yyyy-MM-d HH:mm:ss"));
        //
        NAMED_PARSERS.put("yyyy/mm/dd h:m:s", new ChronoTimeHandler(ChronoUnit.SECONDS, "yyyy/MM/dd H:m:s"));
        NAMED_PARSERS.put("yyyy/mm/dd h:m:ss", new ChronoTimeHandler(ChronoUnit.SECONDS, "yyyy/MM/dd H:m:ss"));
        NAMED_PARSERS.put("yyyy/mm/dd h:mm:s", new ChronoTimeHandler(ChronoUnit.SECONDS, "yyyy/MM/dd H:mm:s"));
        NAMED_PARSERS.put("yyyy/mm/dd hh:m:s", new ChronoTimeHandler(ChronoUnit.SECONDS, "yyyy/MM/dd HH:m:s"));
        NAMED_PARSERS.put("yyyy/mm/dd hh:mm:s", new ChronoTimeHandler(ChronoUnit.SECONDS, "yyyy/MM/dd HH:mm:s"));
        NAMED_PARSERS.put("yyyy/mm/dd hh:m:ss", new ChronoTimeHandler(ChronoUnit.SECONDS, "yyyy/MM/dd HH:m:ss"));
        NAMED_PARSERS.put("yyyy/mm/dd h:mm:ss", new ChronoTimeHandler(ChronoUnit.SECONDS, "yyyy/MM/dd H:mm:ss"));
        NAMED_PARSERS.put("yyyy/mm/dd hh:mm:ss", new ChronoTimeHandler(ChronoUnit.SECONDS, "yyyy/MM/dd HH:mm:ss"));
        //
        NAMED_PARSERS.put("yyyy/mm/d h:m:s", new ChronoTimeHandler(ChronoUnit.SECONDS, "yyyy/MM/d H:m:s"));
        NAMED_PARSERS.put("yyyy/mm/d h:m:ss", new ChronoTimeHandler(ChronoUnit.SECONDS, "yyyy/MM/d H:m:ss"));
        NAMED_PARSERS.put("yyyy/mm/d h:mm:s", new ChronoTimeHandler(ChronoUnit.SECONDS, "yyyy/MM/d H:mm:s"));
        NAMED_PARSERS.put("yyyy/mm/d hh:m:s", new ChronoTimeHandler(ChronoUnit.SECONDS, "yyyy/MM/d HH:m:s"));
        NAMED_PARSERS.put("yyyy/mm/d hh:mm:s", new ChronoTimeHandler(ChronoUnit.SECONDS, "yyyy/MM/d HH:mm:s"));
        NAMED_PARSERS.put("yyyy/mm/d hh:m:ss", new ChronoTimeHandler(ChronoUnit.SECONDS, "yyyy/MM/d HH:m:ss"));
        NAMED_PARSERS.put("yyyy/mm/d h:mm:ss", new ChronoTimeHandler(ChronoUnit.SECONDS, "yyyy/MM/d H:mm:ss"));
        NAMED_PARSERS.put("yyyy/mm/d hh:mm:ss", new ChronoTimeHandler(ChronoUnit.SECONDS, "yyyy/MM/d HH:mm:ss"));
        //
        NAMED_PARSERS.put("yyyy/m/d h:m:s", new ChronoTimeHandler(ChronoUnit.SECONDS, "yyyy/M/d H:m:s"));
        NAMED_PARSERS.put("yyyy/m/d h:m:ss", new ChronoTimeHandler(ChronoUnit.SECONDS, "yyyy/M/d H:m:ss"));
        NAMED_PARSERS.put("yyyy/m/d h:mm:s", new ChronoTimeHandler(ChronoUnit.SECONDS, "yyyy/M/d H:mm:s"));
        NAMED_PARSERS.put("yyyy/m/d hh:m:s", new ChronoTimeHandler(ChronoUnit.SECONDS, "yyyy/M/d HH:m:s"));
        NAMED_PARSERS.put("yyyy/m/d hh:mm:s", new ChronoTimeHandler(ChronoUnit.SECONDS, "yyyy/M/d HH:mm:s"));
        NAMED_PARSERS.put("yyyy/m/d hh:m:ss", new ChronoTimeHandler(ChronoUnit.SECONDS, "yyyy/M/d HH:m:ss"));
        NAMED_PARSERS.put("yyyy/m/d h:mm:ss", new ChronoTimeHandler(ChronoUnit.SECONDS, "yyyy/M/d H:mm:ss"));
        NAMED_PARSERS.put("yyyy/m/d hh:mm:ss", new ChronoTimeHandler(ChronoUnit.SECONDS, "yyyy/M/d HH:mm:ss"));
        //
        NAMED_PARSERS.put("yyyy/m/dd h:m:s", new ChronoTimeHandler(ChronoUnit.SECONDS, "yyyy/M/dd H:m:s"));
        NAMED_PARSERS.put("yyyy/m/dd h:m:ss", new ChronoTimeHandler(ChronoUnit.SECONDS, "yyyy/M/dd H:m:ss"));
        NAMED_PARSERS.put("yyyy/m/dd h:mm:s", new ChronoTimeHandler(ChronoUnit.SECONDS, "yyyy/M/dd H:mm:s"));
        NAMED_PARSERS.put("yyyy/m/dd hh:m:s", new ChronoTimeHandler(ChronoUnit.SECONDS, "yyyy/M/dd HH:m:s"));
        NAMED_PARSERS.put("yyyy/m/dd hh:mm:s", new ChronoTimeHandler(ChronoUnit.SECONDS, "yyyy/M/dd HH:mm:s"));
        NAMED_PARSERS.put("yyyy/m/dd hh:m:ss", new ChronoTimeHandler(ChronoUnit.SECONDS, "yyyy/M/dd HH:m:ss"));
        NAMED_PARSERS.put("yyyy/m/dd h:mm:ss", new ChronoTimeHandler(ChronoUnit.SECONDS, "yyyy/M/dd H:mm:ss"));
        NAMED_PARSERS.put("yyyy/m/dd hh:mm:ss", new ChronoTimeHandler(ChronoUnit.SECONDS, "yyyy/M/dd HH:mm:ss"));
        //
        NAMED_PARSERS.put("dd/mm/yyyy h:m:s", new ChronoTimeHandler(ChronoUnit.SECONDS, "dd/MM/yyyy H:m:s"));
        NAMED_PARSERS.put("dd/mm/yyyy h:m:ss", new ChronoTimeHandler(ChronoUnit.SECONDS, "dd/MM/yyyy H:m:ss"));
        NAMED_PARSERS.put("dd/mm/yyyy h:mm:s", new ChronoTimeHandler(ChronoUnit.SECONDS, "dd/MM/yyyy H:mm:s"));
        NAMED_PARSERS.put("dd/mm/yyyy hh:m:s", new ChronoTimeHandler(ChronoUnit.SECONDS, "dd/MM/yyyy HH:m:s"));
        NAMED_PARSERS.put("dd/mm/yyyy hh:mm:s", new ChronoTimeHandler(ChronoUnit.SECONDS, "dd/MM/yyyy HH:mm:s"));
        NAMED_PARSERS.put("dd/mm/yyyy hh:m:ss", new ChronoTimeHandler(ChronoUnit.SECONDS, "dd/MM/yyyy HH:m:ss"));
        NAMED_PARSERS.put("dd/mm/yyyy h:mm:ss", new ChronoTimeHandler(ChronoUnit.SECONDS, "dd/MM/yyyy H:mm:ss"));
        NAMED_PARSERS.put("dd/mm/yyyy hh:mm:ss", new ChronoTimeHandler(ChronoUnit.SECONDS, "dd/MM/yyyy HH:mm:ss"));
        //
        NAMED_PARSERS.put("d/m/yyyy h:m:s", new ChronoTimeHandler(ChronoUnit.SECONDS, "d/M/yyyy H:m:s"));
        NAMED_PARSERS.put("d/m/yyyy h:m:ss", new ChronoTimeHandler(ChronoUnit.SECONDS, "d/M/yyyy H:m:ss"));
        NAMED_PARSERS.put("d/m/yyyy h:mm:s", new ChronoTimeHandler(ChronoUnit.SECONDS, "d/M/yyyy H:mm:s"));
        NAMED_PARSERS.put("d/m/yyyy hh:m:s", new ChronoTimeHandler(ChronoUnit.SECONDS, "d/M/yyyy HH:m:s"));
        NAMED_PARSERS.put("d/m/yyyy hh:mm:s", new ChronoTimeHandler(ChronoUnit.SECONDS, "d/M/yyyy HH:mm:s"));
        NAMED_PARSERS.put("d/m/yyyy hh:m:ss", new ChronoTimeHandler(ChronoUnit.SECONDS, "d/M/yyyy HH:m:ss"));
        NAMED_PARSERS.put("d/m/yyyy h:mm:ss", new ChronoTimeHandler(ChronoUnit.SECONDS, "d/M/yyyy H:mm:ss"));
        NAMED_PARSERS.put("d/m/yyyy hh:mm:ss", new ChronoTimeHandler(ChronoUnit.SECONDS, "d/M/yyyy HH:mm:ss"));
        //
        NAMED_PARSERS.put("dd/m/yyyy h:m:s", new ChronoTimeHandler(ChronoUnit.SECONDS, "dd/M/yyyy H:m:s"));
        NAMED_PARSERS.put("dd/m/yyyy h:m:ss", new ChronoTimeHandler(ChronoUnit.SECONDS, "dd/M/yyyy H:m:ss"));
        NAMED_PARSERS.put("dd/m/yyyy h:mm:s", new ChronoTimeHandler(ChronoUnit.SECONDS, "dd/M/yyyy H:mm:s"));
        NAMED_PARSERS.put("dd/m/yyyy hh:m:s", new ChronoTimeHandler(ChronoUnit.SECONDS, "dd/M/yyyy HH:m:s"));
        NAMED_PARSERS.put("dd/m/yyyy hh:mm:s", new ChronoTimeHandler(ChronoUnit.SECONDS, "dd/M/yyyy HH:mm:s"));
        NAMED_PARSERS.put("dd/m/yyyy hh:m:ss", new ChronoTimeHandler(ChronoUnit.SECONDS, "dd/M/yyyy HH:m:ss"));
        NAMED_PARSERS.put("dd/m/yyyy h:mm:ss", new ChronoTimeHandler(ChronoUnit.SECONDS, "dd/M/yyyy H:mm:ss"));
        NAMED_PARSERS.put("dd/m/yyyy hh:mm:ss", new ChronoTimeHandler(ChronoUnit.SECONDS, "dd/M/yyyy HH:mm:ss"));
        //
        NAMED_PARSERS.put("d/mm/yyyy h:m:s", new ChronoTimeHandler(ChronoUnit.SECONDS, "d/MM/yyyy H:m:s"));
        NAMED_PARSERS.put("d/mm/yyyy h:m:ss", new ChronoTimeHandler(ChronoUnit.SECONDS, "d/MM/yyyy H:m:ss"));
        NAMED_PARSERS.put("d/mm/yyyy h:mm:s", new ChronoTimeHandler(ChronoUnit.SECONDS, "d/MM/yyyy H:mm:s"));
        NAMED_PARSERS.put("d/mm/yyyy hh:m:s", new ChronoTimeHandler(ChronoUnit.SECONDS, "d/MM/yyyy HH:m:s"));
        NAMED_PARSERS.put("d/mm/yyyy hh:mm:s", new ChronoTimeHandler(ChronoUnit.SECONDS, "d/MM/yyyy HH:mm:s"));
        NAMED_PARSERS.put("d/mm/yyyy hh:m:ss", new ChronoTimeHandler(ChronoUnit.SECONDS, "d/MM/yyyy HH:m:ss"));
        NAMED_PARSERS.put("d/mm/yyyy h:mm:ss", new ChronoTimeHandler(ChronoUnit.SECONDS, "d/MM/yyyy H:mm:ss"));
        NAMED_PARSERS.put("d/mm/yyyy hh:mm:ss", new ChronoTimeHandler(ChronoUnit.SECONDS, "d/MM/yyyy HH:mm:ss"));
    }

    private static void initSizedParsers() {
        NAMED_PARSERS.forEach((name, handler) -> {
            int length = name.length() - Strings.containsTimes(name, '\'');

            if (name.contains("ZZ")) {
                //+07:00
                length += ZONED_LENGTH_DELTA;
            }

            if (SIZED_PARSERS.containsKey(length)) {
                SIZED_PARSERS.get(length).add(handler);
            } else {
                SIZED_PARSERS.put(length, Lists2.items(handler));
            }
        });
    }

    private static void initZonedParsers() {
        NAMED_PARSERS.put("yyyy-MM-dd'T'HH:mm:ss.SSSZ", new ChronoTimeHandler(ChronoUnit.MILLIS, "yyyy-MM-dd'T'HH:mm:ss.SSSZ",
                DateTimeFormatter.ISO_ZONED_DATE_TIME));
        NAMED_PARSERS.put("yyyy-MM-dd'T'HH:mm:ss.SSSZZ", new ChronoTimeHandler(ChronoUnit.MILLIS, "yyyy-MM-dd'T'HH:mm:ss.SSSZZ",
                DateTimeFormatter.ISO_ZONED_DATE_TIME));
        NAMED_PARSERS.put("yyyy-MM-dd'T'HH:mm:ssZ", new ChronoTimeHandler(ChronoUnit.SECONDS, "yyyy-MM-dd'T'HH:mm:ssZ",
                DateTimeFormatter.ISO_ZONED_DATE_TIME));
        NAMED_PARSERS.put("yyyy-MM-dd'T'HH:mm:ssZZ", new ChronoTimeHandler(ChronoUnit.SECONDS, "yyyy-MM-dd'T'HH:mm:ssZZ",
                DateTimeFormatter.ISO_ZONED_DATE_TIME));
        NAMED_PARSERS.put("yyyy-MM-dd'T'HH:mmZ", new ChronoTimeHandler(ChronoUnit.MINUTES, "yyyy-MM-dd'T'HH:mmZ",
                DateTimeFormatter.ISO_ZONED_DATE_TIME));
        NAMED_PARSERS.put("yyyy-MM-dd'T'HH:mmZZ", new ChronoTimeHandler(ChronoUnit.MINUTES, "yyyy-MM-dd'T'HH:mmZZ",
                DateTimeFormatter.ISO_ZONED_DATE_TIME));
    }
}
