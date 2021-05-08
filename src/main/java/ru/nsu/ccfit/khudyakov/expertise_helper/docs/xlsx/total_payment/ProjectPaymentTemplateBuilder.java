package ru.nsu.ccfit.khudyakov.expertise_helper.docs.xlsx.total_payment;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import ru.nsu.ccfit.khudyakov.expertise_helper.docs.utils.CostFormatter;
import ru.nsu.ccfit.khudyakov.expertise_helper.docs.xlsx.SheetTemplateBuilder;
import ru.nsu.ccfit.khudyakov.expertise_helper.files.FileManager;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import static ru.nsu.ccfit.khudyakov.expertise_helper.docs.xlsx.utils.CellStyleUtils.createBorderCellStyle;
import static ru.nsu.ccfit.khudyakov.expertise_helper.docs.xlsx.utils.CellStyleUtils.createNumericCellStyle;
import static ru.nsu.ccfit.khudyakov.expertise_helper.docs.xlsx.utils.SheetUtils.*;

public class ProjectPaymentTemplateBuilder extends SheetTemplateBuilder<TotalPayment, SheetTemplateBuilder.BuilderOutputData> {

    private static final int BASE_COST_ROW = 0;

    private static final int BASE_COST_COL = 12;

    private static final int PAGE_FACTOR_ROW = 2;

    private static final int PAGE_FACTOR_START_COL = 7;

    private static final int START_ROW = 3;

    private static final int DEGREE_FACTOR_COL = 5;

    private static final int PAYMENT_COST_COL = 12;

    public ProjectPaymentTemplateBuilder(String templatePath, FileManager fileManager) {
        super(templatePath, fileManager);
    }

    @Override
    protected void writeData(XSSFWorkbook workbook, TotalPayment data) {
        fillBaseCost(workbook, data);
        fillPageFactors(workbook, data);
        fillExpertPayments(workbook, data);
        evaluateFormulas(workbook);
    }

    private void fillBaseCost(XSSFWorkbook workbook, TotalPayment data) {
        XSSFSheet sheet = workbook.getSheetAt(0);

        String cost = CostFormatter.formatCost(data.getBaseCost());
        String baseCost = MessageFormat.format("расчет по формуле (база - {0})", cost);
        setCellStringValue(sheet, BASE_COST_ROW, BASE_COST_COL, baseCost);
        setCellDoubleValue(sheet, PAGE_FACTOR_ROW, BASE_COST_COL, data.getBaseCost());
    }

    private void fillPageFactors(XSSFWorkbook workbook, TotalPayment data) {
        XSSFSheet sheet = workbook.getSheetAt(0);
        PageFactor[] pageFactors = PageFactor.values();

        for (int i = 0; i < pageFactors.length; i++) {
            PageFactor pageFactor = pageFactors[i];
            setCellDoubleValue(sheet, PAGE_FACTOR_ROW, PAGE_FACTOR_START_COL + i, pageFactor.getValue());
        }
    }

    private void fillExpertPayments(XSSFWorkbook workbook, TotalPayment data) {
        XSSFSheet sheet = workbook.getSheetAt(0);

        List<ExpertProjectPayment> payments = data.getPayments();
        XSSFCellStyle borderCellStyle = createBorderCellStyle(workbook);
        XSSFCellStyle numericCellStyle = createNumericCellStyle(workbook, borderCellStyle);

        for (int i = 0, paymentsSize = payments.size(); i < paymentsSize; i++) {
            ExpertProjectPayment payment = payments.get(i);
            XSSFRow row = sheet.getRow(START_ROW + i);

            fillPayment(workbook, i + 1, row, payment, borderCellStyle);
            fillPaymentCost(sheet.getRow(PAGE_FACTOR_ROW), row, numericCellStyle);
            fillPaymentTotalCost(row, numericCellStyle);
        }
    }

    private void fillPaymentTotalCost(XSSFRow row, XSSFCellStyle style) {
        XSSFCell cell = row.getCell(PAYMENT_COST_COL);
        XSSFCell resultCell = row.createCell(PAYMENT_COST_COL + 1);
        setCellProductFormula(resultCell, cell, 1.271d);
        resultCell.setCellStyle(style);
    }

    private void fillPaymentCost(XSSFRow pageFactorsRow, XSSFRow row, XSSFCellStyle style) {
        List<String> formulas = new ArrayList<>();

        PageFactor[] pageFactors = PageFactor.values();
        for (int i = 0; i < pageFactors.length; i++) {
            String productFormula = getProductFormula(
                    List.of(
                            row.getCell(DEGREE_FACTOR_COL),
                            row.getCell(PAGE_FACTOR_START_COL + i),
                            pageFactorsRow.getCell(PAGE_FACTOR_START_COL + i),
                            pageFactorsRow.getCell(BASE_COST_COL)
                    )
            );
            formulas.add(productFormula);
        }

        XSSFCell cell = row.createCell(PAYMENT_COST_COL);
        cell.setCellStyle(style);
        setCellSumFormula(cell, formulas);
    }

    private void fillPayment(XSSFWorkbook workbook,
                             int number,
                             XSSFRow row,
                             ExpertProjectPayment payment,
                             XSSFCellStyle style) {
        List<Object> fields = payment.getFieldsAsList();

        XSSFCell rowCell = row.createCell(0);
        rowCell.setCellValue(number);
        rowCell.setCellStyle(style);

        for (int j = 0; j < fields.size(); j++) {
            rowCell = row.createCell(j + 1);

            Object value = fields.get(j);

            rowCell.setCellStyle(style);
            setCellValue(workbook, rowCell, value);
        }
    }

    @Override
    protected BuilderOutputData getOutputData(byte[] sheetBytes) {
        BuilderOutputData builderOutputData = new BuilderOutputData();
        builderOutputData.setSheetBytes(sheetBytes);
        return builderOutputData;
    }

}
