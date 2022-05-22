package com.fuyouj.sword.database.data.timeseries;

import lombok.Getter;

/**
 * 时序数据的采用策略对象，包括三种要素：采样间隔， 聚合类型，补值策略
 */
@Getter
public final class SampleStrategy {
    public static final SampleStrategy PER_MINUTE_LAST_LAST =
            new SampleStrategy(60, AggregateType.Last, FillInType.LastValue);

    public static final SampleStrategy PER_HOUR_LAST_LAST =
            new SampleStrategy(60 * 60, AggregateType.Last, FillInType.LastValue);

    public static final SampleStrategy PER_DAY_LAST_LAST =
            new SampleStrategy(60 * 60 * 24, AggregateType.Last, FillInType.LastValue);

    private final int intervalInSeconds;
    private final AggregateType aggregateType;
    private final FillInType fillInType;

    public SampleStrategy(final int intervalInSeconds, final AggregateType aggregateType, final FillInType fillInType) {
        this.intervalInSeconds = intervalInSeconds;
        this.aggregateType = aggregateType;
        this.fillInType = fillInType;
    }
}
