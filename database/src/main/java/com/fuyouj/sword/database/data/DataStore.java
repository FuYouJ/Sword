package com.fuyouj.sword.database.data;

import java.util.Map;

/**
 * 1. 大数量，小膨胀
 * 2. 理论基本占据所有内存量
 * 3. 查询
 * 4. 堆外
 */
public interface DataStore {
    /**
     * Clear the columnar data store of given key. all data will be cleaned up
     *
     * @param columnarKey key of the columnar data store
     */
    <Key, Value> void clear(String columnarKey);

    /**
     * Get size of the given columnar data store
     *
     * @return data size
     */
    <Key, Value> long count(String columnarKey);

    /**
     * Get datapoint from a given columnar data store
     *
     * @param columnarKey the key of a given columnar data store. NotNull
     * @param key         key of the datapoint. NotNull
     * @return value of the given key. Nullable
     */
    <Key, Value> Value get(String columnarKey, Key key);

    /**
     * Get the latest value (the biggest key) of columnar data store.
     *
     * @param columnarKey key of the columnar data
     * @return latest value. Nullable
     */
    <Key, Value> Value latest(String columnarKey);

    /**
     * Add new datapoint, update datapoint to a given columnar data store
     *
     * @param columnarKey the key of a given columnar data store. NotNull
     * @param key         key of the datapoint. NotNull
     * @param value       value of the datapoint. Can be nullable
     */
    <Key, Value> void put(String columnarKey, Key key, Value value);

    /**
     * Query a columnar data store by given query option.
     *
     * @param columnarKey the key of a given columnar data store. NotNull
     * @param queryOption option of query
     * @return list of value
     */
    <Key, Value> Map<Key, Value> query(String columnarKey, QueryOption<Value> queryOption);

    /**
     * Remove datapoint to a given columnar data store
     *
     * @param columnarKey the key of a given columnar data store. NotNull
     * @param key         key of the datapoint. NotNull
     */
    <Key, Value> void remove(String columnarKey, Key key);

}
