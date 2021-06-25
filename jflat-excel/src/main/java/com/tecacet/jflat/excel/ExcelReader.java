package com.tecacet.jflat.excel;

import com.tecacet.jflat.BeanMapper;
import com.tecacet.jflat.FlatFileReader;
import com.tecacet.jflat.FlatFileReaderCallback;
import com.tecacet.jflat.RowRecord;
import com.tecacet.jflat.converters.LocalDateConverter;
import com.tecacet.jflat.converters.LocalDateTimeConverter;
import com.tecacet.jflat.converters.LocalTimeConverter;
import com.tecacet.jflat.impl.AbstractFlatFileReader;
import com.tecacet.jflat.impl.GenericBeanMapper;
import com.tecacet.jflat.impl.HeaderBeanMapper;
import com.tecacet.jflat.impl.IndexBeanMapper;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

public class ExcelReader<T> extends AbstractFlatFileReader<T> {

    private final ExcelParser parser;
    private final BeanMapper<T> beanMapper;

    public ExcelReader(BeanMapper<T> beanMapper, boolean hasHeader) {
        this.beanMapper = beanMapper;
        this.parser = new ExcelParser(hasHeader);
        this.registerConverter(LocalDate.class, new LocalDateConverter(CellMapper.DEFAULT_DATETIME_FORMAT));
        this.registerConverter(LocalDateTime.class, new LocalDateTimeConverter(CellMapper.DEFAULT_DATETIME_FORMAT));
        this.registerConverter(LocalTime.class, new LocalTimeConverter(CellMapper.DEFAULT_DATETIME_FORMAT));
    }

    @Override
    public void read(InputStream is, FlatFileReaderCallback<T> callback) throws IOException {
        Iterable<RowRecord> records = parser.parse(is);
        for (RowRecord record : records) {
            T bean = beanMapper.apply(record);
            callback.accept(record, bean);
        }
    }

    @Override
    public Stream<T> readAsStream(Reader reader) throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Read to stream is not supported for excel");
    }

    @Override
    public Stream<RowRecord> readAsRecordStream(Reader reader) {
        throw new UnsupportedOperationException("Read to stream is not supported for excel");
    }


    @Override
    public <S> FlatFileReader<T> registerConverter(String property, Function<String, S> converter) {
        if (beanMapper instanceof GenericBeanMapper) {
            ((GenericBeanMapper) beanMapper).registerConverter(property, converter);
        }
        return this;
    }

    public static <T> ExcelReader<T> createWithHeaderMapping(Class<T> type,
                                                             String[] header,
                                                             String[] properties) {
        return new ExcelReader<>(new HeaderBeanMapper<>(type, header, properties), true);
    }

    public static <T> ExcelReader<T> createWithIndexMapping(Class<T> type,
                                                            String[] properties,
                                                            boolean skipHeaderRow) {
        return new ExcelReader<>(new IndexBeanMapper<>(type, properties), skipHeaderRow);
    }

}
