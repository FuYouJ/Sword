package com.fuyouj.sword.scabard;

public class FormulaStringConst {
    private final String text;

    public FormulaStringConst(final String text) {
        this.text = text;
    }

    public static String fromAny(final Object object) {
        if (object == null) {
            return "";
        }

        return "\"" + Objects2.asString(object).replaceAll("\"", "\"\"") + "\"";
    }

    public static String fromString(final String str) {
        if (str == null) {
            return "";
        }

        return "\"" + str.replaceAll("\"", "\"\"") + "\"";
    }

    @Override
    public String toString() {
        if (this.text == null) {
            return null;
        }

        return text.substring(1, text.length() - 1).replaceAll("\"\"", "\"");
    }
}

