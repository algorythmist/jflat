package com.tecacet.jflat.impl;

import com.tecacet.jflat.BeanMapper;
import com.tecacet.jflat.RowRecord;
import com.tecacet.jflat.domain.Order;
import com.tecacet.jflat.impl.jodd.JoddPropertySetter;
import com.tecacet.jflat.impl.objenesis.BeanFactory;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GenericBeanMapperTest {

    @Test
    void testGetSimpleRow() {

        BeanMapper<Order> mapper = new GenericBeanMapper<>(new BeanFactory<>(Order.class),
                new JoddPropertySetter(),
                new IndexRecordExtractor(new int[] {0,1,2}),
                new String[]{"number", "price", "quantity"});

        RowRecord record = new ArrayRowRecord(1, new String[]{"911", "12.5", "100"});

        // First row gets the header
        Order order = mapper.apply(record);
        assertEquals(100, order.getQuantity());
        assertEquals("911", order.getNumber());
        assertEquals(12.5, order.getPrice(), 0.0001);
        assertNull(order.getCustomer());

    }

    @Test
    void testGetNestedRow() {
        BeanMapper<Order> mapper = new GenericBeanMapper<>(new BeanFactory<>(Order.class),
                new JoddPropertySetter(),
                new IndexRecordExtractor(new int[] {0,1,2, 3}),
                new String[]{"number", "price", "quantity", "customer.firstName"});

        RowRecord record = new ArrayRowRecord(1,
                new String[] { "911", "12.5", "100", "Jack" });
        Order order = mapper.apply(record);
        assertEquals(100, order.getQuantity());
        assertEquals("911", order.getNumber());
        assertEquals(12.5, order.getPrice(), 0.0001);
        assertNotNull(order.getCustomer());
        assertEquals("Jack", order.getCustomer().getFirstName());
    }

    @Test
    void testConverter() {
        GenericBeanMapper<Order> mapper = new GenericBeanMapper<>(new BeanFactory<>(Order.class),
                new JoddPropertySetter(),
                new IndexRecordExtractor(new int[] {0,1,2, 3}),
                new String[]{"number", "price", "quantity", "customer.firstName"});


        mapper.registerConverter("price",
            (value) -> Double.parseDouble(value.substring(value.indexOf("$") + 1)));

        RowRecord record = new ArrayRowRecord(1,
                new String[] { "911", "$12.5", "100", "Jack" });
        Order order = mapper.apply(record);
        assertEquals(100, order.getQuantity());
        assertEquals("911", order.getNumber());
        assertEquals(12.5, order.getPrice(), 0.0001);
        assertNotNull(order.getCustomer());
    }
}
