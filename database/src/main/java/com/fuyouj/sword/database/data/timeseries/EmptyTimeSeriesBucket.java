package com.fuyouj.sword.database.data.timeseries;

import java.util.List;

import com.fuyouj.sword.scabard.Lists2;

public class EmptyTimeSeriesBucket<Value> implements TimeSeriesBucket<Value> {
    public static final EmptyTimeSeriesBucket EMPTY = new EmptyTimeSeriesBucket();

    private EmptyTimeSeriesBucket() {
    }

    @SuppressWarnings("unchecked")
    public static <Value> TimeSeriesBucket<Value> empty() {
        return (TimeSeriesBucket<Value>) EMPTY;
    }

    @Override
    public List<TimePoint<Value>> asTimePoints() {
        return Lists2.staticEmpty();
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
    public Class<?> getDataType() {
        return Void.class;
    }

    @Override
    public int getDay() {
        return -1;
    }

    @Override
    public TimePoint<Value> getLatest() {
        return null;
    }

    @Override
    public TimePoint<Value> getLatestBefore(final int time) {
        return null;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public TimePoint<Value> getValueBeforeTimeExclusively(final int time) {
        return null;
    }

    @Override
    public TimePoint<Value> getValueByTime(final int time) {
        return null;
    }

    @Override
    public List<TimePoint<Value>> getValuesAfter(final int from) {
        return Lists2.staticEmpty();
    }

    @Override
    public List<TimePoint<Value>> getValuesBefore(final int to) {
        return Lists2.staticEmpty();
    }

    @Override
    public List<TimePoint<Value>> getValuesBetween(final int from, final int to) {
        return Lists2.staticEmpty();
    }

    @Override
    public boolean isEmpty(final int time) {
        return true;
    }

    @Override
    public void removeByTime(final int time) {

    }

    @Override
    public boolean save(final int time, final Value value) {
        return false;
    }
}
