package com.fuyouj.sword.database.data.filter;

import java.util.Arrays;
import java.util.Optional;

import com.fuyouj.sword.scabard.Strings;

import lombok.Getter;

public enum ConditionOp {
    EQ("eq!", "="),

    BETWEEN_AND("between_and", "between_and"),

    GT("gt!", ">"),
    GTE("gte!", ">="),

    LT("lt!", "<"),
    LTE("lte!", "<="),

    NE("ne!", "<>"),
    MATCH("match!", "like"),
    UNMATCH("unmatch!", "not like"),

    DYNAMIC("dynamic!", ""),
    DATE("date!", "");

    @Getter
    private String op;
    @Getter
    private String formulaOp;

    ConditionOp(final String op, final String formulaOp) {
        this.op = op;
        this.formulaOp = formulaOp;
    }

    public static Optional<ConditionOp> fromString(final String op) {
        if (Strings.isBlank(op)) {
            return Optional.empty();
        }

        return Arrays.stream(values())
                .filter(value -> value.name().equalsIgnoreCase(op) || value.op.equalsIgnoreCase(op))
                .findFirst();
    }
}
