package com.fuyouj.sword.database.data.filter;

import java.util.List;
import java.util.Map;

import com.fuyouj.sword.scabard.Lists2;

public class CompositeOrder implements Order {
    public static final Order DEFAULT = (o1, o2) -> 1;

    private List<Order> orders;

    private CompositeOrder(final List<Order> orders) {
        this.orders = orders;
    }

    public static CompositeOrder of(final List<Order> simpleOrders) {
        return new CompositeOrder(simpleOrders);
    }

    @Override
    public int compare(final Map<String, Object> o1, final Map<String, Object> o2) {
        if (Lists2.isNullOrEmpty(orders)) {
            return 1;
        }

        int compare = 1;
        for (Order order : orders) {
            compare = order.compare(o1, o2);
            if (compare != 0) {
                return compare;
            }
        }

        return compare;
    }

    @Override
    public List<Order> getOrders() {
        return orders;
    }

    @Override
    public Order replaceKey(final Map<String, String> map) {
        return of(Lists2.map(orders, order -> order.replaceKey(map)));
    }
}


