package com.fuyouj.sword.scabard;

import java.text.DecimalFormat;
import java.util.Optional;

import lombok.Getter;

public class Bytes {
    private static final char[] HEX_CHARS = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
    private static final DecimalFormat DECIMAL_FORMATTER = new DecimalFormat("#.##");
    private static final int HIGH_FOUR_BITS = 0xF0;
    private static final int LOW_FOUR_BITS = 0x0F;
    private static final int BITS_TO_MOVE = 4;

    public static char[] encodeHex(final byte[] data) {
        if (data == null) {
            return new char[]{};
        }

        int length = data.length;
        char[] chars = new char[length << 1];
        for (int index = 0, j = 0; index < length; index++) {
            chars[j++] = HEX_CHARS[(HIGH_FOUR_BITS & data[index]) >>> BITS_TO_MOVE];
            chars[j++] = HEX_CHARS[LOW_FOUR_BITS & data[index]];
        }

        return chars;
    }

    public static String intToReadableString(final Integer size) {
        if (size == null) {
            return "";
        }

        return toReadableString(Long.valueOf(size));
    }

    public static String toReadableString(final Long size) {
        if (size == null) {
            return "";
        }

        return ByteUnit.search(size).map(unit -> format(size, unit)).orElse("");
    }

    private static String format(final Long size, final ByteUnit unit) {
        return DECIMAL_FORMATTER.format(size / unit.getSize()) + unit.getDisplayName();
    }

    @Getter
    enum ByteUnit {
        Byte(1, "B"),
        KB(Constants.UNIT, "KB"),
        MB(Constants.UNIT * Constants.UNIT, "MB"),
        GB(Constants.UNIT * Constants.UNIT * Constants.UNIT, "GB");

        private final double size;
        private final String displayName;

        ByteUnit(final double size, final String displayName) {
            this.size = size;
            this.displayName = displayName;
        }

        public static Optional<ByteUnit> search(final Long size) {
            if (size == null || size < 0) {
                return Optional.empty();
            }

            ByteUnit result = Byte;
            for (ByteUnit unit : values()) {
                if (unit.getSize() > size * Constants.RATIO) {
                    break;
                }

                result = unit;
            }

            return Optional.of(result);
        }

        private static class Constants {
            public static final double UNIT = 1024.0;
            public static final long RATIO = 2;
        }
    }
}
