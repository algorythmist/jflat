package com.tecacet.jflat.converters;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class LocalTimeConverter extends AbstractTimeConverter<LocalTime> {

    public LocalTimeConverter(String... dateFormatStrings) {
        super(dateFormatStrings);
    }

    @Override
    protected LocalTime parse(String string, DateTimeFormatter formatter) {
        return LocalTime.parse(string, formatter);
    }

}
