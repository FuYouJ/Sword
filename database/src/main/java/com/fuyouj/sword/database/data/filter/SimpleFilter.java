package com.fuyouj.sword.database.data.filter;

import java.util.Map;
import java.util.function.BiFunction;

import com.fuyouj.sword.scabard.MapBuilder;
import com.fuyouj.sword.scabard.Comparators;
import com.fuyouj.sword.scabard.FormulaStringConst;
import com.fuyouj.sword.scabard.Maps2;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@EqualsAndHashCode(callSuper = false)
public class SimpleFilter implements ValueFilter {
    private static final Map<ConditionOp, BiFunction<Object, Object, Boolean>> OP_HANDLER;

    static {
        OP_HANDLER = MapBuilder.<ConditionOp, BiFunction<Object, Object, Boolean>>instance()
                .entry(ConditionOp.EQ, Comparators::equal)
                .entry(ConditionOp.NE, Comparators::notEqual)
                .entry(ConditionOp.LT, Comparators::lessThan)
                .entry(ConditionOp.LTE, Comparators::lessThanEqual)
                .entry(ConditionOp.GT, Comparators::greaterThan)
                .entry(ConditionOp.GTE, Comparators::greaterThanEqual)
                .entry(ConditionOp.MATCH, Comparators::matchWith)
                .entry(ConditionOp.UNMATCH, Comparators::unMatchWith)
                .build();
    }

    @Getter
    private final String referenceKey;
    @Getter
    private final ConditionOp op;
    @Getter
    private final Object value;

    protected SimpleFilter(final String referenceKey, final ConditionOp op, final Object value) {
        this.referenceKey = referenceKey;
        this.op = op;
        this.value = value;
    }

    public static SimpleFilter of(final String referenceKey, final ConditionOp op, final Object value) {
        return new SimpleFilter(referenceKey, op, value);
    }

    @Override
    public String asFormula() {
        String resolvedValue = null;

        if (value != null) {
            resolvedValue = value.toString();
        } else {
            resolvedValue = "";
        }

        return referenceKey + " " + op.getFormulaOp() + " " + FormulaStringConst.fromString(resolvedValue);
    }

    @Override
    public Map<String, Object> asPlainObj() {
        return MapBuilder.builder(
                        referenceKey, (Object) MapBuilder.builder(op.getOp(), value).build())
                .build();
    }

    public boolean isMatchFilter() {
        return op.equals(ConditionOp.MATCH) || op.equals(ConditionOp.UNMATCH);
    }

    @Override
    public boolean match(final Map<String, Object> entry) {
        Object actual = null;

        if (!Maps2.isNullOrEmpty(entry)) {
            actual = entry.get(referenceKey);
        }

        if (OP_HANDLER.containsKey(op)) {
            return OP_HANDLER.get(op).apply(actual, value);
        }

        return false;
    }

}
