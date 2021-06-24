package com.tecacet.jflat;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * Parse an input source into a collection of records
 */
public interface FlatFileParser {

    Iterable<RowRecord> parse(Reader reader) throws IOException;

    default Iterable<RowRecord> parse(InputStream is) throws IOException {
        try (InputStreamReader reader = new InputStreamReader(is)) {
            return parse(reader);
        }
    }

    default Stream<RowRecord> parseStream(Reader reader) throws IOException {
        return StreamSupport.stream(parse(reader).spliterator(), false);
    }
}
