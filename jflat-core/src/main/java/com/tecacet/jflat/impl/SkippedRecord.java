package com.tecacet.jflat.impl;

import com.tecacet.jflat.RowRecord;

public class SkippedRecord implements RowRecord {

    private final long rowNumber;
    private final String content;

    public SkippedRecord(long rowNumber, String content) {
        this.rowNumber = rowNumber;
        this.content = content;
    }

    @Override
    public String get(int index) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String get(String name) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int size() {
        return content.length();
    }

    @Override
    public long getRowNumber() {
        return rowNumber;
    }

    @Override
    public boolean skipped() {
        return true;
    }

    public String getContent() {
        return content;
    }
}
