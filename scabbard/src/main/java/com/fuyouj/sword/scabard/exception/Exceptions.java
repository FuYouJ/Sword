package com.fuyouj.sword.scabard.exception;

import java.util.List;

import com.fuyouj.sword.scabard.Lists2;
import com.fuyouj.sword.scabard.Strings;

public class Exceptions {
    public static final int MAX_STACK = 3;

    public static String extractMessage(final Throwable throwable) {
        List<String> stack = Lists2.staticEmpty();
        doExtractMessage(throwable, stack, MAX_STACK);

        return Strings.join(stack, " caused by ");
    }

    public static String extractMessage(final Throwable throwable, final int maxStack) {
        List<String> stack = Lists2.staticEmpty();
        doExtractMessage(throwable, stack, maxStack);

        return Strings.join(stack, " caused by ");
    }

    private static void doExtractMessage(final Throwable throwable, final List<String> stack, final int maxStack) {
        if (throwable == null || stack.size() >= maxStack) {
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
        doExtractMessage(throwable.getCause(), stack, maxStack);
    }
}
