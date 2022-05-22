package com.fuyouj.sword.database.data.array;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.fuyouj.sword.database.data.filter.Filter;
import com.fuyouj.sword.scabard.Atomic;

/**
 * 以一个以单一列为单元的数据存储。业务列可能包含多个<b>ColumnarDataStore</b>
 * resourceKey == ID
 * resourceKey ->
 * 1. unsorted ArrayDataContainer
 * 2. hashed ArrayDataContainer
 * 3. b+ ArrayDataContainer
 * 4. 数字化的ArrayDataContainer
 * 5. 日期化的ArrayDataContainer
 * <p>
 * <p>
 * <p>
 * ArrayDataStore:
 * 1. 大数量，小内存膨胀
 * 2. 理论基本占据所有内存量
 * 4. Java堆外存储
 */
public interface KeyedDataStreams extends AutoCloseable {
    int add(String key, Map<String, Object> valueMap);

    <T> void addStreamValue(String key, String field, List<T> values, Class<T> tClass);

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

    /**
     * Get datapoint from a given columnar data store
     *
     * @param key key of the datapoint. NotNull
     * @return value of the given key. Nullable
     */
    Map<String, Object> get(String key, int index);

    <Value> Value get(String key, String field, int index, Class<Value> tClass);

    Boolean getAsBoolean(String key, String field, int index);

    Double getAsDouble(String key, String field, int index);

    Integer getAsInt(String key, String field, int index);

    Long getAsLong(String key, String field, int index);

    String getAsString(String key, String field, int index);

    Map<String, ArrayDataStream> getStreams(String key);

    void put(int index, Map<String, Object> valueMap);

    <Value> boolean put(String key, String field, int index, Value value);

    <Value> void put(String key, String field, int index, List<Value> value);

    /**
     * Query a columnar data store by given query option.
     *
     * @param query option
     * @return list of value
     */
    ResultIterator query(String key, Filter query);

    /**
     * 注册一个基于index的key value组合，类似一个table，并指定主键，
     * 如果已经注册则可以直接忽略
     *
     * @param key            table key
     * @param primaryHashKey table的主键，决定了table的每一行
     */
    @Atomic
    void register(String key, String primaryHashKey);

    /**
     * Remove datapoint to a given columnar data store
     *
     * @param key key of the datapoint. NotNull
     */
    boolean remove(String key, int index);

    void remove(String realKey, String field, int index);

    <T> void removeStream(String key, String field, Class<T> tClass);

    <T> ArrayDataStream<T> selectOrCreateStream(String key, String field, Class<T> tClass);

    <T> Optional<ArrayDataStream<T>> selectStream(String key, String field, Class<T> tClass);
}
