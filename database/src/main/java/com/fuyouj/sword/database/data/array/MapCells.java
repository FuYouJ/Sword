package com.fuyouj.sword.database.data.array;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.fuyouj.sword.database.base.BuiltInItemKey;
import com.fuyouj.sword.scabard.Maps2;
import com.fuyouj.sword.scabard.Strings;

import lombok.Setter;

public class MapCells extends BaseCells {
    private final Map<String, Object> source;
    private final EntryCell currentCell = new EntryCell();
    private Iterator<Map.Entry<String, Object>> iterator;

    public MapCells(final Map<String, Object> source) {
        this.source = source;
        tryGetPrimaryValue();
    }

    public static MapCells of(final Map<String, Object> source) {
        return new MapCells(source);
    }

    @Override
    public Object get(final String key) {
        return source.get(key);
    }

    @Override
    public boolean hasNext() {
        return iterator.hasNext();
    }

    @Override
    public Iterator<Cell> iterator() {
        iterator = source.entrySet().iterator();
        return this;
    }

    @Override
    public Set<String> keySet() {
        return source.keySet();
    }

    @Override
    public Cell next() {
        currentCell.setCurrentEntry(iterator.next());
        return currentCell;
    }

    @Override
    public Map<String, Object> toMap() {
        return source;
    }

    private void tryGetPrimaryValue() {
        if (Maps2.isNullOrEmpty(source)) {
            return;
        }

        Object value = source.get(BuiltInItemKey.INDEX.getKey());
        if (value == null) {
            return;
        }

        String primaryValue = Strings.toString(value);
        if (!Strings.isNullOrEmpty(primaryValue)) {
            this.primaryValue = primaryValue;
        }
    }

    private class EntryCell implements Cell {
        @Setter
        Map.Entry<String, Object> currentEntry;

        @Override
        public String getKey() {
            return currentEntry.getKey();
        }

        @Override
        public Object getValue() {
            return currentEntry.getValue();
        }
    }
}

