package com.tecacet.jflat.impl;

import com.tecacet.jflat.impl.jodd.JoddPropertySetter;
import com.tecacet.jflat.impl.objenesis.BeanFactory;

import java.util.function.Supplier;
import java.util.stream.IntStream;

public class IndexBeanMapper<T> extends GenericBeanMapper<T> {

    public IndexBeanMapper(Class<T> type, String[] properties) {
        this(new BeanFactory<>(type), properties);
    }

    public IndexBeanMapper(Supplier<T> beanFactory, String[] properties) {
        this(beanFactory, IntStream.range(0, properties.length).toArray(), properties);
    }

    public IndexBeanMapper(Supplier<T> beanFactory, int[] indexes, String[] properties) {
       super(beanFactory, new JoddPropertySetter<>(), new IndexRecordExtractor(indexes),
               properties);
    }
}
