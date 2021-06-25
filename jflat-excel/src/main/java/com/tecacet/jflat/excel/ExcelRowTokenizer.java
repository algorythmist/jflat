package com.tecacet.jflat.excel;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class ExcelRowTokenizer implements Function<Row, String[]>{

	private final CellMapper cellMapper = new CellMapper();

	@Override
	public String[] apply(Row row) {
		List<String> tokens = new ArrayList<>();
		for (int c = row.getFirstCellNum(); c < row.getLastCellNum(); c++) {
			Cell cell = row.getCell(c);
			String cellValue = "";
			if (cell != null) {
				cellValue = cellMapper.getCellContent(cell);
			}
			tokens.add(cellValue);
		}
		return tokens.toArray(new String[tokens.size()]);
	}

}
