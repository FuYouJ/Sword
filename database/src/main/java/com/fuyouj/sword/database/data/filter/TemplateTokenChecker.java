package com.fuyouj.sword.database.data.filter;

import java.util.regex.Pattern;

public abstract class TemplateTokenChecker {
    private static final Pattern REGEX_FILTER_TEMPLATE_PATTERN = Pattern.compile("\\{+.*}");

    public boolean isTemplateToken(final String text) {
        return REGEX_FILTER_TEMPLATE_PATTERN.matcher(text).find();
    }

    public abstract Object replace(String text);
}
