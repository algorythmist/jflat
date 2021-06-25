package com.tecacet.jflat.excel;

import java.time.LocalDate;

public class Quote {

	private LocalDate date;
	private double open;
	private double close;
	private long volume;

	public Quote(LocalDate date, double open, double close, long volume) {
		super();
		this.date = date;
		this.open = open;
		this.close = close;
		this.volume = volume;
	}

	public LocalDate getDate() {
		return date;
	}

	public double getOpen() {
		return open;
	}

	public double getClose() {
		return close;
	}

	public long getVolume() {
		return volume;
	}

	@Override
	public String toString() {
		return "Quote1 [date=" + date + ", open=" + open + ", close=" + close + ", volume=" + volume + "]";
	}

}
