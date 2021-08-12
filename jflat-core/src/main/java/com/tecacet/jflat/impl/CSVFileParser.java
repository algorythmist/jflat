package com.tecacet.jflat.impl;

import com.tecacet.jflat.CSVReader;
import com.tecacet.jflat.FlatFileParser;
import com.tecacet.jflat.RowRecord;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.IOException;
import java.io.Reader;
import java.util.Iterator;

public class CSVFileParser implements FlatFileParser {

    private CSVFormat csvFormat;

    public CSVFileParser(CSVFormat csvFormat) {
        this.csvFormat = csvFormat;
    }

    @Override
    public Iterable<RowRecord> parse(Reader reader) throws IOException {

        CSVParser parser = csvFormat.parse(reader);
        Iterator<CSVRecord> iterator = parser.iterator();
        return () -> new Iterator<RowRecord>() {
            @Override
            public boolean hasNext() {
                return iterator.hasNext();
            }

            @Override
            public RowRecord next() {
                return new CSVRowRecord(iterator.next());
            }
        };
    }

    public void withSkipHeader() {
        this.csvFormat = csvFormat.withFirstRecordAsHeader();
    }

    public void withDelimiter(char delimiter) {
        this.csvFormat = csvFormat.withDelimiter(delimiter);
    }
}
