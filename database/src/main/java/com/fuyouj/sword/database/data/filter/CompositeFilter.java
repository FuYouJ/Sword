package com.fuyouj.sword.database.data.filter;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.fuyouj.sword.scabard.Lists2;
import com.fuyouj.sword.scabard.Maps2;
import com.fuyouj.sword.scabard.Strings;

public interface CompositeFilter extends Filter {
    @Override
    default String asFormula() {
        if (Lists2.isNullOrEmpty(getFilters())) {
            return null;
        }

        String formula = Strings.join(Lists2.mapNotNull(getFilters(), Filter::asFormula), " " + op().name() + " ");
        if (getFilters().size() > 1) {
            return "(" + formula + ")";
        } else {
            return formula;
        }
    }

    default Map<String, Object> asPlainObj() {
        return Maps2.of(op().getName(), Lists2.map(getFilters(), Filter::asPlainObj));
    }

    List<Filter> getFilters();

    boolean isSimpleComposite();

    @Override
    default Iterator<Filter> iterator() {
        return this.getFilters().iterator();
    }

    FilterOp op();
}
