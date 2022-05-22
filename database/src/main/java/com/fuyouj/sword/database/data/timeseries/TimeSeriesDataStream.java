package com.fuyouj.sword.database.data.timeseries;

import java.util.List;

import com.fuyouj.sword.database.data.DataStream;

public interface TimeSeriesDataStream<Value> extends DataStream {
    List<TimePoint<Value>> getByTimeRange(long from, long to, SampleStrategy sampleStrategy, Class<Value> tClass);

    List<TimePoint<Value>> getByTimeRange(long from, long to, Class<Value> tClass);

    TimePoint<Value> getByTimestamp(long timestamp);

    TimePoint<Value> getLatest();

    TimePoint<Value> getLatestBefore(long time);

    String getName();

    boolean isEmpty(long timestamp);

    void remove(long timestamp);

    boolean save(long timestamp, Value value);
}
