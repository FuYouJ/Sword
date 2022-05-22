package com.fuyouj.sword.database.data.array;

public interface QueryResults {
    QueryResults join(QueryResults anotherResults);

    QueryResults union(QueryResults anotherResults);
}
