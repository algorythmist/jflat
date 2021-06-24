package com.tecacet.jflat;

import com.tecacet.jflat.impl.AbstractFlatFileWriter;
import com.tecacet.jflat.impl.BeanTokenizer;
import com.tecacet.jflat.impl.FixedWidthLineMerger;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Collection;
import java.util.function.Function;

public class FixedWidthWriter<T> extends AbstractFlatFileWriter<T> {

    private final LineMerger lineMerger;

    public FixedWidthWriter(int[] widths, String[] properties) {
        this(new FixedWidthLineMerger(widths), properties);
    }

    public FixedWidthWriter(LineMerger lineMerger, String[] properties) {
        this.lineMerger = lineMerger;
        super.tokenizer = new BeanTokenizer<>(properties);
    }

    public FixedWidthWriter(LineMerger lineMerger, Function<T, String[]> tokenizer) {
        this.lineMerger = lineMerger;
        super.tokenizer = tokenizer;
    }

    @Override
    public void write(Writer writer, Collection<T> beans) throws IOException {
        try (BufferedWriter bf = new BufferedWriter(writer)) {
            if (super.header != null) {
                String line = lineMerger.makeLine(header);
                bf.write(line);
            }
            for (T bean : beans) {
                String[] tokens = tokenizer.apply(bean);
                String line = lineMerger.makeLine(tokens);
                bf.write(line);
            }
        }
    }

    public static FixedWidthWriter<String[]> defaultWriter(int[] widths) {
        return new FixedWidthWriter<>(new FixedWidthLineMerger(widths), s -> s);
    }

    public FixedWidthWriter<T> withHeader(String[] header) {
        return (FixedWidthWriter<T>) super.withHeader(header);
    }
}
