package com.fuyouj.sword.scabard;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.databind.ObjectMapper;

public class Objects2 {
    private static final InternalComparator DEFAULT_COMPARATOR = new CompositeComparator();
    public static boolean isNullOrEmpty(final Object object) {
        if (object == null) {
            return true;
        }

        if (object instanceof String) {
            return Strings.isNullOrEmpty((String) object);
        }

        return false;
    }

    public static Object natureConvert(final Object raw) {

        if (raw == null) {
            return null;
        }

        if (raw instanceof Number) {
            return ((Number) raw).doubleValue();
        }

        String rawAsStr = Strings.toString(raw);

        Double number = Numbers2.tryParseDouble(rawAsStr).orElse(null);
        if (number != null) {
            return number;
        }

        //对于String,大概率不是dateTime，以一个低成本的方式阻拦，避免DateTimes2.toLocal被打爆
        if (raw instanceof String && DateTimes2.dateTimeImpossible(rawAsStr)) {
            return rawAsStr;
        }

        LocalDateTime dateTime = DateTimes2.toLocal(raw, null).orElse(null);
        if (dateTime != null) {
            return dateTime;
        }

        return rawAsStr;
    }
    public static int compareByNaturalOrder(final Object o1, final Object o2) {
        if (o1 instanceof Comparable && o2 instanceof Comparable) {
            Comparator naturalOrder = Comparator.naturalOrder();
            return naturalOrder.compare(o1, o2);
        }

        return 0;
    }

    public static int compare(final Object obj1, final Object obj2) {
        return DEFAULT_COMPARATOR.compare(obj1, obj2);
    }

    private interface InternalComparator {
        Integer compare(Object obj1, Object obj2);
    }

    private static class BigNumberComparator implements InternalComparator {
        @Override
        public Integer compare(final Object obj1, final Object obj2) {
            String str1 = obj1.toString();
            String str2 = obj2.toString();

            if (!Numbers2.isNumber(str1) || !Numbers2.isNumber(str2)) {
                return null;
            }

            try {
                BigDecimal bigDecimal1 = new BigDecimal(str1);
                BigDecimal bigDecimal2 = new BigDecimal(str2);

                return bigDecimal1.compareTo(bigDecimal2);
            } catch (Exception ex) {
                return null;
            }
        }
    }

    private static class EffectiveStringComparator implements InternalComparator {
        @Override
        public Integer compare(final Object obj1, final Object obj2) {
            if (obj1 == obj2 || obj1.equals(obj2)) {
                return 0;
            }

            return obj1.toString().compareTo(obj2.toString());
        }
    }

    private static class EffectiveNumberComparator implements InternalComparator {
        @Override
        public Integer compare(final Object obj1, final Object obj2) {
            Double d1 = Numbers2.toDouble(obj1, null);
            Double d2 = Numbers2.toDouble(obj2, null);

            if (d1 == null && d2 == null) {
                return null;
            }

            if (d1 == null) {
                return -1;
            }

            if (d2 == null) {
                return 1;
            }

            if (!Numbers2.withDoublePrecision(d1) || !Numbers2.withDoublePrecision(d2)) {
                return null;
            }

            return Double.compare(d1, d2);
        }
    }

    private static class NullableComparator implements InternalComparator {
        @Override
        public Integer compare(final Object obj1, final Object obj2) {
            if (obj1 == null && obj2 == null) {
                return 0;
            }

            if (obj1 == null) {
                if (obj2 instanceof String && isBlank(obj2)) {
                    return 0;
                } else {
                    return -1;
                }
            }

            if (obj2 == null) {
                if (obj1 instanceof String && isBlank(obj1)) {
                    return 0;
                } else {
                    return 1;
                }
            }

            return null;
        }
    }

    private static class NumberComparator implements InternalComparator {
        @Override
        public Integer compare(final Object obj1, final Object obj2) {
            if (obj1 instanceof Number && obj2 instanceof Number) {
                double double1 = ((Number) obj1).doubleValue();
                double double2 = ((Number) obj2).doubleValue();

                if (Numbers2.withDoublePrecision(double1) && Numbers2.withDoublePrecision(double2)) {
                    return Double.compare(((Number) obj1).doubleValue(), double2);
                }

            }

            if (obj1 instanceof Number && Numbers2.withDoublePrecision(((Number) obj1).doubleValue())) {
                Optional<Double> optionalDouble = Numbers2.tryParseDouble(obj2.toString());

                if (optionalDouble.isPresent() && Numbers2.withDoublePrecision(optionalDouble.get())) {
                    return Double.compare(((Number) obj1).doubleValue(), optionalDouble.get());
                }
            }

            if (obj2 instanceof Number && Numbers2.withDoublePrecision(((Number) obj2).doubleValue())) {
                Optional<Double> optionalDouble = Numbers2.tryParseDouble(obj1.toString());

                if (optionalDouble.isPresent() && Numbers2.withDoublePrecision(optionalDouble.get())) {
                    return Double.compare(optionalDouble.get(), ((Number) obj2).doubleValue());
                }
            }

            return null;
        }
    }

    private static class StringComparator implements InternalComparator {
        @Override
        public Integer compare(final Object obj1, final Object obj2) {
            if (obj1 instanceof String) {
                return ((String) obj1).trim().compareTo(obj2.toString().trim());
            }

            if (obj2 instanceof String) {
                return (obj1.toString().trim()).compareTo(((String) obj2).trim());
            }

            return null;
        }
    }

    private static class CompositeComparator implements InternalComparator {
        private final List<InternalComparator> comparators;

        private CompositeComparator() {
            comparators = Lists2.items(
                    new NullableComparator(),
                    new NumberComparator(),
                    new EffectiveNumberComparator(),
                    new BigNumberComparator(),
                    new StringComparator(),
                    new EffectiveStringComparator()
            );
        }

        @Override
        public Integer compare(final Object obj1, final Object obj2) {
            Integer result = null;

            for (InternalComparator comparator : comparators) {
                result = comparator.compare(obj1, obj2);

                if (result != null) {
                    break;
                }
            }

            return result;
        }
    }

    public static boolean isBlank(final Object object) {
        if (object == null) {
            return true;
        }

        if (object instanceof String) {
            return Strings.isBlank((String) object);
        }

        return false;
    }

    public static boolean objectMatchString(final Object obj, final Object regex) {
        return Objects2.asString(obj).contains(Objects2.asString(regex));
    }

    public static String asString(final Object obj) {
        if (obj == null) {
            return "";
        }

        if (obj instanceof Number) {
            return Numbers2.asString((Number) obj);
        }

        if (DateTimes2.isDateTime(obj)) {
            return DateTimes2.format(obj);
        }

        return obj.toString();
    }

    public static String asJsonString(final Object obj) {
        return Jsons.toJsonString(obj);
    }

    public static String asJsonString(final Object obj, final ObjectMapper objectMapper) {
        return Jsons.toJsonString(obj, objectMapper);
    }
}
