package com.tecacet.jflat.impl;

import com.tecacet.jflat.RowRecord;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ArrayRowRecordTest {

    @Test
    void getByIndex() {
        String[] tokens = {"x", "y", "z"};
        RowRecord record = new ArrayRowRecord(10, tokens);
        assertEquals("y", record.get(1));
        assertEquals(10, record.getRowNumber());
        assertEquals(3, record.size());
        assertEquals("[x, y, z]", record.toString());
    }

    @Test
    void getByNameUnsupported() {
        String[] tokens = {"x", "y", "z"};
        RowRecord record = new ArrayRowRecord(10, tokens);
        assertThrows(UnsupportedOperationException.class,
                () -> record.get("hello"));

    }
}