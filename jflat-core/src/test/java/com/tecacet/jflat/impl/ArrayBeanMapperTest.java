package com.tecacet.jflat.impl;

import com.tecacet.jflat.BeanMapper;
import com.tecacet.jflat.RowRecord;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ArrayBeanMapperTest {

    @Test
    void apply() {
        String[] tokens = {"x", "y", "z"};
        RowRecord record = new ArrayRowRecord(10, tokens);
        BeanMapper<String[]> beanMapper = new ArrayBeanMapper();
        String[] value = beanMapper.apply(record);
        assertArrayEquals(tokens, value);

    }
}