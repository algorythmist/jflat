package com.tecacet.jflat.impl;

import com.tecacet.jflat.LineMapper;
import com.tecacet.jflat.RowRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.function.Predicate;

/**
 * Parses a line with fixed width fields
 * 
 * @author Dimitri Papaioannou
 * 
 */
public class FixedWidthLineMapper implements LineMapper {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	private final int[] widths;
	private final boolean trimWhitespace;
	private final int totalWidth;

	private Predicate<String> skipPredicate = s -> false;


	/**
	 * 
	 * @param widths
	 *            the width of each token in the line
	 */
	public FixedWidthLineMapper(int[] widths) {
		this(widths, true);
	}

	public FixedWidthLineMapper(int[] widths, boolean trimWhitespace) {
		super();
		this.widths = widths;
		this.trimWhitespace = trimWhitespace;
		totalWidth = Arrays.stream(widths).sum();
	}

	@Override
	public RowRecord apply(Long lineNumber, final String originalLine) {
		String line = originalLine;
		//check if line should be skipped
		if (skipPredicate.test(line)) {
			return new SkippedRecord(lineNumber, line);
		}
		if (line.length() < totalWidth) {
			logger.warn("Line {} has length {}, but expected at least a length of {}. Will attempt padding.",
					lineNumber, line.length(), totalWidth);
			line = rightPad(line, totalWidth);
		}
		int lastIndex = 0;
		String[] tokens = new String[widths.length];
		for (int i = 0; i < widths.length; i++) {
			String thisToken = line.substring(lastIndex, lastIndex + widths[i]);
			if (trimWhitespace) {
				thisToken = thisToken.trim();
			}
			if (thisToken.isEmpty()) {
				thisToken = null;
			}
			tokens[i] = thisToken;
			lastIndex += widths[i];
		}
		return new ArrayRowRecord(lineNumber, tokens);
	}

	public Predicate<String> getSkipPredicate() {
		return skipPredicate;
	}

	public void addSkipPredicate(Predicate<String> predicate) {
		this.skipPredicate = this.skipPredicate.or(predicate);
	}

	private static String rightPad(String original, int length) {
		return String.format("%-" + length + "s", original);
	}
}
