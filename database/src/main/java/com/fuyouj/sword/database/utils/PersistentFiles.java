package com.fuyouj.sword.database.utils;

import java.util.Arrays;
import java.util.function.Predicate;

public class PersistentFiles {

    public static String[] splitFileName(final String fileName, final String separator,
                                         final String magicStr, final int magicIndex) {
        final String[] split = fileName.split(separator);
        if (split == null) {
            return null;
        }

        final int magicAt = Arrays.asList(split).indexOf(magicStr);
        if (magicAt < magicIndex) {
            return null;
        }

        if (magicAt == magicIndex) {
            return split;
        }

        int offSet = magicAt - magicIndex;
        String[] resolved = new String[split.length - offSet];

        String[] keyAsArray = new String[offSet + 1];
        System.arraycopy(split, 0, keyAsArray, 0, keyAsArray.length);

        resolved[0] = String.join(".", keyAsArray);
        System.arraycopy(split, magicAt, resolved, magicIndex, resolved.length - magicIndex);

        return resolved;
    }

    public static boolean validateFilePart(final String[] split, final int pos, final boolean optional,
                                           final Predicate<String> rule) {
        if (split.length <= pos) {
            return optional;
        }

        return rule.test(split[pos]);
    }

}
