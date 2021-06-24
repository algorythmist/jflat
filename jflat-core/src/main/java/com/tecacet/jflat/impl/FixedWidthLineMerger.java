package com.tecacet.jflat.impl;

import com.tecacet.jflat.FieldTooWideException;
import com.tecacet.jflat.LineMerger;
import com.tecacet.jflat.LineMergerException;
import com.tecacet.jflat.TooManyFieldsException;


/**
 * Merge tokens using fixed width format
 */
public class FixedWidthLineMerger implements LineMerger {

    private final int[] widths;
    private final String format;
    private final String EMPTY_STRING = "";
    private boolean truncateFields = false;

    public FixedWidthLineMerger(int[] widths) {
        super();
        this.widths = widths;
        this.format = buildFormat();
    }

    @Override
    public String makeLine(String[] elements) throws LineMergerException {
        convertNullsToEmptyStrings(elements);
        validateFields(elements);
        return String.format(format, (Object[]) elements);
    }

    private String buildFormat() {
        StringBuilder sb = new StringBuilder();
        for (int width : widths) {
            //This means a left-justified string of <width> characters truncated
            String format = "%-" + width + "." + width + "s";
            sb.append(format);
        }
        sb.append("\n"); //TODO system sep?
        return sb.toString();
    }

    private void validateFields(String[] elements) throws LineMergerException {
        if (elements.length > widths.length) {
            throw new TooManyFieldsException(elements.length, widths.length);
        }
        if (truncateFields) {
            return;
        }
        for (int i = 0; i < elements.length; i++) {
            if (elements[i] == null) {
                continue;
            }
            if (elements[i].length() > widths[i]) {
                throw new FieldTooWideException(elements[i], widths[i]);
            }
        }
    }

    private void convertNullsToEmptyStrings(String[] elements) {
        for (int i = 0; i < elements.length; i++) {
            if (elements[i] == null) {
                elements[i] = EMPTY_STRING;
            }
        }
    }

    public void setTruncateFields(boolean truncateFields) {
        this.truncateFields = truncateFields;
    }
}
