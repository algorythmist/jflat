package com.tecacet.jflat.impl;

import com.tecacet.jflat.RecordExtractor;
import com.tecacet.jflat.RowRecord;

public class HeaderRecordExtractor implements RecordExtractor {

    private final String[] header;

    public HeaderRecordExtractor(String[] header) {
        this.header = header;
    }

    @Override
    public String getRecordValue(RowRecord record, int index) {
        return record.get(header[index]);
    }
}
