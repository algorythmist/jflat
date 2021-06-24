package com.tecacet.jflat;

import java.util.function.BiFunction;

/**
 * Map a line to a tokenized record
 * 
 * @author dimitri
 *
 */
@FunctionalInterface
public interface LineMapper extends BiFunction<Long, String, RowRecord> {

}
