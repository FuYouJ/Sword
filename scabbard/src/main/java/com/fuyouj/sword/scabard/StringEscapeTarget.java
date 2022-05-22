package com.fuyouj.sword.scabard;

public class StringEscapeTarget {
    private final char toBeEscaped;
    private final String escapePrefix = "\\";

    public StringEscapeTarget(final char toBeEscaped) {
        this.toBeEscaped = toBeEscaped;
    }

    public String getEscaped() {
        return escapePrefix + escapePrefix + toBeEscaped;
    }

}
