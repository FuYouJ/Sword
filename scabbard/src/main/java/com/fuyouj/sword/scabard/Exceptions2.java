package com.fuyouj.sword.scabard;

import java.util.List;

public class Exceptions2 {
    public static final int MAX_STACK = 3;

    public static String extractMessage(final Throwable throwable) {
        List<String> stack = Lists2.newList();
        doExtractMessage(throwable, stack);

        return Strings.join(stack, " caused by ");
    }

    private static void doExtractMessage(final Throwable throwable, final List<String> stack) {
        if (throwable == null || stack.size() >= MAX_STACK) {
            return;
        }

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(throwable.getClass().getName());

        if (throwable.getMessage() != null) {
            stringBuilder.append("[").append(throwable.getMessage()).append("]");
            stack.add(stringBuilder.toString());
            return;
        }

        stringBuilder.append("[not provided]");
        stack.add(stringBuilder.toString());
        doExtractMessage(throwable.getCause(), stack);
    }
}
