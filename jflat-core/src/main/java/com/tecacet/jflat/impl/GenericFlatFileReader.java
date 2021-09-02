package com.tecacet.jflat.impl;

import com.tecacet.jflat.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.function.Function;
import java.util.stream.Stream;

public class GenericFlatFileReader<T> extends AbstractFlatFileReader<T> {

    protected BeanMapper<T> beanMapper;
    protected FlatFileParser parser;

    public GenericFlatFileReader(BeanMapper<T> beanMapper, FlatFileParser parser) {
        this.beanMapper = beanMapper;
        this.parser = parser;
    }

    @Override
    public void read(InputStream is, FlatFileReaderCallback<T> callback) throws IOException {
        try (InputStreamReader reader = new InputStreamReader(is)) {
            Iterable<RowRecord> records = parser.parse(reader);
            for (RowRecord record : records) {
                if (record.skipped()) {
                    continue;
                }
                T bean = beanMapper.apply(record);
                bean = callback.apply(record, bean);
            }
        }
    }

    @Override
    public Stream<T> readAsStream(Reader reader) throws IOException {
        return readAsRecordStream(reader).filter(record -> !record.skipped())
                .map(record -> beanMapper.apply(record));
    }

    @Override
    public Stream<RowRecord> readAsRecordStream(Reader reader) throws IOException {
        return parser.parseStream(reader);
    }

    @Override
    public <S> FlatFileReader<T> registerConverter(String property, Function<String, S> converter) {
        if (beanMapper instanceof GenericBeanMapper) {
            ((GenericBeanMapper) beanMapper).registerConverter(property, converter);
        }
        return this;
    }

    public void setParser(FlatFileParser parser) {
        this.parser = parser;
    }

    public void setBeanMapper(BeanMapper<T> beanMapper) {
        this.beanMapper = beanMapper;
    }

    public BeanMapper<T> getBeanMapper() {
        return beanMapper;
    }

    public FlatFileParser getParser() {
        return parser;
    }
}
