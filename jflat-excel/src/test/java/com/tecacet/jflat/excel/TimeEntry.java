package com.tecacet.jflat.excel;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

public class TimeEntry {

	private String identifier;

	private String description;

	private LocalDate date;

	private LocalTime startTime;

	private LocalTime endTime;

	private BigDecimal duration;

	private String project;

	public String getIdentifier() {
		return identifier;
	}

	public LocalDate getDate() {
		return date;
	}

	public LocalTime getStartTime() {
		return startTime;
	}

	public LocalTime getEndTime() {
		return endTime;
	}

	public String getDescription() {
		return description;
	}

	public BigDecimal getDuration() {
		return duration;
	}

	public String getProject() {
		return project;
	}

	@Override
	public String toString() {
		return String.format("%s, %s, %s", identifier, date, description);
	}

}
