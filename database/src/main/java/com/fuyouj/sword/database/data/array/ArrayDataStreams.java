package com.fuyouj.sword.database.data.array;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.fuyouj.sword.database.data.QueryOption;
import com.fuyouj.sword.database.data.array.index.ArrayDataStreamIndex;
import com.fuyouj.sword.database.data.filter.Filter;
import com.fuyouj.sword.database.data.filter.RowQueryOption;
import com.fuyouj.sword.scabard.Bytes;

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

    ArrayDataStreamIndex addIndex(String key);

    long addPrimaryValue(String value);

    List<Long> addPrimaryValues(List<String> values);

    long addRow(Cells cells);

    <Value> long addValue(String key, Value value);

    void cleanColumn(String key);

    void clear();

    /**
     * Clear the columnar data store of given key. all data will be cleaned up
     */
    void clearColumn(String key);

    @Override
    void close();

    boolean containsKeys(String key);

    int count();

    /**
     * Get size of the given columnar data store
     *
     * @return data size
     */
    long count(String key);

    long count(Filter filter);

    boolean dataTypeAligned(Cells cells);

    void fillBufferHolyWithNullTo(String key, long index);

    ArrayDataStreamIndex findIndex(String key);

    String generatePrimaryValue();

    String generatePrimaryValue(long index);

    long getBufferCount();

    long getBufferSize();

    <Value> IndexedValues<Value> getColumn(String key, List<Long> indexes, Class<Value> tClass);

    <Value> IndexedValues<Value> getColumn(String field, long from, long to, Class<Value> tClass);

    <Value> IndexedValues<Value> getColumn(String field, Class<Value> tClass);

    long getIndexByPrimaryValue(String pkValue);

    Set<String> getKeys();

    Map<String, Object> getMemoryDetails();

    String getPrimaryKey();

    ArrayDataStream<String> getPrimaryStream();

    default String getReadableBufferSize() {
        return Bytes.toReadableString(getBufferSize());
    }

    IndexedMap<String, Object> getRow(long index);

    IndexedMap<String, Object> getRowByPrimaryValue(String primaryValue);

    IndexedMaps<String, Object> getRows();

    IndexedMaps<String, Object> getRows(long from, long to);

    IndexedMaps<String, Object> getRows(List<Long> indexes);

    IndexedMaps<String, Object> getRowsByFloor(long from, int limit);

    IndexedMaps<String, Object> getRowsByPrimaryValues(List<String> primaryValues);

    IndexedMaps<String, Object> getRowsByUpper(long to, int limit);

    IndexedMaps<String, Object> getRowsWithPage(int page, int size);

    IndexedMap<String, Object> getSubRow(long index, Set<String> keys);

    IndexedMaps<String, Object> getSubRows(Set<String> keys);

    IndexedMaps<String, Object> getSubRows(List<Long> indexes, Set<String> keys);

    /**
     * Get datapoint from a given columnar data store
     *
     * @param key key of the datapoint. NotNull
     * @return value of the given key. Nullable
     */
    <Value> IndexedValue<Value> getValue(String key, long index, Class<Value> tClass);

    IndexedValue<Boolean> getValueAsBoolean(String key, long index);

    IndexedValue<Double> getValueAsDouble(String key, long index);

    IndexedValue<Integer> getValueAsInt(String key, long index);

    IndexedValue<Long> getValueAsLong(String key, long index);

    IndexedValue<String> getValueAsString(String key, long index);

    boolean isPrimaryIndexEmpty(long index);

    boolean isPrimaryStreamValueEmpty(Cells cells);

    default boolean isRowEmpty() {
        return this.count() <= 0;
    }

    default boolean isStreamsValueValid(Cells cells) {
        return cells != null && !isPrimaryStreamValueEmpty(cells) && dataTypeAligned(cells);
    }

    <Value> boolean putColumn(String key, IndexedValues<Value> values, Class<Value> tClass);

    boolean putRow(long index, Cells value);

    /**
     * Add new datapoint, update datapoint to a given columnar data store
     *
     * @param key   key of the datapoint. NotNull
     * @param value value of the datapoint. Can be nullable
     */
    <Value> boolean putValue(String key, long index, Value value);

    /**
     * Query a columnar data store by given query option.
     *
     * @param query option
     * @return list of value
     */
    <Value> QueryResult<Value> query(String key, QueryOption<Value> query);

    IndexedMaps<String, Object> query(RowQueryOption query);

    void removeColumn(String key);

    void removeIndex(String key);

    @Deprecated
    boolean removeRow(long index);

    boolean removeRowCells(long index, List<String> keys);

    /**
     * Remove datapoint to a given columnar data store
     *
     * @param key key of the datapoint. NotNull
     */
    void removeValue(String key, long index);

    List<Long> replacePrimaryColumn(List<String> values);

    IndexedMaps<String, Object> reverseFullIndexMaps();

    IndexedMaps<String, Object> reverseFullIndexMaps(Set<String> keys);

    <T> ArrayDataStream<T> selectOrCreateStream(String key, Class<T> tClass);

    <T> ArrayDataStream<T> selectStream(String key, Class<T> tClass);
}
