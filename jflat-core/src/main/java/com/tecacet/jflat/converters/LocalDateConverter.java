package com.tecacet.jflat.converters;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class LocalDateConverter extends AbstractTimeConverter<LocalDate> {

    private static final String[] STANDARD_FORMATS = { "M/d/yyyy", "M-d-yyyy", "yyyy-M-d" };

    public LocalDateConverter() {
        this(STANDARD_FORMATS);
    }

    public LocalDateConverter(String... dateFormatStrings) {
        super(dateFormatStrings);
    }

    @Override
    protected LocalDate parse(String string, DateTimeFormatter formatter) {
        return LocalDate.parse(string, formatter);
    }

}
