package com.tecacet.jflat;

import com.tecacet.jflat.impl.IndexBeanMapper;
import com.tecacet.jflat.impl.*;

import java.util.function.Function;

public class FixedWidthReader<T> extends GenericFlatFileReader<T> {

    private final FixedWidthLineMapper lineMapper;

    public FixedWidthReader(BeanMapper<T> beanMapper, int[] widths) {
        this(beanMapper, new FixedWidthLineMapper(widths));
    }

    protected FixedWidthReader(BeanMapper<T> beanMapper, FixedWidthLineMapper lineMapper) {
        super(beanMapper, new LineMapperParser(lineMapper));
        this.lineMapper = lineMapper;
    }

    public static FixedWidthReader<String[]> createDefaultReader(int[] widths) {
        return new FixedWidthReader<>(new ArrayBeanMapper(), widths);
    }

    public static <T> FixedWidthReader<T> createWithIndexMapping(Class<T> type, String[] properties, int[] widths) {
        return new FixedWidthReader<>(new IndexBeanMapper<>(type, properties), widths);
    }

    @Override
    public <S> FixedWidthReader<T> registerConverter(Class<S> type, Function<String, S> converter) {
        super.registerConverter(type, converter);
        return this;
    }

    @Override
    public <S> FixedWidthReader<T> registerConverter(String property, Function<String, S> converter) {
        super.registerConverter(property, converter);
        return this;
    }


    //TODO: Make following methods generic for readers

    public FixedWidthReader<T> withSkipRows(int skipRows) {
        ((LineMapperParser) super.parser).setSkipRows(skipRows);
        return this;
    }

    public FixedWidthReader<T> withSkipEmptyLines() {
        lineMapper.addSkipPredicate(line -> line.trim().length() == 0);
        return this;
    }

    public FixedWidthReader<T> withSkipComments(String commentIdentifier) {
        lineMapper.addSkipPredicate(line -> line.trim().startsWith(commentIdentifier));
        return this;
    }


}
