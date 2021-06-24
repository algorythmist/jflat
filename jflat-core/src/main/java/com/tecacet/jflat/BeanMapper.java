package com.tecacet.jflat;

import java.util.function.Function;

/**
 * Map a Record to a Java Bean of type T 
 * 
 * @author dimitri
 *
 * @param <T> the type of the mapped bean
 */
@FunctionalInterface
public interface BeanMapper<T> extends Function<RowRecord, T> {

}
