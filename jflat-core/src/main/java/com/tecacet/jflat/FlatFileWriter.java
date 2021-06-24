package com.tecacet.jflat;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Collection;
import java.util.function.Function;

/**
 * Writes a collection of beans in flat file format
 *
 * @author Dimitri Papaioannou
 *
 * @param <T> the type of the beans
 */
public interface FlatFileWriter<T> {

    default void writeToFile(String pathToFile, Collection<T> beans) throws IOException {
        try (FileWriter fw = new FileWriter(pathToFile)) {
            write(fw, beans);
        }
    }

    void write(Writer writer, Collection<T> beans) throws IOException;

    <S> FlatFileWriter<T> registerConverterForProperty(String property, Function<S, String> converter);

    <S> FlatFileWriter<T> registerConverterForClass(Class<S> clazz, Function<S, String> converter);

    FlatFileWriter<T> registerPropertyGetter(String property, Function<T, Object> getter);

    FlatFileWriter<T> withLineMapper(Function<T, String> mapper);
}
