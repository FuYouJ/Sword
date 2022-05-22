package com.fuyouj.sword.scabard.command;
//
//import java.util.Optional;
//
//import com.fasterxml.jackson.annotation.JsonProperty;
//
//import net.thingworks.jarvis.utils.command.NestedCommand;
//import net.thingworks.jarvis.utils.command.Required;
//import net.thingworks.jarvis.utils.command.ResourceCommand;
//import net.thingworks.jarvis.utils.exception.CommonErrorCode;
//import net.thingworks.jarvis.utils.filter.Filter;
//import net.thingworks.jarvis.utils.filter.dynamic.DynamicFilterType;
//import net.thingworks.jarvis.utils.type.DateOffset;
//import net.thingworks.jarvis.utils.type.DateTimes2;
//import net.thingworks.jarvis.utils.type.DateUnit;
//import net.thingworks.jarvis.utils.type.Enums;
//import net.thingworks.jarvis.utils.type.time.ChronoTime;
//
//import lombok.Getter;
//
//import static net.thingworks.jarvis.utils.exception.IllegalCommandException.badCommand;
//
//public class DateRangeFilterCommand implements ResourceCommand {
//    @Required
//    @Getter
//    private final String from;
//    @NestedCommand
//    private final DateOffsetCommand fromOffset;
//    @Getter
//    @Required
//    private final String to;
//    @NestedCommand
//    private final DateOffsetCommand toOffset;
//
//    public DateRangeFilterCommand(@JsonProperty("from") final String from,
//                                  @JsonProperty("fromOffset") final DateOffsetCommand fromOffset,
//                                  @JsonProperty("to") final String to,
//                                  @JsonProperty("toOffset") final DateOffsetCommand toOffset) {
//        this.from = from;
//        this.fromOffset = fromOffset;
//        this.to = to;
//        this.toOffset = toOffset;
//    }
//
//    public Filter getEnd(final String key) {
//        return getFilter(key, to, DateRangeRole.End, getToDateOffset());
//    }
//
//    public Filter getStart(final String key) {
//        return getFilter(key, from, DateRangeRole.Start, getFromOffset());
//    }
//
//    private Filter getFilter(final String key, final String date, final DateRangeRole role, final DateOffset offset) {
//        return Enums.fromString(date, DynamicFilterType.class)
//                .<Filter>map(type -> DynamicDateFilter.dynamic(key, type, offset.defaultUnit(type.getDateUnit()), role))
//                .orElseGet(() -> {
//                    ChronoTime chronoTime = DateTimes2.parse(date)
//                            .orElseThrow(() -> badCommand(
//                                    CommonErrorCode.PROPERTY_INVALID,
//                                    date + " is not valid date string")
//                            );
//
//                    return SimpleDateFilter.of(key, chronoTime.getDateTime(), offset.defaultUnit(DateUnit.Day), role);
//                });
//    }
//
//    private DateOffset getFromOffset() {
//        return Optional.ofNullable(fromOffset).map(DateOffsetCommand::toResource).orElse(DateOffset.none());
//    }
//
//    private DateOffset getToDateOffset() {
//        return Optional.ofNullable(toOffset).map(DateOffsetCommand::toResource).orElse(DateOffset.none());
//    }
//
//}
