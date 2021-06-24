package com.tecacet.jflat;

/**
 * Abstraction for getting object properties
 *
 * @author Dimitri Papaioannou
 *
 * @param <T> the bean type
 */
public interface PropertyGetter<T> {

    /**
     * Get a named property value from a bean
     *
     * @param bean target bean
     * @param propertyName name of the property to access
     * @return the value corresponding to this property. Null if it does not exist
     */
    Object getProperty(T bean, String propertyName);
}
