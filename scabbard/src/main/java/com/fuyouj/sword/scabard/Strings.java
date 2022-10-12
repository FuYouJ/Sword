package com.fuyouj.sword.scabard;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class Strings {
    public static final String EMPTY = "";
    public static final String TRUE = "true";
    public static final String FALSE = "false";
    public static final StringEscapeTarget DEFAULT_TARGET = new StringEscapeTarget('"');
    public static final StringEscapeTarget ESCAPE_TARGET = new StringEscapeTarget('\\');
    public static final char CHAR_SPACE = ' ';
    private static final int UPPER_CASE_OFFSET = 32;
    private static final String[] EMPTY_STRING_ARRAY = new String[]{};
    private static final int INDEX_NOT_FOUND = -1;

    private Strings() {
    }

    public static int containsTimes(final String name, final char aChar) {
        if (name == null) {
            return 0;
        }

        int total = 0;
        int length = name.length();
        for (int index = 0; index < length; index++) {
            if (name.charAt(index) == aChar) {
                total += 1;
            }
        }
        return total;
    }

    public static String fromEnum(final Class<? extends Enum> enumClass) {
        List<String> values = Arrays
                .stream(enumClass.getEnumConstants())
                .map(Enum::name)
                .collect(toList());

        return fromList(values, ",");
    }

    public static <T> String fromList(final Collection<T> objects, final String joiner) {
        String internalJoiner = joiner;
        String prefix = "";
        String suffix = "";
        if (Strings.isBlank(internalJoiner)) {
            internalJoiner = " , ";
            prefix = "\n    ";
            suffix = "\n";
        }

        if (Lists2.isNullOrEmpty(objects)) {
            return "[]";
        }

        return "[" + prefix
                + join(objects.stream().map(Object::toString).collect(toList()), internalJoiner)
                + suffix + "]";
    }

    public static boolean isBlank(final String value) {
        return isNullOrEmpty(value) || isNullOrEmpty(value.trim());
    }

    public static boolean isNotBlank(final String source) {
        return !isBlank(source);
    }

    public static boolean isNullOrEmpty(final String source) {
        return source == null || source.isEmpty();
    }

    public static String join(final List<String> stringList, final String separator) {
        if (stringList == null || stringList.isEmpty()) {
            return "";
        }

        StringBuilder stringBuilder = new StringBuilder();

        final int size = stringList.size();

        for (int index = 0; index < size; index++) {
            stringBuilder.append(stringList.get(index));

            if (index < size - 1) {
                stringBuilder.append(separator);
            }
        }

        return stringBuilder.toString();
    }

    public static boolean needToTrim(final String target) {
        if (target == null) {
            return false;
        }

        return target.length() > 0 && (target.charAt(0) == CHAR_SPACE || target.charAt(target.length() - 1) == CHAR_SPACE);
    }

    public static String toString(final Object object) {
        if (object == null) {
            return "";
        }

        return object.toString();
    }
}
