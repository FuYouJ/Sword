package com.fuyouj.sword.database.data.timeseries;

import java.util.List;
import java.util.Map;

import com.fuyouj.sword.database.MemoryAware;

/**
 * TimeSeriesDataStore 是时序数据持久化最外成的结构，所有的时序数据的管理均通过该接口实现；
 * 它的内部是一个介于Key的DataContainer hash结构，每个Key代表一个TimeSeriesContainer；
 * 而每一个TimeSeriesContainer内部又包含若干个TimeSeriesBucket，每个bucket代表一天的数据；
 * Container，Bucket内部均采用append only的方式上报数据，最新的数据append在后。
 */
public interface TimeSeriesDataStreamsStore extends AutoCloseable, MemoryAware {
    /**
     * Clear 清除该key所有的数据
     *
     * @param key 时序数据ID
     */
    void clear(String key);

    @Override
    void close();

    /**
     * Delete 删除该key，并清除所有数据
     *
     * @param key 时序数据ID
     */
    void delete(String key);

    <Value> TimePoint<Value> get(String key, long timestamp, Class<Value> tClass);

    TimePoint<Boolean> getAsBoolean(String key, long timestamp);

    TimePoint<Double> getAsDouble(String key, long timestamp);

    TimePoint<Integer> getAsInt(String key, long timestamp);

    TimePoint<String> getAsString(String key, long timestamp);

    /**
     * Get time points by given range and class
     *
     * @param key     唯一ID
     * @param from    开始时间
     * @param to      结束时间
     * @param tClass  数据类型
     * @param <Value> 类型泛型
     * @return 一组按时间排序的time points
     */
    <Value> List<TimePoint<Value>> getByTimeRange(String key, long from, long to, Class<Value> tClass);

    /**
     * Get time points by given range and class
     *
     * @param key            唯一ID
     * @param from           开始时间
     * @param to             结束时间
     * @param sampleStrategy 采样策略
     * @param tClass         数据类型
     * @param <Value>        类型泛型
     * @return 一组按时间排序的time points
     */
    <Value> List<TimePoint<Value>> getByTimeRange(String key,
                                                  long from,
                                                  long to,
                                                  SampleStrategy sampleStrategy,
                                                  Class<Value> tClass);

    <Value> TimePoint<Value> getLatest(String key, Class<Value> tClass);

    TimePoint<Boolean> getLatestAsBoolean(String key);

    TimePoint<Double> getLatestAsDouble(String key);

    TimePoint<Integer> getLatestAsInt(String key);

    TimePoint<Long> getLatestAsLong(String key);

    TimePoint<String> getLatestAsString(String key);

    <Value> TimePoint<Value> getLatestBefore(String key, long timestamp, Class<Value> tClass);

    TimePoint<Boolean> getLatestBeforeAsBoolean(String key, long timestamp);

    TimePoint<Double> getLatestBeforeAsDouble(String key, long timestamp);

    TimePoint<Integer> getLatestBeforeAsInt(String key, long timestamp);

    TimePoint<Long> getLatestBeforeAsLong(String key, long timestamp);

    TimePoint<String> getLatestBeforeAsString(String key, long timestamp);

    Map<String, String> getReadableMemoryLayout();

    boolean isEmpty(String key, long timestamp);

    void remove(String key, long timestamp);

    <Value> boolean save(String key, long timestamp, Value value);
}
