package com.tecacet.jflat;

import com.tecacet.jflat.impl.AbstractFlatFileWriter;
import com.tecacet.jflat.impl.BeanTokenizer;
import com.tecacet.jflat.impl.FixedWidthLineMerger;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import java.io.IOException;
import java.io.Writer;
import java.util.Collection;
import java.util.function.Function;

public class CSVWriter<T> extends AbstractFlatFileWriter<T> {

    private String[] header = null;
    private CSVFormat format = CSVFormat.DEFAULT;

    @Override
    public void write(Writer writer, Collection<T> beans) throws IOException {
        try (CSVPrinter csvPrinter = new CSVPrinter(writer, format)) {
            if (header != null) {
                csvPrinter.printRecord(header);
            }
            for (T bean : beans) {
                if (tokenizer != null) {
                    csvPrinter.printRecord(tokenizer.apply(bean));
                } else {
                    csvPrinter.printRecord(lineMapper.apply(bean));
                }
            }
        }
    }

    public static <T> CSVWriter<T> writerForProperties(String[] properties) {
        return new CSVWriter<T>().withTokenizer(new BeanTokenizer<>(properties));
    }

    public static CSVWriter<String[]> defaultWriter(String[] properties) {
        return new CSVWriter<String[]>().withTokenizer(new BeanTokenizer<>(properties));
    }

    @Override
    public CSVWriter<T> withLineMapper(Function<T, String> mapper) {
        super.withLineMapper(mapper);
        return this;
    }

    public CSVWriter<T> withTokenizer(Function<T, String[]> tokenizer) {
        this.tokenizer = tokenizer;
        return CSVWriter.this;
    }

    public CSVWriter<T> withHeader(String[] header) {
        this.header = header;
        return this;
    }

    public CSVWriter<T> withFormat(CSVFormat format) {
        this.format = format;
        return this;
    }

    @Override
    public <S> CSVWriter<T> registerConverterForProperty(String property, Function<S, String> converter) {
        super.registerConverterForProperty(property, converter);
        return this;
    }

    @Override
    public <S> CSVWriter<T> registerConverterForClass(Class<S> clazz, Function<S, String> converter) {
        super.registerConverterForClass(clazz, converter);
        return this;
    }
}
