package ru.nsu.ccfit.khudyakov.expertise_helper.docs.xlsx.utils;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.time.LocalDate;
import java.util.List;

public class SheetUtils {

    public static void setCellValue(XSSFCell rowCell, Object value) {
        if (value instanceof String) {
            rowCell.setCellValue((String) value);
        } else if (value instanceof Double) {
            rowCell.setCellValue((Double) value);
        } else if (value instanceof Integer) {
            rowCell.setCellValue((Integer) value);
        } else if (value instanceof LocalDate) {
            rowCell.setCellValue((LocalDate) value);
        }
    }

    public static void evaluateFormulas(XSSFWorkbook workbook) {
        FormulaEvaluator formulaEvaluator = workbook.getCreationHelper().createFormulaEvaluator();
        formulaEvaluator.evaluateAll();
    }

    public static void setCellSumFormula(XSSFCell cell, List<String> formulas) {
        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0, cellsSize = formulas.size() - 1; i < cellsSize; i++) {
            String formula = formulas.get(i);
            stringBuilder
                    .append(formula)
                    .append(" + ");
        }
        stringBuilder.append(formulas.get(formulas.size() - 1));
        cell.setCellFormula(stringBuilder.toString());
    }

    public static void setCellSumFormula(XSSFCell cell, int colNum, int startRowNum, int endRowNum) {
        String colName = CellReference.convertNumToColString(colNum);
        String startCellReference = colName + startRowNum;
        String endCellReference = colName + endRowNum;
        cell.setCellFormula(String.format("SUM(%s:%s)", startCellReference, endCellReference));
    }

    public static String getProductFormula(List<XSSFCell> cells) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0, cellsSize = cells.size() - 1; i < cellsSize; i++) {
            XSSFCell cell = cells.get(i);
            stringBuilder
                    .append(cell.getReference())
                    .append(" * ");
        }
        stringBuilder.append(cells.get(cells.size() - 1).getReference());
        return stringBuilder.toString();
    }

    public static void setCellProductFormula(XSSFCell resultCell, List<XSSFCell> cells) {
        String productFormula = getProductFormula(cells);
        resultCell.setCellFormula(productFormula);
    }

    public static void setCellProductFormula(XSSFCell resultCell, XSSFCell valueCell, double factor) {
        resultCell.setCellFormula(String.format("%s * %s", valueCell.getReference(), factor));
    }

    public static void setCellStringValue(XSSFSheet worksheet, int rowNum, int colNum, String value) {
        XSSFRow row = worksheet.getRow(rowNum);
        XSSFCell cell = row.getCell(colNum);
        cell.setCellValue(value);
    }

    public static void setCellDoubleValue(XSSFSheet worksheet, int rowNum, int colNum, double value) {
        XSSFRow row = worksheet.getRow(rowNum);
        XSSFCell cell = row.getCell(colNum);
        cell.setCellValue(value);
    }

    public static void shiftTable(XSSFSheet worksheet, int startRow, int n) {
        worksheet.shiftRows(startRow, worksheet.getLastRowNum(), n, true, true);
        fixReferences(worksheet);
    }

    public static void fixReferences(XSSFSheet sheet) {
        for (int r = sheet.getFirstRowNum(); r < sheet.getLastRowNum() + 1; r++) {
            XSSFRow row = sheet.getRow(r);
            if (row != null) {
                long rRef = row.getCTRow().getR();
                for (Cell cell : row) {
                    String cRef = ((XSSFCell) cell).getCTCell().getR();
                    ((XSSFCell) cell).getCTCell().setR(cRef.replaceAll("[0-9]", "") + rRef);
                }
            }
        }
    }

}
