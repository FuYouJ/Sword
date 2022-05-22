package com.fuyouj.sword.database.utils;

import java.util.zip.CRC32;

public class Bytes {
    public static int crc32(final byte[] data) {
        if (data == null || data.length == 0) {
            return 0;
        }

        CRC32 crc32 = new CRC32();
        crc32.update(data);
        return (int) crc32.getValue();
    }

    public static boolean equal(final byte[] left, final byte[] right) {
        if (left == null || right == null) {
            return false;
        }

        if (left.length != right.length) {
            return false;
        }

        for (int index = 0; index < left.length; index++) {
            if (left[index] != right[index]) {
                return false;
            }
        }

        return true;
    }
}
