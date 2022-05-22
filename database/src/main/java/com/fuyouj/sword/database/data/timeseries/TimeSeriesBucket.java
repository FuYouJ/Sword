package com.fuyouj.sword.database.data.timeseries;

import java.util.List;

import com.fuyouj.sword.database.data.DataStream;

public interface TimeSeriesBucket<Value> extends DataStream {
    List<TimePoint<Value>> asTimePoints();

    int getDay();

    TimePoint<Value> getLatest();

    TimePoint<Value> getLatestBefore(int time);

    TimePoint<Value> getValueBeforeTimeExclusively(int time);

    TimePoint<Value> getValueByTime(int time);

    List<TimePoint<Value>> getValuesAfter(int from);

    List<TimePoint<Value>> getValuesBefore(int to);

    List<TimePoint<Value>> getValuesBetween(int from, int to);

    boolean isEmpty(int time);

    void removeByTime(int time);

    boolean save(int time, Value value);
}
