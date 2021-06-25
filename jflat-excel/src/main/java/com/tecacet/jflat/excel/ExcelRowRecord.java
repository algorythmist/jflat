package com.tecacet.jflat.excel;

import com.tecacet.jflat.RowRecord;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ExcelRowRecord implements RowRecord {

	private final CellMapper cellMapper = new CellMapper();

	private final Row row;
	private final Map<String, Integer> headerMap;

	public ExcelRowRecord(Row row, String[] header) {
		super();
		this.row = row;
		if (header == null) {
			headerMap = null;
		} else {
			headerMap = IntStream.range(0, header.length)
					.boxed()
					.collect(Collectors.toMap(i -> header[i], i -> i));
		}
	}

	@Override
	public String get(int index) {
		Cell cell = row.getCell(index);
		return cellMapper.getCellContent(cell);
	}

	@Override
	public String get(String name) {
		if (headerMap == null) {
			throw new IllegalArgumentException("No header is defined");
		}
		Integer i = headerMap.get(name);
		if (i == null) {
			return null;
		}
		return get(i);
	}

	@Override
	public int size() {
		return row.getLastCellNum() - row.getFirstCellNum();
	}

	@Override
	public long getRowNumber() {
		return row.getRowNum();
	}

}
