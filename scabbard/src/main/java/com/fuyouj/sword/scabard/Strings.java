package com.fuyouj.sword.scabard;

import java.util.List;

public class Strings {
    public static String join(final List<String> stringList, final String separator) {
        if (stringList == null || stringList.isEmpty()) {
            return "";
        }

        StringBuilder stringBuilder = new StringBuilder();

        final int size = stringList.size();

        for (int index = 0; index < size; index++) {
            stringBuilder.append(stringList.get(index));

            if (index < size - 1) {
                stringBuilder.append(separator);
            }
        }

        return stringBuilder.toString();
    }
}
