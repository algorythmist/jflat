package com.tecacet.jflat.impl;

import com.tecacet.jflat.RecordExtractor;
import com.tecacet.jflat.RowRecord;

public class IndexRecordExtractor implements RecordExtractor {

    private final int[] indexes;

    public IndexRecordExtractor(int[] indexes) {
        this.indexes = indexes;
    }

    @Override
    public String getRecordValue(RowRecord record, int index) {
        return record.get(indexes[index]);
    }
}
