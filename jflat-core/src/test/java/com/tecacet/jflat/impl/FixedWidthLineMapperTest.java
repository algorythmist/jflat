package com.tecacet.jflat.impl;

import com.tecacet.jflat.LineMapper;
import com.tecacet.jflat.RowRecord;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FixedWidthLineMapperTest {

    @Test
    void happyPath() {
        LineMapper parser = new FixedWidthLineMapper(new int[]{1, 9, 9});
        String line = "D000375204000013900";
        RowRecord record = parser.apply(1L, line);
        assertEquals("D", record.get(0));
        assertEquals("000375204", record.get(1));
        assertEquals("000013900", record.get(2));

        line = "H0000L463070123B";
        parser = new FixedWidthLineMapper(new int[]{1, 4, 4});
        record = parser.apply(1L, line);
        assertEquals("H", record.get(0));
        assertEquals("0000", record.get(1));
        assertEquals("L463", record.get(2));

        line = "   XXX   ";
        parser = new FixedWidthLineMapper(new int[]{3, 3, 3});
        record = parser.apply(1L, line);
        assertNull(record.get(0));
        assertEquals("XXX", record.get(1));
        assertNull(record.get(2));
    }

}
