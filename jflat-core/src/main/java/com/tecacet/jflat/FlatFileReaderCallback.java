package com.tecacet.jflat;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;

/**
 * Allows custom processing of mapped records 
 * 
 * @author dimitri
 *
 * @param <T> the bean type
 */
@FunctionalInterface
public interface FlatFileReaderCallback<T> extends BiFunction<RowRecord, T, T> {
	
}
