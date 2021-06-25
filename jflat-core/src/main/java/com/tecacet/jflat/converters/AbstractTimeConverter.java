package com.tecacet.jflat.converters;

import java.time.format.DateTimeFormatter;
import java.util.function.Function;

public abstract class AbstractTimeConverter<T> implements Function<String, T> {

    protected final DateTimeFormatter[] formatters;

    protected AbstractTimeConverter(String... dateFormatStrings) {
        super();
        this.formatters = new DateTimeFormatter[dateFormatStrings.length];
        for (int i = 0; i < dateFormatStrings.length; i++) {
            formatters[i] = DateTimeFormatter.ofPattern(dateFormatStrings[i]);
        }
    }

    @Override
    public T apply(String string) {
        if (string == null || string.trim().isEmpty()) {
            return null;
        }
        for (DateTimeFormatter formatter : formatters) {
            try {
                return parse(string.trim(), formatter);
            } catch (Exception e) {
                // try the next formatter
            }
        }
        String message = String.format("Error converting %s to Date/Time", string);
        throw new IllegalArgumentException(message);
    }

    protected abstract T parse(String string, DateTimeFormatter formatter);
}
