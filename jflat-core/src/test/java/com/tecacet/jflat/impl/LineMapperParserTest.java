package com.tecacet.jflat.impl;

import com.tecacet.jflat.LineMapper;
import com.tecacet.jflat.RowRecord;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LineMapperParserTest {

    @Test
    void parse() throws IOException, URISyntaxException {
        LineMapper lineMapper = new FixedWidthLineMapper(new int[]{20, 10, 12});
        LineMapperParser lineMapperParser = new LineMapperParser(lineMapper, 1);


        URL resource = getClass().getClassLoader().getResource("directory.txt");
        File file = new File(resource.toURI());

        try (FileReader fw = new FileReader(file)) {
            Iterator<RowRecord> iterator = lineMapperParser.parse(fw).iterator();
            long row = 1;
            while(iterator.hasNext()) {
                RowRecord record = iterator.next();
                assertEquals(++row, record.getRowNumber());
            }
        }

    }
}