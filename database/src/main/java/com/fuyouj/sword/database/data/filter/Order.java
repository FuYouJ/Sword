package com.fuyouj.sword.database.data.filter;

import java.io.Serializable;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import com.fuyouj.sword.scabard.Lists2;

public interface Order extends Comparator<Map<String, Object>>, Serializable {

    default List<Order> getOrders() {
        return Lists2.items(this);
    }

    default Order replaceKey(Map<String, String> map) {
        return this;
    }

    default <T> Comparator<T> transfer(Function<T, Map<String, Object>> convertor) {
        return (t1, t2) -> this.compare(convertor.apply(t1), convertor.apply(t2));
    }
}
