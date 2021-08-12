package com.tecacet.jflat;

import com.tecacet.jflat.impl.IndexBeanMapper;
import com.tecacet.jflat.impl.*;
import org.apache.commons.csv.CSVFormat;

import java.util.function.Function;

public class CSVReader<T> extends GenericFlatFileReader<T> {

    public CSVReader(BeanMapper<T> beanMapper) {
        super(beanMapper, new CSVFileParser(CSVFormat.DEFAULT));
    }

    public CSVReader<T> withResourceLoader(ResourceLoader resourceLoader) {
        setResourceLoader(resourceLoader);
        return this;
    }

    public CSVReader<T> withSkipHeader() {
        getParser().withSkipHeader();
        return this;
    }

    public CSVReader<T> withDelimiter(char delimiter) {
        getParser().withDelimiter(delimiter);
        return this;
    }

    public CSVReader<T> withFormat(CSVFormat csvFormat) {
        setParser(new CSVFileParser(csvFormat));
        return this;
    }

    public static CSVReader<String[]> defaultReader() {
        return new CSVReader<>(new ArrayBeanMapper());
    }

    public static <T> CSVReader<T> readerWithIndexMapping(Class<T> type, String[] properties) {
        return new CSVReader<>(new IndexBeanMapper<>(type, properties))
                .withFormat(CSVFormat.DEFAULT);
    }

    public static <T> CSVReader<T> readerWithHeaderMapping(Class<T> type,
                                                           String[] header,
                                                           String[] properties) {
        return new CSVReader<>(new HeaderBeanMapper<>(type, header, properties))
                .withFormat(CSVFormat.DEFAULT.withFirstRecordAsHeader());
    }

    @Override
    public <S> CSVReader<T> registerConverter(Class<S> type, Function<String, S> converter) {
        super.registerConverter(type, converter);
        return this;
    }

    @Override
    public <S> CSVReader<T> registerConverter(String property, Function<String, S> converter) {
        super.registerConverter(property, converter);
        return this;
    }

    @Override
    public CSVFileParser getParser() {
        return (CSVFileParser)parser;
    }

}
