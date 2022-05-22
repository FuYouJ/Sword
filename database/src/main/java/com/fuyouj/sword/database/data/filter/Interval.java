package com.fuyouj.sword.database.data.filter;

import java.util.Objects;

import com.fuyouj.sword.scabard.Asserts;
import com.fuyouj.sword.scabard.Objects2;
import com.fuyouj.sword.scabard.exception.SwordException;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * @author hangwen
 * @date 2020/10/13
 */
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode(callSuper = false)
public class Interval {
    static final Interval EMPTY = openInterval("", "");

    private final Object left;
    private final Object right;
    private final boolean leftOpen;
    private final boolean rightOpen;

    public static Interval closedInterval(final Object left, final Object right) {
        assertValue(left, right);

        return new Interval(left, right, false, false);
    }

    public static Interval closedOpenInterval(final Object left, final Object right) {
        assertValue(left, right);

        return new Interval(left, right, false, true);
    }

    public static Interval of(final boolean leftOpen, final boolean rightOpen,
                              final Object leftValue, final Object rightValue) {
        return new Interval(leftValue, rightValue, leftOpen, rightOpen);
    }

    public static Interval of(final ConditionOp leftOp, final ConditionOp rightOp,
                              final Object leftValue, final Object rightValue) {

        if (leftOp.equals(ConditionOp.GT) && rightOp.equals(ConditionOp.LT)) {
            return Interval.openInterval(leftValue, rightValue);
        }

        if (leftOp.equals(ConditionOp.GT) && rightOp.equals(ConditionOp.LTE)) {
            return Interval.openClosedInterval(leftValue, rightValue);
        }

        if (leftOp.equals(ConditionOp.GTE) && rightOp.equals(ConditionOp.LT)) {
            return Interval.closedOpenInterval(leftValue, rightValue);
        }

        if (leftOp.equals(ConditionOp.GTE) && rightOp.equals(ConditionOp.LTE)) {
            return Interval.closedInterval(leftValue, rightValue);
        }

        throw new SwordException(String.format("unexpected ConditionOp:[%s],[%s] for interval", leftOp, rightOp));
    }

    public static Interval openClosedInterval(final Object left, final Object right) {
        assertValue(left, right);

        return new Interval(left, right, true, false);
    }

    public static Interval openInterval(final Object left, final Object right) {
        assertValue(left, right);

        return new Interval(left, right, true, true);
    }

    public boolean contains(final Object value) {
        if (value == null) {
            return false;
        }

        if (this.isEmpty()) {
            return false;
        }

        Object target = Objects2.natureConvert(value);
        Object min = Objects2.natureConvert(left);
        Object max = Objects2.natureConvert(right);

        int leftCompare = Objects2.compareByNaturalOrder(target, min);
        if (leftCompare < 0) {
            return false;
        }

        if (leftCompare == 0 && leftOpen) {
            return false;
        }

        int rightCompare = Objects2.compareByNaturalOrder(target, max);
        if (rightCompare > 0) {
            return false;
        }

        return rightCompare != 0 || !rightOpen;
    }

    public boolean contains(final Interval other) {
        if (other == null) {
            return false;
        }

        if (this.isEmpty()) {
            return false;
        }

        Interval target = other.natureConvertValue();
        Object min = Objects2.natureConvert(left);
        Object max = Objects2.natureConvert(right);

        int leftCompare = Objects2.compareByNaturalOrder(target.left, min);
        if (leftCompare < 0) {
            return false;
        }

        if (leftCompare == 0
                && leftOpen && !other.leftOpen) {
            return false;
        }

        int rightCompare = Objects2.compareByNaturalOrder(target.right, max);
        if (rightCompare > 0) {
            return false;
        }

        return rightCompare != 0
                || !rightOpen || other.rightOpen;
    }

    public Interval intersection(final Interval other) {
        if (this.isEmpty()) {
            return EMPTY;
        }

        if (other == null || other.isEmpty()) {
            return EMPTY;
        }

        if (this.contains(other)) {
            return other;
        }

        if (other.contains(this)) {
            return this;
        }

        Object otherMin = other.getLeft();
        Object otherMax = other.getRight();

        if (this.contains(otherMin) && !this.contains(otherMax)) {
            Interval interval = Interval.of(other.leftOpen, this.rightOpen, other.getLeft(), this.getRight());

            if (interval.isEmpty()) {
                return EMPTY;
            } else {
                return interval;
            }
        }

        if (!this.contains(otherMin) && this.contains(otherMax)) {
            Interval interval = Interval.of(this.leftOpen, other.rightOpen, this.getLeft(), other.getRight());

            if (interval.isEmpty()) {
                return EMPTY;
            } else {
                return interval;
            }
        }

        return EMPTY;
    }

    public boolean isDegenerated() {
        return !leftOpen && !rightOpen && Objects.equals(left, right);
    }

    public boolean isEmpty() {
        return this == EMPTY || (Objects.equals(left, right) && !isDegenerated());
    }

    public Interval natureConvertValue() {
        return new Interval(
                Objects2.natureConvert(left),
                Objects2.natureConvert(right),
                leftOpen,
                rightOpen);
    }

    private static void assertValue(final Object left, final Object right) {
        Asserts.hasArg(left, "parameter 'left' is required");
        Asserts.hasArg(right, "parameter 'right' is required");
        boolean isLess = Objects2.compareByNaturalOrder(
                Objects2.natureConvert(left),
                Objects2.natureConvert(right)
        ) <= 0;
        if (!isLess) {
            throw new IllegalArgumentException("left should <= right");
        }
    }
}
