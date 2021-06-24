package com.tecacet.jflat.impl;

import com.tecacet.jflat.*;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GenericFlatFileReaderTest {

    @Test
    void readAll() throws IOException {
        FlatFileReader<String[]> fileReader = buildReader();
        List<String[]> records = fileReader.readAll("directory.txt");
        assertEquals(4, records.size());
        assertEquals("[John Smith, WA, 418-311-4111]", Arrays.toString(records.get(1)));
    }

    @Test
    void readAsStream() throws IOException {
        FlatFileReader<String[]> fileReader = buildReader();
        InputStream is = ClassLoader.getSystemResourceAsStream("directory.txt");
        Stream<String[]> records = fileReader.readAsStream(is);
        List<String> names = records.skip(1).map(array -> array[0]).collect(Collectors.toList());
        assertEquals("[John Smith, Mary Hartford, Evan Nolan]", names.toString());
    }

    private FlatFileReader<String[]> buildReader() {
        BeanMapper<String[]> beanMapper = new ArrayBeanMapper();
        LineMapper lineMapper = new FixedWidthLineMapper(new int[] {20,10,12});
        FlatFileParser parser = new LineMapperParser(lineMapper);
        return new GenericFlatFileReader<>(beanMapper, parser);
    }

}
