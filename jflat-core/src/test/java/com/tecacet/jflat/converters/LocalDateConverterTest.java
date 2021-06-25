package com.tecacet.jflat.converters;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LocalDateConverterTest {

    @Test
    void builtInFormats() {
        LocalDateConverter converter = new LocalDateConverter();
        assertEquals(LocalDate.of(2013, 7, 15), converter.apply("2013-7-15"));
        assertEquals(LocalDate.of(2013, 7, 15), converter.apply("7-15-2013"));
        assertEquals(LocalDate.of(2013, 7, 15), converter.apply("7/15/2013"));
    }

    @Test
    void suppliedFormats() {
        LocalDateConverter converter = new LocalDateConverter("M+d%yy");
        assertEquals(LocalDate.of(2013, 7, 15), converter.apply("7+15%13"));
    }

}