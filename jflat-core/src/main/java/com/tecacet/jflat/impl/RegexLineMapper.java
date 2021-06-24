package com.tecacet.jflat.impl;

import com.tecacet.jflat.LineMapper;
import com.tecacet.jflat.RowRecord;

public class RegexLineMapper implements LineMapper {

    private final String regex;

    public RegexLineMapper(String delimiter) {
        super();
        this.regex = delimiter;
    }

    @Override
    public RowRecord apply(Long lineNumber, String line) {
        return new ArrayRowRecord(lineNumber, line.split(regex));
    }

}
