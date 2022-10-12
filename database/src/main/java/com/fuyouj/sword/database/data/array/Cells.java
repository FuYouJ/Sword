package com.fuyouj.sword.database.data.array;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public interface Cells {
    Set<String> keySet();
    void genPrimaryValue(String gen);

    Object get(String key);

    String getPrimaryValue();

    Map<String, Object> toMap();

    Iterator<Cell> iterator();
}
