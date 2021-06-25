package com.tecacet.jflat.excel;

import com.tecacet.jflat.RowRecord;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

public class ExcelParser {

    private final ExcelRowTokenizer tokenizer = new ExcelRowTokenizer();
    private final boolean hasHeader;

    public ExcelParser(boolean hasHeader) {
        this.hasHeader = hasHeader;
    }

    public Iterable<RowRecord> parse(InputStream is) throws IOException {
        Workbook workbook = WorkbookFactory.create(is);
        Sheet sheet = workbook.getSheetAt(0);
        Iterator<Row> iterator = sheet.iterator();
        final String[] header = (hasHeader && iterator.hasNext()) ? tokenizer.apply(iterator.next()) : null;
        return () -> new Iterator<RowRecord>() {
            @Override
            public boolean hasNext() {
                return iterator.hasNext();
            }

            @Override
            public RowRecord next() {
                return new ExcelRowRecord(iterator.next(), header);
            }
        };
    }

}
