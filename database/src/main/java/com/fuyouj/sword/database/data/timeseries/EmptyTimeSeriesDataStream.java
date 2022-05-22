package com.fuyouj.sword.database.data.timeseries;

import java.util.List;

import net.thingworks.jarvis.utils.type.Lists2;

public final class EmptyTimeSeriesDataStream<Value> implements TimeSeriesDataStream<Value> {
    public static final EmptyTimeSeriesDataStream EMPTY = new EmptyTimeSeriesDataStream();

    public static <Value> EmptyTimeSeriesDataStream<Value> empty() {
        return (EmptyTimeSeriesDataStream<Value>) EMPTY;
    }

    @Override
    public void clear() {

    }

    @Override
    public void close() {

    }

    @Override
    public int count() {
        return 0;
    }

    @Override
    public long getBufferSize() {
        return 0;
    }

    @Override
    public List<TimePoint<Value>> getByTimeRange(final long from,
                                                 final long to,
                                                 final SampleStrategy sampleStrategy,
                                                 final Class<Value> tClass) {
        return Lists2.staticEmpty();
    }

    @Override
    public List<TimePoint<Value>> getByTimeRange(final long from, final long to, final Class<Value> tClass) {
        return Lists2.staticEmpty();
    }

    @Override
    public TimePoint<Value> getByTimestamp(final long timestamp) {
        return null;
    }

    @Override
    public Class<Value> getDataType() {
        return null;
    }

    @Override
    public TimePoint<Value> getLatest() {
        return null;
    }

    @Override
    public TimePoint<Value> getLatestBefore(final long time) {
        return null;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public boolean isEmpty(final long timestamp) {
        return true;
    }

    @Override
    public void remove(final long timestamp) {

    }

    @Override
    public boolean save(final long timestamp, final Value value) {
        return false;
    }
}
