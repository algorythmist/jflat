package com.tecacet.jflat.impl;

import com.tecacet.jflat.RowRecord;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * RowRecord implementation for Commons CSV
 * 
 * @author dimitri
 *
 */
public class CSVRowRecord implements RowRecord {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private final CSVRecord csvRecord;

    public CSVRowRecord(CSVRecord csvRecord) {
        super();
        this.csvRecord = csvRecord;
    }

    @Override
    public String get(int index) {
        return csvRecord.get(index);
    }

    @Override
    public String get(String name) {
        if (csvRecord.isSet(name)) {
            return csvRecord.get(name);
        }
        logger.warn("There is no header mapping for column '{}'", name);
        return null;
    }

    @Override
    public int size() {
        return csvRecord.size();
    }

    @Override
    public long getRowNumber() {
        return csvRecord.getRecordNumber();
    }

}
