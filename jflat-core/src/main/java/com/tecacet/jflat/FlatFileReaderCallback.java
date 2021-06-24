package com.tecacet.jflat;

import java.util.function.BiConsumer;

/**
 * Allows custom processing of mapped records 
 * 
 * @author dimitri
 *
 * @param <T> the bean type
 */
@FunctionalInterface
public interface FlatFileReaderCallback<T> extends BiConsumer<RowRecord, T> {
	
}
