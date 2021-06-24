package com.tecacet.jflat.impl;

import com.tecacet.jflat.impl.jodd.JoddPropertySetter;
import com.tecacet.jflat.impl.objenesis.BeanFactory;

public class HeaderBeanMapper<T> extends GenericBeanMapper<T> {

    public HeaderBeanMapper(Class<T> type, String[] header, String[] properties) {
        super(new BeanFactory<>(type), new JoddPropertySetter<>(),
                new HeaderRecordExtractor(header), properties);
    }


}
