package com.tecacet.jflat;

/**
 * Represents a row in a flat file
 * 
 * @author dimitri
 *
 */
public interface RowRecord {

	/**
	 * Access token in the row by index
	 * 
	 * @param index the index
	 * @return the record @index
	 */
	String get(int index);
	
	/**
	 * Access token in the row by name
	 * 
	 * @param name the name identifying the record
	 * @return the record of that name or null if it does not exist
	 */
	String get(String name);
	
	/**
	 * Number of tokens
	 * 
	 * @return the number of tokens
	 */
	int size();
	
	/**
	 * The row number in the file
	 * @return the row number
	 */
	long getRowNumber();

	default boolean skipped() {
		return false;
	}
}
