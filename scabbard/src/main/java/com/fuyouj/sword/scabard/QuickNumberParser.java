package com.fuyouj.sword.scabard;

import java.util.Optional;

public class QuickNumberParser {
    public static final QuickNumberParser GENERAL = new QuickNumberParser(NumberParseMode.Eager);
    public static final QuickNumberParser NONE_SCIENTIFIC = new QuickNumberParser(NumberParseMode.IgnoreScientific);
    private static final char CHAR_THOUSAND_DELIMITER = ',';
    private static final char MINUS = '-';
    private static final char PLUS = '+';
    private static final char LOWER_SCIENTIFIC_CHAR = 'e';
    private static final char UPPER_SCIENTIFIC_CHAR = 'E';
    private static final int RADIX = 10;
    private static final char CHAR_0 = '0';
    private static final char CHAR_9 = '9';
    private static final char DOT = '.';
    private static final int THOUSAND_BIT_LENGTH = 4;

    private final NumberParseMode mode;

    public QuickNumberParser(final NumberParseMode mode) {
        this.mode = mode;
    }

    public final boolean isNumber(final String stringDouble) {
        return quickParse(stringDouble).isPresent();
    }

    public Optional<Double> quickParse(final String stringDouble) {
        if (stringDouble == null) {
            return Optional.empty();
        }
        return this.doQuickParse(stringDouble.trim(), this.mode);
    }

    private String checkAndRemoveThousandDelimiter(final String stringDouble) {
        if (stringDouble.indexOf(CHAR_THOUSAND_DELIMITER) == -1) {
            return stringDouble;
        }

        int dotIndex = stringDouble.indexOf(".");
        //是否发现正负符号
        boolean signSpotted = false;

        //有小数
        if (dotIndex != -1) {
            //小数位数中包含千分符，则为非法数字
            if (stringDouble.indexOf(CHAR_THOUSAND_DELIMITER, dotIndex) >= 0) {
                return null;
            }
        }

        int checkPosition = dotIndex - 1;

        if (checkPosition < 0) {
            checkPosition = stringDouble.length() - 1;
        }

        char currentChar;
        //跌倒的数字位数
        int bitIndex = 0;
        //从个数开始迭代
        for (int index = checkPosition; index >= 0; index--) {
            if (signSpotted) {
                return null;
            }

            bitIndex++;
            currentChar = stringDouble.charAt(index);

            if (currentChar == MINUS || currentChar == PLUS) {
                signSpotted = true;
                continue;
            }

            if (bitIndex % THOUSAND_BIT_LENGTH == 0) {
                if (currentChar != CHAR_THOUSAND_DELIMITER) {
                    return null;
                }

                if (index == 0) {
                    return null;
                }

                continue;
            }

            if (currentChar < CHAR_0 || currentChar > CHAR_9) {
                return null;
            }
        }

        return stringDouble.replaceAll(",", "");
    }

    private Optional<Double> doQuickParse(final String stringDouble, final NumberParseMode mode) {
        String toBeParsed = stringDouble;

        double result = 0.0d;
        int fractionIndex = 0;
        boolean fractional = false;
        boolean negative = false;
        boolean scientifically = false;
        int scientificIndex = 0;

        if (Strings.isBlank(toBeParsed)) {
            return Optional.empty();
        }

        toBeParsed = checkAndRemoveThousandDelimiter(stringDouble);

        if (toBeParsed == null) {
            return Optional.empty();
        }

        if (toBeParsed.charAt(0) == MINUS) {
            negative = true;
            toBeParsed = toBeParsed.substring(1);
        } else if (toBeParsed.charAt(0) == PLUS) {
            toBeParsed = toBeParsed.substring(1);
        }

        if (Strings.isBlank(toBeParsed)) {
            return Optional.empty();
        }

        if (toBeParsed.charAt(0) < CHAR_0 || toBeParsed.charAt(0) > CHAR_9) {
            return Optional.empty();
        }

        for (int index = 0; index < toBeParsed.length(); index++) {
            char bit = toBeParsed.charAt(index);

            if (bit == DOT) {
                if (!fractional) {
                    fractional = true;
                    fractionIndex = index;
                    continue;
                }

                return Optional.empty();
            }

            if (bit == LOWER_SCIENTIFIC_CHAR || bit == UPPER_SCIENTIFIC_CHAR) {

                if (mode == NumberParseMode.IgnoreScientific) {
                    return Optional.empty();
                }

                scientifically = true;
                scientificIndex = index;
                break;
            }

            if (bit < CHAR_0 || bit > CHAR_9) {
                return Optional.empty();
            }

            result = result * RADIX + bit - CHAR_0;
        }

        if (scientifically) {
            Optional<Double> optionalScientificSuffix = doQuickParse(
                    toBeParsed.substring(scientificIndex + 1),
                    NumberParseMode.IgnoreScientific);

            if (optionalScientificSuffix.isEmpty() || !Numbers2.canBeInt(optionalScientificSuffix.get())) {
                return Optional.empty();
            }

            result = result * Math.pow(RADIX, optionalScientificSuffix.get().intValue());
        }

        if (fractional) {
            if (scientifically) {
                result /= Math.pow(RADIX, scientificIndex - fractionIndex - 1);
            } else {
                result /= Math.pow(RADIX, toBeParsed.length() - fractionIndex - 1);
            }
        }

        if (negative) {
            return Optional.of(-result);
        }

        return Optional.of(result);
    }
}
