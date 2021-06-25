package com.tecacet.jflat.excel;

import org.apache.poi.ss.usermodel.*;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CellMapper {

    public static final String DEFAULT_DATETIME_FORMAT = "yyyy-MM-dd hh:mm a";

    private final NumberFormat numberFormat = new DecimalFormat("#.#####");
    private final DateFormat dateFormat = new SimpleDateFormat(DEFAULT_DATETIME_FORMAT);

    public String getCellContent(Cell cell) {
        if (cell == null) {
            return "";
        }

        CellType cellType = cell.getCellType();
        switch (cellType) {
            case STRING:
                return cell.getRichStringCellValue().getString();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    Date date = cell.getDateCellValue();
                    return dateFormat.format(date);
                } else {
                    double d = cell.getNumericCellValue();
                    // TODO find a flexible enough format for all numeric types
                    return numberFormat.format(d);
                    // return Double.toString(d);
                }
            case BOOLEAN:
                boolean b = cell.getBooleanCellValue();
                return Boolean.toString(b);
            case FORMULA:
                return evaluateFormula(cell);
            case BLANK:
                return "";
            case ERROR:
                byte bt = cell.getErrorCellValue();
                return Byte.toString(bt);
            default:
                return cell.getStringCellValue();
        }
    }

    private String evaluateFormula(Cell cell) {
        FormulaEvaluator evaluator = cell.getSheet().getWorkbook().getCreationHelper().createFormulaEvaluator();
        CellValue cellValue = evaluator.evaluate(cell);
        switch (cellValue.getCellType()) {
            case BOOLEAN:
                boolean b = cellValue.getBooleanValue();
                return Boolean.toString(b);
            case NUMERIC:
                double d = cellValue.getNumberValue();
                return numberFormat.format(d);
            case BLANK:
                return "";
            case ERROR:
                byte bt = cellValue.getErrorValue();
                return Byte.toString(bt);
            case STRING:
            default:
                return cellValue.getStringValue();
        }

    }
}
