package com.tecacet.jflat.impl.objenesis;

import com.tecacet.jflat.domain.ClassicQuote;
import com.tecacet.jflat.domain.ImmutableQuote;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BeanFactoryTest {

    @Test
    void testNormalConstructor() {
        BeanFactory<ClassicQuote> beanFactory = new BeanFactory<>(ClassicQuote.class);
        ClassicQuote bean = beanFactory.get();
        assertEquals(ClassicQuote.class, bean.getClass());
    }

    @Test
    void testNoDefaultConstructor() {
        BeanFactory<ImmutableQuote> beanFactory = new BeanFactory<>(ImmutableQuote.class);
        ImmutableQuote bean = beanFactory.get();
        assertEquals(ImmutableQuote.class, bean.getClass());
    }
}