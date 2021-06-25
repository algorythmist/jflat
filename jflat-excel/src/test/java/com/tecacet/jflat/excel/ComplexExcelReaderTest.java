package com.tecacet.jflat.excel;

import com.tecacet.jflat.FlatFileReader;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ComplexExcelReaderTest {

    @Test
    void readSpreadsheetWithFormulas() throws IOException {
        String[] header = {"PX Code", "Date", "Description", "Start time", "End time", "Duration"};
        String[] properties = {"identifier", "date", "description", "startTime", "endTime", "duration"};

        FlatFileReader<TimeEntry> reader = ExcelReader.createWithHeaderMapping(TimeEntry.class, header, properties);

        List<TimeEntry> entries = reader.readAll("Timesheet.xls");
        TimeEntry entry = entries.get(0);
        assertEquals("5018", entry.getIdentifier());
        assertEquals(LocalDate.of(2010, 4, 1), entry.getDate());
        assertEquals("Ad Hoc Meetings", entry.getDescription());
        assertEquals("08:00", entry.getStartTime().toString());
        assertEquals("09:30", entry.getEndTime().toString());
        assertEquals(1.5, entry.getDuration().doubleValue(), .001);
    }
}
