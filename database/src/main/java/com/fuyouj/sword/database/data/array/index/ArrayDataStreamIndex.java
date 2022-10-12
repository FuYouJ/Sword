package com.fuyouj.sword.database.data.array.index;

import java.util.Iterator;
import java.util.Set;

import com.fuyouj.sword.database.data.filter.ConditionOp;
import com.fuyouj.sword.database.data.filter.OrderOp;

public interface ArrayDataStreamIndex<T> {
    void destroy();

    Set<Long> get(T key);

    Iterator<Set<Long>> keysIterator(OrderOp op);

    Set<Long> query(ConditionOp op, Object value);

}
