package com.fuyouj.sword.scabard;

public class Comparators {
    public static boolean lessThan(final Object left, final Object right) {
        return Objects2.compare(left, right) < 0;
    }

    public static boolean equal(final Object left, final Object right) {
        return Objects2.compare(left, right) == 0;
    }

    public static boolean notEqual(final Object left, final Object right) {
        return Objects2.compare(left, right) != 0;
    }

    public static Boolean lessThanEqual(final Object left, final Object right) {
        return Objects2.compare(left, right) <= 0;
    }

    public static Boolean greaterThan(final Object left, final Object right) {
        return Objects2.compare(left, right) > 0;
    }

    public static Boolean greaterThanEqual(final Object left, final Object right) {
        return Objects2.compare(left, right) >= 0;
    }

    public static Boolean matchWith(final Object str, final Object regex) {
        return Objects2.objectMatchString(str, regex);
    }

    public static Boolean unMatchWith(final Object str, final Object regex) {
        return !Objects2.objectMatchString(str, regex);
    }
}
