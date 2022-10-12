package com.fuyouj.sword.scabard;

public class PageUtils {
    public static long getOffset(final Integer pageNum, final Integer pageSize) {
        long skip = 0;
        if (pageNum > 0) {
            skip = ((pageNum - 1) * pageSize);
        }
        return skip;
    }

    public static int getPages(final int pageSize, final long total) {
        int pages = (int) (total / pageSize);
        if (total % pageSize > 0) {
            pages += 1;
        }
        return pages;
    }

    /**
     * 获取总页数
     */
    public static int getTotalPageCount(final long totalCount, final long pageSize) {
        if (totalCount == 0) {
            return 0;
        }

        if (pageSize > 0) {
            long pageCount = totalCount / pageSize;
            long mod = totalCount % pageSize;

            if (mod > 0) {
                return (int) (pageCount + 1);
            } else {
                return (int) pageCount;
            }
        }

        return 0;
    }

    public static int legalizePageNumber(final int pageNumber, final int pageSize, final long total) {
        int pagesCount = getPages(pageSize, total);
        return Math.min(pagesCount, pageNumber);
    }

    public static boolean verifyPageNumber(final Integer pageNumber) {
        return pageNumber != null && pageNumber > 0;
    }

    public static boolean verifyPageSize(final Integer pageSize) {
        return pageSize != null && pageSize > 0;
    }
}
