package com.tecacet.jflat;

public class TooManyFieldsException extends LineMergerException {

    private final int fieldCount;
    private final int maxFieldCount;
    
    public TooManyFieldsException(int fieldCount, int maxFieldCount) {
        super();
        this.fieldCount = fieldCount;
        this.maxFieldCount = maxFieldCount;
    }
    
    @Override
    public String getMessage(){
        return String.format("Too many fields.  Got: %d, Maximum: %d.", fieldCount, maxFieldCount);
    }
    
    public int getFieldCount() {
        return fieldCount;
    }
    
    public int getMaxFieldCount() {
        return maxFieldCount;
    }
}

