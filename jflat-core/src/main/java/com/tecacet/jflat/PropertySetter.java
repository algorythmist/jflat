package com.tecacet.jflat;

public interface PropertySetter<T> {

    void setProperty(T bean, String property, Object value);
}
