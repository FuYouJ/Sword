package com.fuyouj.sword.database.data.timeseries;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.NavigableMap;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

import com.fuyouj.sword.scabard.Lists2;
import com.fuyouj.sword.scabard.PageUtils;

import lombok.Getter;

/**
 * 这里的补值仅处理后补值，与TSDB一致，前补值由外部按需处理
 */
public class DownSampler {

    public <T> List<TimePoint<T>> downSampling(final long from,
                                               final long to,
                                               final SampleStrategy sampling,
                                               final List<TimePoint<T>> points) {
        if (Lists2.isNullOrEmpty(points)) {
            return Lists2.staticEmpty();
        }

        if (Lists2.size(points) > 1) {
            points.sort(Comparator.naturalOrder());
        }

        long floorFrom = this.floorToMinute(from);
        long ceilTo = this.ceilToMinute(to) + 1;
        long intervalMills = TimeUnit.SECONDS.toMillis(sampling.getIntervalInSeconds());

        List<TimePoint<T>> downSampled = this.doAggregate(
                this.splitTimeSegment(floorFrom, ceilTo, intervalMills, asTreeMap(points))
        );

        FillInType fillInType = sampling.getFillInType();
        if (fillInType == FillInType.Null) {
            return downSampled;
        }

        return new ArrayList<>(
                this.fill(new LinkedList<>(downSampled), ceilTo, intervalMills)
        );
    }

    private <T> TreeMap<Long, TimePoint<T>> asTreeMap(final List<TimePoint<T>> points) {
        TreeMap<Long, TimePoint<T>> treeMap = new TreeMap<>();

        points.forEach(p -> treeMap.put(p.getTimestamp(), p));

        return treeMap;
    }

    private int calculateSegCount(final long from, final long to, final long intervalMills) {
        return PageUtils.getTotalPageCount(to - from, intervalMills);
    }

    private long ceilToMinute(final long timestamp) {
        long intervalMills = TimeUnit.MINUTES.toMillis(1);

        long mod = timestamp % intervalMills;
        if (mod == 0) {
            return timestamp;
        }

        return timestamp + intervalMills - mod;
    }

    private <T> List<TimePoint<T>> doAggregate(final List<Seg<T>> segments) {
        segments.forEach(Seg::doAggregate);
        return Lists2.mapNotNull(segments, Seg::getAggregated);
    }

    private <T> List<TimePoint<T>> fill(final List<TimePoint<T>> points,
                                        final long to,
                                        final long intervalMills) {
        for (int i = 0; i < points.size() - 1; i++) {
            TimePoint<T> p1 = points.get(i);
            TimePoint<T> p2 = points.get(i + 1);

            if (p2.getTimestamp() - p1.getTimestamp() == intervalMills) {
                continue;
            }

            long ts = p1.getTimestamp() + intervalMills;
            T value = p1.getValue();

            points.add(i + 1, new TimePoint<>(ts, value));
        }

        TimePoint<T> last = Lists2.last(points).orElse(null);
        while (last != null && last.getTimestamp() < to) {
            long ts = last.getTimestamp() + intervalMills;
            T value = last.getValue();

            last = new TimePoint<>(ts, value);
            points.add(last);

            if (ts >= System.currentTimeMillis()) {
                break;
            }
        }

        return points;
    }

    private long floorToMinute(final long timestamp) {
        long intervalMills = TimeUnit.MINUTES.toMillis(1);

        return timestamp - timestamp % intervalMills;
    }

    private <T> List<Seg<T>> splitTimeSegment(final long from, final long to,
                                              final long intervalMills,
                                              final TreeMap<Long, TimePoint<T>> points) {
        List<Seg<T>> segments = Lists2.fixed(this.calculateSegCount(from, to, intervalMills));

        Seg<T> seg = Seg.of(from, intervalMills);
        seg.extractPoints(points);

        segments.add(seg);

        while (seg.getEnd() < to) {
            seg = seg.next(intervalMills);
            seg.extractPoints(points);

            segments.add(seg);
        }

        return segments;
    }

    /**
     * [start,end)
     */
    @Getter
    static class Seg<T> {
        private final long start;
        private final long end;

        private List<TimePoint<T>> points;
        private TimePoint<T> aggregated;

        Seg(final long start, final long end) {
            this.start = start;
            this.end = end;
        }

        @SuppressWarnings("OptionalGetWithoutIsPresent")
        void doAggregate() {
            if (Lists2.isNullOrEmpty(points)) {
                return;
            }

            points.sort(Comparator.naturalOrder());
            TimePoint<T> last = Lists2.last(points).get();

            this.aggregated = new TimePoint<>(this.start, last.getValue());
        }

        void extractPoints(final TreeMap<Long, TimePoint<T>> points) {
            this.points = this.search(points, start, true, end, false);
        }

        static <T> Seg<T> of(final long start, final long interval) {
            return new Seg<>(start, start + interval);
        }

        private Seg<T> next(final long intervalMills) {
            return Seg.of(this.getEnd(), intervalMills);
        }

        private List<TimePoint<T>> search(final TreeMap<Long, TimePoint<T>> points,
                                          final long from, final boolean fromInclusive,
                                          final long to, final boolean toInclusive) {
            NavigableMap<Long, TimePoint<T>> subPoints =
                    points.subMap(from, fromInclusive, to, toInclusive);
            if (!subPoints.isEmpty()) {
                return new ArrayList<>(subPoints.values());
            } else {
                return Collections.emptyList();
            }
        }
    }

}
