package com.fuyouj.sword.database.data.array;

import java.util.Map;

import com.fuyouj.sword.database.data.QueryOption;

/**
 * 以一个以单一列为单元的数据存储。业务列可能包含多个ColumnarDataStore
 * resourceKey == ID
 * resourceKey ->
 * 1. unsorted ArrayDataContainer
 * 2. hashed ArrayDataContainer
 * 3. b+ ArrayDataContainer
 * 4. 数字化的ArrayDataContainer
 * 5. 日期化的ArrayDataContainer
 * ArrayDataStore:
 * 1. 大数量，小内存膨胀
 * 2. 理论基本占据所有内存量
 * 4. Java堆外存储
 */
public interface ArrayDataStreams extends AutoCloseable {
    <Value> int add(String key, Value value);

    /**
     * Clear the columnar data store of given key. all data will be cleaned up
     */
    void clear(String key);

    @Override
    void close();

    /**
     * Get size of the given columnar data store
     *
     * @return data size
     */
    long count(String key);

    void fillBufferHolyWithNullTo(String key, int index);

    /**
     * Get datapoint from a given columnar data store
     *
     * @param key key of the datapoint. NotNull
     * @return value of the given key. Nullable
     */
    <Value> Value get(String key, int index, Class<Value> tClass);

    Map<String,ArrayDataStream> getAllStream();

    Boolean getAsBoolean(String key, int index);

    Double getAsDouble(String key, int index);

    Integer getAsInt(String key, int index);

    Long getAsLong(String key, int index);

    String getAsString(String key, int index);

    /**
     * Add new datapoint, update datapoint to a given columnar data store
     *
     * @param key   key of the datapoint. NotNull
     * @param value value of the datapoint. Can be nullable
     */
    <Value> boolean put(String key, int index, Value value);

    /**
     * Query a columnar data store by given query option.
     *
     * @param query option
     * @return list of value
     */
    <Value> QueryResult<Value> query(String key, QueryOption<Value> query);

    /**
     * Remove datapoint to a given columnar data store
     *
     * @param key key of the datapoint. NotNull
     */
    void remove(String key, int index);

    <T> ArrayDataStream<T> selectOrCreateStream(String key, Class<T> tClass);

    <T> ArrayDataStream<T> selectStream(String key, Class<T> tClass);
}
