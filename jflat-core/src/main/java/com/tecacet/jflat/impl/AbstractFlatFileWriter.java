package com.tecacet.jflat.impl;

import com.tecacet.jflat.FlatFileWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Function;

public abstract class AbstractFlatFileWriter<T> implements FlatFileWriter<T> {

    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    protected Function<T, String> lineMapper = Object::toString;
    protected Function<T, String[]> tokenizer = null;
    protected String[] header = null;

    @Override
    public <S> FlatFileWriter<T> registerConverterForProperty(String property, Function<S, String> converter) {
        if (tokenizer instanceof BeanTokenizer) {
            ((BeanTokenizer) tokenizer).registerConverter(property, converter);
        } else {
            logger.warn("Tokenizer {} does not support converters", tokenizer);
        }
        return this;
    }

    @Override
    public <S> FlatFileWriter<T> registerConverterForClass(Class<S> clazz, Function<S, String> converter) {
        if (tokenizer instanceof BeanTokenizer) {
            ((BeanTokenizer) tokenizer).registerConverter(clazz, converter);
        } else {
            logger.warn("Tokenizer {} does not support converters", tokenizer);
        }
        return this;
    }

    @Override
    public AbstractFlatFileWriter<T> registerPropertyGetter(String property, Function<T, Object> getter) {
        if (tokenizer instanceof BeanTokenizer) {
            ((BeanTokenizer) tokenizer).registerPropertyGetter(property, getter);
        } else {
            logger.warn("Tokenizer {} does not support converters", tokenizer);
        }
        return this;
    }

    @Override
    public AbstractFlatFileWriter<T> withLineMapper(Function<T, String> mapper) {
        this.lineMapper = mapper;
        return this;
    }

    public AbstractFlatFileWriter<T> withHeader(String[] header) {
        this.header = header;
        return this;
    }
}
