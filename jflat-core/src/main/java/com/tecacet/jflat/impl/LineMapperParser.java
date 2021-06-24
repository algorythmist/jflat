package com.tecacet.jflat.impl;

import com.tecacet.jflat.FlatFileParser;
import com.tecacet.jflat.LineMapper;
import com.tecacet.jflat.RowRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.Reader;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Parse a file via a mapper
 */
public class LineMapperParser implements FlatFileParser {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final LineMapper lineMapper;
    private int skipRows;

    public LineMapperParser(LineMapper lineMapper) {
        this(lineMapper, 0);
    }

    public LineMapperParser(LineMapper lineMapper, int skipRows) {
        this.lineMapper = lineMapper;
        this.skipRows = skipRows;
    }

    @Override
    public Iterable<RowRecord> parse(Reader reader) {
        BufferedReader bufferedReader = new BufferedReader(reader);
        Iterator<String> iterator = bufferedReader.lines().iterator();
        int rows = skipRows(iterator);
        logger.debug("Skipping the first {} rows.", rows);

        AtomicLong lineNumber = new AtomicLong(rows);
        return () -> new Iterator<RowRecord>() {
            @Override
            public boolean hasNext() {
                return iterator.hasNext();
            }

            @Override
            public RowRecord next() {
                long line = lineNumber.incrementAndGet();
                return lineMapper.apply(line, iterator.next());
            }
        };

    }

    private int skipRows(Iterator<String> iterator) {
        int rows = 0;
        while (iterator.hasNext() && rows < skipRows) {
            iterator.next();
            rows++;
        }
        return rows;
    }

    public int getSkipRows() {
        return skipRows;
    }

    public void setSkipRows(int skipRows) {
        this.skipRows = skipRows;
    }
}
