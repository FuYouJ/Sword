package com.fuyouj.sword.database.data.array;

import java.util.List;

public interface QueryResult<Value> {
    String getName();

    //交集, and
    QueryResults join(QueryResult<?> anotherResult);

    List<Value> toList();

    //并集, or
    QueryResults union(QueryResult<?> anotherResult);

}
