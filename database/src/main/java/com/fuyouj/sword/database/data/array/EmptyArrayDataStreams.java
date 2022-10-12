package com.fuyouj.sword.database.data.array;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.fuyouj.sword.database.data.QueryOption;
import com.fuyouj.sword.database.data.array.index.ArrayDataStreamIndex;
import com.fuyouj.sword.database.data.filter.Filter;
import com.fuyouj.sword.database.data.filter.RowQueryOption;
import com.fuyouj.sword.scabard.Lists2;
import com.fuyouj.sword.scabard.Maps2;

public class EmptyArrayDataStreams implements ArrayDataStreams {
    public static final ArrayDataStreams EMPTY = new EmptyArrayDataStreams();

    @Override
    public ArrayDataStreamIndex addIndex(final String key) {
        return null;
    }

    @Override
    public long addPrimaryValue(final String value) {
        return -1;
    }

    @Override
    public List<Long> addPrimaryValues(final List<String> values) {
        return Lists2.staticEmpty();
    }

    @Override
    public long addRow(final Cells cells) {
        return 0;
    }

    @Override
    public <Value> long addValue(final String key, final Value value) {
        return -1;
    }

    @Override
    public void cleanColumn(final String key) {

    }

    @Override
    public void clear() {

    }

    @Override
    public void clearColumn(final String field) {

    }

    @Override
    public void close() {

    }

    @Override
    public boolean containsKeys(final String key) {
        return false;
    }

    @Override
    public long count(final String key) {
        return 0;
    }

    @Override
    public long count(final Filter filter) {
        return 0;
    }

    @Override
    public int count() {
        return 0;
    }

    @Override
    public boolean dataTypeAligned(final Cells cells) {
        return false;
    }

    @Override
    public void fillBufferHolyWithNullTo(final String key, final long index) {

    }

    @Override
    public ArrayDataStreamIndex findIndex(final String key) {
        return null;
    }

    @Override
    public String generatePrimaryValue() {
        return null;
    }

    @Override
    public String generatePrimaryValue(final long index) {
        return null;
    }

    @Override
    public long getBufferCount() {
        return 0;
    }

    @Override
    public long getBufferSize() {
        return 0;
    }

    @Override
    public <Value> IndexedValues<Value> getColumn(final String key, final List<Long> indexes, final Class<Value> tClass) {
        return EmptyIndexedValues.empty();
    }

    @Override
    public <Value> IndexedValues<Value> getColumn(final String field, final long from, final long to,
                                                  final Class<Value> tClass) {
        return EmptyIndexedValues.empty();
    }

    @Override
    public <Value> IndexedValues<Value> getColumn(final String field, final Class<Value> tClass) {
        return EmptyIndexedValues.empty();
    }

    @Override
    public long getIndexByPrimaryValue(final String pkValue) {
        return -1;
    }

    @Override
    public Set<String> getKeys() {
        return Collections.emptySet();
    }

    @Override
    public Map<String, Object> getMemoryDetails() {
        return Maps2.staticEmpty();
    }

    @Override
    public String getPrimaryKey() {
        return null;
    }

    @Override
    public ArrayDataStream<String> getPrimaryStream() {
        return EmptyArrayDataStream.EMPTY;
    }

    @Override
    public IndexedMap<String, Object> getRow(final long index) {
        return EmptyIndexedMap.empty();
    }

    @Override
    public IndexedMap<String, Object> getRowByPrimaryValue(final String primaryValue) {
        return EmptyIndexedMap.empty();
    }

    @Override
    public IndexedMaps<String, Object> getRows() {
        return EmptyIndexedMaps.empty();
    }

    @Override
    public IndexedMaps<String, Object> getRows(final long from, final long to) {
        return EmptyIndexedMaps.empty();
    }

    @Override
    public IndexedMaps<String, Object> getRows(final List<Long> indexes) {
        return EmptyIndexedMaps.empty();
    }

    @Override
    public IndexedMaps<String, Object> getRowsByFloor(final long from, final int total) {
        return EmptyIndexedMaps.empty();
    }

    @Override
    public IndexedMaps<String, Object> getRowsByPrimaryValues(final List<String> primaryValues) {
        return EmptyIndexedMaps.empty();
    }

    @Override
    public IndexedMaps<String, Object> getRowsByUpper(final long to, final int limit) {
        return EmptyIndexedMaps.empty();
    }

    @Override
    public IndexedMaps<String, Object> getRowsWithPage(final int page, final int size) {
        return EmptyIndexedMaps.empty();
    }

    @Override
    public IndexedMap<String, Object> getSubRow(final long index, final Set<String> keys) {
        return EmptyIndexedMap.empty();
    }

    @Override
    public IndexedMaps<String, Object> getSubRows(final Set<String> keys) {
        return null;
    }

    @Override
    public IndexedMaps<String, Object> getSubRows(final List<Long> indexes, final Set<String> keys) {
        return null;
    }

    @Override
    public <Value> IndexedValue<Value> getValue(final String key, final long index, final Class<Value> tClass) {
        return IndexedValue.empty();
    }

    @Override
    public IndexedValue<Boolean> getValueAsBoolean(final String key, final long index) {
        return IndexedValue.empty();
    }

    @Override
    public IndexedValue<Double> getValueAsDouble(final String key, final long index) {
        return IndexedValue.empty();
    }

    @Override
    public IndexedValue<Integer> getValueAsInt(final String key, final long index) {
        return IndexedValue.empty();
    }

    @Override
    public IndexedValue<Long> getValueAsLong(final String key, final long index) {
        return IndexedValue.empty();
    }

    @Override
    public IndexedValue<String> getValueAsString(final String key, final long index) {
        return IndexedValue.empty();
    }

    @Override
    public boolean isPrimaryIndexEmpty(final long index) {
        return true;
    }

    @Override
    public boolean isPrimaryStreamValueEmpty(final Cells cells) {
        return false;
    }

    @Override
    public <Value> boolean putColumn(final String key, final IndexedValues<Value> values, final Class<Value> tClass) {
        return false;
    }

    @Override
    public boolean putRow(final long index, final Cells cells) {
        return false;
    }

    @Override
    public <Value> boolean putValue(final String key, final long index, final Value value) {
        return false;
    }

    @Override
    public <Value> QueryResult<Value> query(final String key, final QueryOption<Value> query) {
        return null;
    }

    @Override
    public IndexedMaps<String, Object> query(final RowQueryOption query) {
        return EmptyIndexedMaps.empty();
    }

    @Override
    public void removeColumn(final String key) {

    }

    @Override
    public void removeIndex(final String key) {

    }

    @Override
    public boolean removeRow(final long index) {
        return false;
    }

    @Override
    public boolean removeRowCells(final long index, final List<String> keys) {
        return false;
    }

    @Override
    public void removeValue(final String key, final long index) {

    }

    @Override
    public List<Long> replacePrimaryColumn(final List<String> values) {
        return Lists2.staticEmpty();
    }

    @Override
    public IndexedMaps<String, Object> reverseFullIndexMaps() {
        return EmptyIndexedMaps.empty();
    }

    @Override
    public IndexedMaps<String, Object> reverseFullIndexMaps(final Set<String> keys) {
        return EmptyIndexedMaps.empty();
    }

    @Override
    public <T> ArrayDataStream<T> selectOrCreateStream(final String key, final Class<T> tClass) {
        return EmptyArrayDataStream.EMPTY;
    }

    @Override
    public <T> ArrayDataStream<T> selectStream(final String key, final Class<T> tClass) {
        return EmptyArrayDataStream.EMPTY;
    }
}
