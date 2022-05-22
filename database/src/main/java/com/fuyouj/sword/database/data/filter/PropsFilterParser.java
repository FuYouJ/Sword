package com.fuyouj.sword.database.data.filter;
//
//import java.util.List;
//import java.util.Map;
//
//import com.fuyouj.sword.scabard.Lists2;
//import com.fuyouj.sword.scabard.Maps2;
//import com.fuyouj.sword.scabard.Strings;
//import com.fuyouj.sword.scabard.error.CommonErrorCode;
//import com.fuyouj.sword.scabard.exception.IllegalCommandException;
//
//import static com.fuyouj.sword.scabard.exception.IllegalCommandException.badCommand;
//
//public class PropsFilterParser {
//    private final TemplateTokenChecker checker;
//
//    public PropsFilterParser(final TemplateTokenChecker checker) {
//        this.checker = checker;
//    }
//
//    public Filter parse(final Map<String, Object> filters) {
//        if (Maps2.isNullOrEmpty(filters)) {
//            return null;
//        }
//
//        return checkFilter(filters, "");
//    }
//
//    @SuppressWarnings("unchecked")
//    private List<Filter> checkCompositeFilters(final Map.Entry<String, Object> filter, final String key) {
//        String path = key + "." + filter.getKey();
//
//        if (!(filter.getValue() instanceof List)) {
//            throw new IllegalCommandException(
//                    CommonErrorCode.ENTRY_FILTER_INVALID,
//                    "path [%s] filter must be a composite filter", path
//            );
//        }
//
//        return Lists2.mapNotNull((List) filter.getValue(), filterValue -> {
//            if (!(filterValue instanceof Map)) {
//                throw new IllegalCommandException(
//                        CommonErrorCode.ENTRY_FILTER_INVALID,
//                        "path [%s] with content [%s] is not a valid simple filter.", path, filterValue
//                );
//            }
//
//            return checkFilter((Map<String, Object>) filterValue, key);
//        });
//    }
//
//    private ConditionOp checkConditionOp(final Map.Entry<String, Object> entry, final String key) {
//        final String op = entry.getKey();
//        ConditionOp conditionOp = ConditionOp.fromString(op).orElseThrow(() ->
//                new IllegalCommandException(
//                        CommonErrorCode.ENTRY_FILTER_INVALID,
//                        "path [%s] with [%s] condition operator is NOT valid, must be one of %s",
//                        key,
//                        op,
//                        Strings.fromEnum(ConditionOp.class)
//                )
//        );
//
//        if (conditionOp == ConditionOp.DYNAMIC) {
//            checkDynamicFilterValue(entry.getKey(), entry.getValue(), key);
//        }
//
//        return conditionOp;
//    }
//
//    private Filter checkDateRangeFilter(final String parentKey,
//                                        final String filterKey,
//                                        final Map.Entry<String, Object> entry) {
//        final Object value = entry.getValue();
//
//        IllegalCommandException badFormat = badCommand(
//                CommonErrorCode.ENTRY_FILTER_INVALID,
//                "[%s] is NOT a valid date range filter, it must look like {from: 2010-01-01, to: 2010-02-01}",
//                value);
//
//        if (value == null) {
//            throw new IllegalCommandException(
//                    CommonErrorCode.ENTRY_FILTER_INVALID,
//                    "[%s] can NOT be null", entry.getKey()
//            );
//        }
//
//        if (!(value instanceof Map)) {
//            throw badFormat;
//        }
//
//        return Objects2.fromProperties((Map<String, Object>) value, DateRangeFilterCommand.class)
//                .map(cmd -> {
//                    cmd.check();
//
//                    return SimpleDateRangeFilter.rangeOf(filterKey, cmd.getStart(filterKey), cmd.getEnd(filterKey));
//                })
//                .orElseThrow(() -> badFormat);
//
//    }
//
//    private Filter checkDynamicFilter(final String parentKey,
//                                      final String filterKey,
//                                      final Map.Entry<String, Object> entry) {
//        if (!(entry.getValue() instanceof String)) {
//            throw badCommand(
//                    CommonErrorCode.PROPERTY_INVALID,
//                    "%s. %s must be one of %s", parentKey, filterKey, Strings.fromEnum(DynamicFilterType.class));
//        }
//
//        return DynamicDateTimeFilterParser.parse(filterKey, entry.getValue().toString(), null);
//    }
//
//    private void checkDynamicFilterValue(final String key, final Object value, final String path) {
//        IllegalCommandException badCommand = badCommand(
//                CommonErrorCode.ENTRY_FILTER_INVALID,
//                "Dynamic filter [%s.%s] with value [%s] is NOT valid, must be one of %s",
//                path, key, value, Strings.fromEnum(DynamicFilterType.class)
//        );
//
//        if (!(value instanceof String)) {
//            throw badCommand;
//        }
//
//        Enums.fromString(value.toString(), DynamicFilterType.class).orElseThrow(() -> badCommand);
//    }
//
//    private Filter checkFilter(final Map<String, Object> filters, final String key) {
//        filterSizeCheck(filters);
//
//        Map.Entry<String, Object> filter = filters.entrySet().iterator().next();
//
//        if (FilterOp.isOp(filter.getKey())) {
//            List<Filter> filterList = checkCompositeFilters(filter, key + "." + filter.getKey());
//
//            if (Lists2.isNullOrEmpty(filterList)) {
//                return null;
//            }
//
//            return FilterOp.fromString(filter.getKey())
//                    .map(op -> {
//                        if (op == FilterOp.AND) {
//                            return AndFilter.of(filterList);
//                        }
//
//                        return OrFilter.of(filterList);
//                    })
//                    .orElseThrow(() -> new IllegalCommandException(
//                            CommonErrorCode.ENTRY_FILTER_INVALID,
//                            "%s.%s is not valid", key, filter.getKey()
//                    ));
//        } else {
//            return checkSimpleFilter(filter, key);
//        }
//    }
//
//    @SuppressWarnings("unchecked")
//    private Filter checkSimpleFilter(final Map.Entry<String, Object> filter, final String key) {
//        Object filterContent = filter.getValue();
//        String filterKey = filter.getKey();
//
//        if (!(filterContent instanceof Map)) {
//            throw new IllegalCommandException(
//                    CommonErrorCode.ENTRY_FILTER_INVALID,
//                    "simple filter with path [%s] and content [%s] must be a key value pair",
//                    key + "." + filterKey, filterContent
//            );
//        }
//
//        if (((Map) filterContent).isEmpty()) {
//            return null;
//        }
//
//        filterSizeCheck(((Map) filterContent));
//
//        Map.Entry<String, Object> entry =
//                (Map.Entry<String, Object>) ((Map) filterContent).entrySet().iterator().next();
//
//        ConditionOp op = checkConditionOp(entry, key + "." + filterKey);
//
//        if (op == ConditionOp.DATE) {
//            return checkDateRangeFilter(key, filterKey, entry);
//        }
//
//        if (op == ConditionOp.DYNAMIC) {
//            return checkDynamicFilter(key, filterKey, entry);
//        }
//
//        final Object value = entry.getValue();
//
//        if (value == null) {
//            throw new IllegalCommandException(
//                    CommonErrorCode.ENTRY_FILTER_INVALID,
//                    "[%s] can NOT be null", entry.getKey()
//            );
//        }
//
//        if (value instanceof Map) {
//            return ComplexQueryFilterParser.parse(filterKey, op, (Map<String, Object>) value);
//        }
//
//        Object filterValue = value;
//
//        if (this.checker.isTemplateToken(filterValue.toString())) {
//            filterValue = this.checker.replace(filterValue.toString());
//            if (filterValue == null) {
//                if (ComplexQueryFilterParser.isComplexQuery(value.toString())) {
//                    return ComplexQueryFilterParser.parse(filterKey, op, value.toString());
//                }
//
//                return null;
//            }
//        }
//
//        return SimpleFilter.of(filterKey, op, filterValue);
//    }
//
//    private void filterSizeCheck(final Map filters) {
//        if (filters.entrySet().size() > 1) {
//            throw new IllegalCommandException(
//                    CommonErrorCode.ENTRY_FILTER_INVALID,
//                    "Do you miss one of condition operator %s", Strings.fromEnum(FilterOp.class)
//            );
//        }
//    }
//
//}
