package com.fuyouj.sword.database.data.map;

import java.util.List;
import java.util.Map;

import com.fuyouj.sword.database.data.array.AddOrUpdateResult;
import com.fuyouj.sword.database.data.array.ResultIterator;
import com.fuyouj.sword.database.data.array.SimpleIndexedValue;
import com.fuyouj.sword.database.data.filter.Filter;
import com.fuyouj.sword.scabard.Atomic;

public interface KeyedMapDataStreams<Key> extends AutoCloseable {
    long add(String streamKey, Key valueKey, Map<String, Object> valueMap);

    <T> void addAllStreamValue(String key, String field, List<KeyedValue<Key, T>> keyedValues, Class<T> tClass);

    AddOrUpdateResult addOrUpdate(String key, Key valueKey, Map<String, Object> valueMap);

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

    Map<String, Object> get(String streamKey, Key valueKey);

    <Value> Value get(String key, String field, Key valueKey, Class<Value> tClass);

    Boolean getAsBoolean(String key, String field, Key valueKey);

    Double getAsDouble(String key, String field, Key valueKey);

    Integer getAsInt(String key, String field, Key valueKey);

    Long getAsLong(String key, String field, Key valueKey);

    String getAsString(String key, String field, Key valueKey);

    <Value> List<SimpleIndexedValue<Value>> getStreamValues(String key,
                                                            String field,
                                                            Key valueKeyFrom,
                                                            Key valueKeyTo,
                                                            Class<Value> tClass);

    <Value> List<SimpleIndexedValue<Value>> getStreamValues(String key,
                                                            String field,
                                                            Key valueKeyFrom,
                                                            int total,
                                                            Class<Value> tClass);

    <Value> List<SimpleIndexedValue<Value>> getStreamValues(String key, String field, Class<Value> tClass);

    <Value> boolean put(String key, String field, Key valueKey, Value value);

    boolean put(String realKey, Key valueKey, Map<String, Object> rowData);

    <Value> void putStreamValue(String key,
                                String field,
                                List<KeyedValue<Key, Value>> keyedValues,
                                Class<Value> tClass);

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
    boolean remove(String key, Key valueKey);

    void remove(String key, String field, Key valueKey);

    <T> void removeStream(String key, String field, Class<T> tClass);

}
