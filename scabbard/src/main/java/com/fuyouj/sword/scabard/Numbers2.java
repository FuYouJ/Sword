package com.fuyouj.sword.scabard;

import java.text.DecimalFormat;
import java.util.Optional;

import static java.util.Optional.ofNullable;

public class Numbers2 {
    private static final double DEFAULT_DOUBLE = 0.0;
    private static final DecimalFormat SCIENTIFIC_FORMATTER = new DecimalFormat("#.############E0");
    private static final DecimalFormat PLAN_FORMATTER = new DecimalFormat("#.######");
    private static final double SCIENTIFIC_FICTION_UPPER_LEVEL = 9999999999999d;
    private static final double SCIENTIFIC_FICTION_LOWER_LEVEL = 0.00001d;

    public static String asString(final Number obj) {
        if (obj == null) {
            return "";
        }

        if (!withDoublePrecision(obj.doubleValue())) {
            return SCIENTIFIC_FORMATTER.format(obj.doubleValue());
        }

        return PLAN_FORMATTER.format(obj.doubleValue());
    }

    public static boolean canBeInt(final Double aDouble) {
        if (aDouble == null) {
            return false;
        }

        return aDouble - aDouble.intValue() == 0;
    }

    public static boolean isNumber(final String str) {
        return QuickNumberParser.GENERAL.isNumber(str);
    }

    public static Double toDouble(final Object obj, final Double defaultValue) {
        if (obj == null) {
            return defaultValue;
        }

        if (obj instanceof Number) {
            return ((Number) obj).doubleValue();
        }

        if (obj instanceof String) {
            return QuickNumberParser.GENERAL.quickParse(((String) obj).trim()).orElse(defaultValue);
        }

        return defaultValue;
    }

    public static Double toDouble(final Object obj) {
        return toDouble(obj, DEFAULT_DOUBLE);
    }

    public static Optional<Double> tryParseDouble(final String obj) {
        return ofNullable(toDouble(obj, null));
    }

    public static Optional<Integer> tryParseInt(final String obj) {
        return tryParseDouble(obj).map(Double::intValue);
    }

    public static Optional<Long> tryParseLong(final Object obj) {
        return ofNullable(toDouble(obj, null)).map(Double::longValue);
    }

    public static boolean withDoublePrecision(final double aDouble) {
        double abs = Math.abs(aDouble);
        return abs == 0 || abs <= SCIENTIFIC_FICTION_UPPER_LEVEL && abs >= SCIENTIFIC_FICTION_LOWER_LEVEL;
    }

    static Integer toInt(final Object obj) {
        return toDouble(obj).intValue();
    }
}
