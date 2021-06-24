package com.tecacet.jflat;

public class FieldTooWideException extends LineMergerException {

    private final String fieldValue;
    private final int maxFieldWidth;
    
    public FieldTooWideException(String fieldValue, int maxFieldWidth) {
        super();
        this.fieldValue = fieldValue;
        this.maxFieldWidth = maxFieldWidth;
    }
    
    @Override
    public String getMessage(){
        String messageFormat = "Value '%s' is too wide.  Actual width: %d, Maximum width: %d.";
        return String.format( messageFormat, fieldValue, fieldValue.length(), maxFieldWidth);
    }
    
    public String getFieldValue() {
        return fieldValue;
    }
    
    public int getMaxFieldWidth() {
        return maxFieldWidth;
    }
}
