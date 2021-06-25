package com.tecacet.jflat.excel;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ExcelReaderTest {

    @Test
    void readNewFormat() throws IOException {
        test("GLD.xlsx");
    }

    @Test
    void readOldFormat() throws IOException {
        test("GLD.xls");
    }

    @Test
    void readWithHeaderMap() throws IOException {
        ExcelReader<Quote> reader = ExcelReader.createWithHeaderMapping(Quote.class,
                new String[]{"Date", "Open", "Close", "Volume"},
                new String[]{"date", "open", "close", "volume"});
        test("GLD.xlsx", reader);
    }

    @Test
    void readWithoutHeader() throws IOException {
        ExcelReader<Quote> reader = ExcelReader.createWithIndexMapping(Quote.class,
                new String[]{"date", "open", "close", "volume"}, false);
        test("GLDNH.xlsx", reader);
    }

    private void test(String filename) throws IOException {
        ExcelReader<Quote> reader = ExcelReader.createWithIndexMapping(Quote.class,
                new String[]{"date", "open", null, null, "close", "volume", null}, true);
        test(filename, reader);
    }

    private void test(String filename, ExcelReader<Quote> reader) throws IOException {

        List<Quote> rows = reader.readAll(filename);
        assertEquals(134, rows.size());
        Quote firstRow = rows.get(0);
        assertEquals("Quote1 [date=2015-12-01, open=102.3, close=101.46, volume=5800200]",
                firstRow.toString());
    }
}