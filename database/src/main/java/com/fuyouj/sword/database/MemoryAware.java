package com.fuyouj.sword.database;

import java.util.Map;

public interface MemoryAware {
    Map<String, Object> getMemorySummaries();

    Map<String, Object> getMemoryDetails();
}
