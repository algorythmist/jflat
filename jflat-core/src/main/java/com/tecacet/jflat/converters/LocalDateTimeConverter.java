package com.tecacet.jflat.converters;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LocalDateTimeConverter extends AbstractTimeConverter<LocalDateTime> {

    public LocalDateTimeConverter(String... dateFormatStrings) {
        super(dateFormatStrings);
    }

    @Override
    protected LocalDateTime parse(String string, DateTimeFormatter formatter) {
        return LocalDateTime.parse(string, formatter);
    }
}
