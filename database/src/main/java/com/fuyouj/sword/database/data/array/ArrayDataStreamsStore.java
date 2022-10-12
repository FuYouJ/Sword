package com.fuyouj.sword.database.data.array;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.fuyouj.sword.database.MemoryAware;
import com.fuyouj.sword.database.data.filter.Filter;
import com.fuyouj.sword.database.data.filter.RowQueryOption;
import com.fuyouj.sword.scabard.Atomic;
import com.fuyouj.sword.scabard.Bytes;

/**
 * 以一个以单一列为单元的数据存储。业务列可能包含多个<b>ColumnarDataStore</b>
 * resourceKey == indicatorId/headerId
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
public interface ArrayDataStreamsStore extends AutoCloseable, MemoryAware {
    void addIndex(String key, String field);

    AddOrUpdateResult addOrUpdateRow(String key, Cells cells);

    List<AddOrUpdateResult> addOrUpdateRows(String key, List<Cells> cells);

    List<AddOrUpdateResult> addOrUpdateRows(String key, Iterator<Cells> rows);

    long addPrimaryValue(String key, String value);

    List<Long> addPrimaryValues(String tableId, List<String> columnData);

    AddResult addRow(String key, Cells cells);

    List<AddResult> addRows(String key, List<Cells> cells);

    List<AddResult> addRows(String key, Iterator<Cells> rows);

    void cleanColumn(String key, String field);

    /**
     * Clear the columnar data store of given key. all data will be cleaned up
     */
    void clear(String key);

    void clearColumn(String key, String field);

    @Override
    void close();

    /**
     * Get size of the given columnar data store
     *
     * @return data size
     */
    long count(String key);

    int count(String key, Filter filter);

    void destroy(String key);

    void dump(String key);

    void fillPrimaryStreamBufferHolyWithNullTo(String key, long index);

    long getBufferSize(String key);

    <Value> IndexedValues<Value> getColumn(String key, String field, List<Long> indexes, Class<Value> tClass);

    <Value> IndexedValues<Value> getColumn(String key, String field, long from, long to, Class<Value> tClass);

    <Value> IndexedValues<Value> getColumn(String key, String field, Class<Value> tClass);

    Map<String, Object> getMemoryDetails();

    default String getReadableBufferSize(String key) {
        return Bytes.toReadableString(getBufferSize(key));
    }

    /**
     * Get datapoint from a given columnar data store
     *
     * @param key key of the datapoint. NotNull
     * @return value of the given key. Nullable
     */
    IndexedMap<String, Object> getRow(String key, long index);

    IndexedMap<String, Object> getRowByPrimaryValue(String key, String primaryValue);

    /**
     * Query a columnar data store by given query option.
     *
     * @param query option
     * @return list of value
     */
    IndexedMaps<String, Object> getRows(String key, Filter query);

    IndexedMaps<String, Object> getRows(String key, long from, long to);

    IndexedMaps<String, Object> getRows(String key);

    IndexedMaps<String, Object> getRows(String key, List<Long> indexes);

    IndexedMaps<String, Object> getRowsByFloor(String key, long from, int limit);

    IndexedMaps<String, Object> getRowsByPrimaryValues(String key, List<String> primaryValues);

    IndexedMaps<String, Object> getRowsByUpper(String key, long to, int limit);

    IndexedMaps<String, Object> getRowsWithPage(String innerKey, int pageNumber, int pageSize);

    <Value> IndexedValue<Value> getValue(String key, String field, long index, Class<Value> tClass);

    IndexedValue<Boolean> getValueAsBoolean(String key, String field, long index);

    IndexedValue<Double> getValueAsDouble(String key, String field, long index);

    IndexedValue<Integer> getValueAsInt(String key, String field, long index);

    IndexedValue<Long> getValueAsLong(String key, String field, long index);

    IndexedValue<String> getValueAsString(String key, String field, long index);

    <Value> void putColumn(String tableId, String columnId, IndexedValues<Value> columnData, Class<Value> tClass);

    boolean putRow(String key, long index, Cells cells);

    <Value> boolean putValue(String key, String field, long index, Value value);

    IndexedMaps<String, Object> query(String key, RowQueryOption option);

    /**
     * 注册一个基于index的key value组合，类似一个table，并指定主键，
     * 如果已经注册则可以直接忽略
     *
     * @param key            table key
     * @param primaryHashKey table的主键，决定了table的每一行
     */
    @Atomic
    ArrayDataStreams register(String key, String primaryHashKey);

    void removeColumn(String key, String field);

    void removeIndex(String key, String field);

    boolean removeRowCells(String key, long index, List<String> fields);

    void removeValue(String key, String field, long index);

    <Value> boolean replaceColumn(String key, String field, IndexedValues<Value> values, Class<Value> tClass);

    List<Long> replacePrimaryColumn(String key, List<String> values);
}

