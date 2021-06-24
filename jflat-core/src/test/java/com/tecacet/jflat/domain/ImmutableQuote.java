package com.tecacet.jflat.domain;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;

public class ImmutableQuote {

	private final LocalDate date;
	private final BigDecimal open;
	private final BigDecimal close;
	private final BigInteger volume;
	private final BigDecimal adjustedClose;
	
	public ImmutableQuote(LocalDate date, BigDecimal open, BigDecimal close, BigInteger volume, BigDecimal adjustedClose) {
		super();
		this.date = date;
		this.open = open;
		this.close = close;
		this.volume = volume;
		this.adjustedClose = adjustedClose;
	}

	public LocalDate getDate() {
		return date;
	}

	public BigDecimal getOpen() {
		return open;
	}

	public BigDecimal getClose() {
		return close;
	}

	public BigInteger getVolume() {
		return volume;
	}

	public BigDecimal getAdjustedClose() {
		return adjustedClose;
	}
	
	
}
