package com.fuyouj.sword.database.data.filter;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import com.fuyouj.sword.scabard.Strings;

import lombok.Getter;

public enum FilterOp {
    AND("and!"), OR("or!");

    @Getter
    private String name;

    FilterOp(final String name) {
        this.name = name;
    }

    public static Optional<FilterOp> fromString(final String op) {
        if (Strings.isBlank(op)) {
            return Optional.empty();
        }

        return Arrays.stream(values()).filter(value -> value.getName().equalsIgnoreCase(op)).findFirst();
    }

    public static boolean isOp(final String op) {
        return fromString(op).isPresent();
    }

    public Filter of(final List<Filter> filters) {
        if (this == FilterOp.AND) {
            return AndFilter.of(filters);
        }

        return OrFilter.of(filters);
    }
}
