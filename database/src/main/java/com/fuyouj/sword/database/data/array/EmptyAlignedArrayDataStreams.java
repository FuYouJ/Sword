package com.fuyouj.sword.database.data.array;

import java.util.Map;

import com.fuyouj.sword.database.data.QueryOption;

public class EmptyAlignedArrayDataStreams implements IndexAlignedArrayDataStreams {
    public static final IndexAlignedArrayDataStreams EMPTY = new EmptyAlignedArrayDataStreams();

    @Override
    public <Value> int add(final String key, final Value value) {
        return -1;
    }

    @Override
    public int add(final Map<String, Object> value) {
        return 0;
    }

    @Override
    public void clear(final String key) {

    }

    @Override
    public void close() {

    }

    @Override
    public long count(final String key) {
        return 0;
    }

    @Override
    public int count() {
        return 0;
    }

    @Override
    public void fillBufferHolyWithNullTo(final String key, final int index) {

    }

    @Override
    public <Value> Value get(final String key, final int index, final Class<Value> tClass) {
        return null;
    }

    @Override
    public Map<String, Object> get(final int index) {
        return null;
    }

    @Override
    public Map<String, ArrayDataStream> getAllStream() {
        return null;
    }

    @Override
    public Boolean getAsBoolean(final String key, final int index) {
        return null;
    }

    @Override
    public Double getAsDouble(final String key, final int index) {
        return null;
    }

    @Override
    public Integer getAsInt(final String key, final int index) {
        return null;
    }

    @Override
    public Long getAsLong(final String key, final int index) {
        return null;
    }

    @Override
    public String getAsString(final String key, final int index) {
        return null;
    }

    @Override
    public long getBufferSize() {
        return 0;
    }

    @Override
    public String getPrimaryKey() {
        return null;
    }

    @Override
    public boolean put(final int index, final Map<String, Object> value) {
        return false;
    }

    @Override
    public <Value> boolean put(final String key, final int index, final Value value) {
        return false;
    }

    @Override
    public <Value> QueryResult<Value> query(final String key, final QueryOption<Value> query) {
        return null;
    }

    @Override
    public void remove(final String key, final int index) {

    }

    @Override
    public boolean remove(final int index) {
        return false;
    }

    @Override
    public <T> ArrayDataStream<T> selectOrCreateStream(final String key, final Class<T> tClass) {
        return null;
    }

    @Override
    public <T> ArrayDataStream<T> selectStream(final String key, final Class<T> tClass) {
        return null;
    }
}
