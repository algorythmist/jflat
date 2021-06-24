package com.tecacet.jflat;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * Reads a flat file into a collection of beans.
 *
 * @author Dimitri Papaioannou
 *
 * @param <T> the type of the destination beans
 */
public interface FlatFileReader<T> {

    /**
     * Read an input stream, processing each row with a callback
     *
     * @param is source input stream
     * @param callback a callback for each line
     * @throws IOException if reading fails
     */
    void read(InputStream is, FlatFileReaderCallback<T> callback) throws IOException;

    void read(String resourceName, FlatFileReaderCallback<T> callback) throws IOException;

    Stream<T> readAsStream(Reader reader) throws IOException;

    Stream<RowRecord> readAsRecordStream(Reader reader) throws IOException;

    List<T> readAll(String resourceName) throws IOException;

    List<T> readAllWithCallback(String resourceName, FlatFileReaderCallback<T> callback) throws IOException;

    default  Stream<T> readAsStream(InputStream is) throws IOException {
        Reader reader = new InputStreamReader(is);
        return readAsStream(reader);
    }

    default List<T> readAll(InputStream is) throws IOException {
        List<T> list = new ArrayList<>();
        read(is, (record, bean) -> list.add(bean));
        return list;
    }

    default List<T> readAllWithCallback(InputStream is, FlatFileReaderCallback<T> callback) throws IOException {
        List<T> list = new ArrayList<>();
        read(is, (record, bean) -> {
            callback.accept(record, bean);
            list.add(bean);
        });
        return list;
    }

    <S> FlatFileReader<T> registerConverter(Class<S> type, Function<String, S> converter);

    <S> FlatFileReader<T> registerConverter(String property, Function<String, S> converter);
}
