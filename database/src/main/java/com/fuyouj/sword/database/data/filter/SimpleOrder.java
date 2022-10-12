package com.fuyouj.sword.database.data.filter;

import java.util.List;
import java.util.Map;

import com.fuyouj.sword.scabard.Maps2;
import com.fuyouj.sword.scabard.Objects2;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@EqualsAndHashCode
public class SimpleOrder implements Order {
    @Getter
    private String referenceKey;
    @Getter
    private OrderOp op;

    private SimpleOrder(final String referenceKey, final OrderOp op) {
        this.referenceKey = referenceKey;
        this.op = op;
    }

    public static SimpleOrder extract(final Order order) {
        if (order == null) {
            return null;
        }

        if (order instanceof SimpleOrder) {
            return (SimpleOrder) order;
        }

        if (order instanceof CompositeOrder) {
            List<Order> orders = ((CompositeOrder) order).getOrders();
            for (Order subOrder : orders) {
                SimpleOrder simpleOrder = extract(subOrder);
                if (simpleOrder != null) {
                    return simpleOrder;
                }
            }
        }

        return null;
    }

    public static SimpleOrder of(final String referenceKey, final OrderOp op) {
        return new SimpleOrder(referenceKey, op);
    }

    @Override
    public int compare(final Map<String, Object> o1, final Map<String, Object> o2) {
        if (Maps2.isNullOrEmpty(o1) && Maps2.isNullOrEmpty(o2)) {
            return 0;
        }

        if (Maps2.isNullOrEmpty(o1)) {
            return result(-1);
        }

        if (Maps2.isNullOrEmpty(o2)) {
            return result(1);
        }

        return result(Objects2.compare(o1.get(referenceKey), o2.get(referenceKey)));
    }

    @Override
    public Order replaceKey(final Map<String, String> map) {
        if (Maps2.isNullOrEmpty(map) || !map.containsKey(referenceKey)) {
            return this;
        }

        return new SimpleOrder(map.getOrDefault(referenceKey, referenceKey), op);
    }

    private int result(final int compare) {
        if (op == OrderOp.DESC) {
            return -compare;
        }

        return compare;
    }
}

