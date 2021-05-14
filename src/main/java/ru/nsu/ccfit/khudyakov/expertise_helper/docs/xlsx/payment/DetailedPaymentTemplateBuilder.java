package ru.nsu.ccfit.khudyakov.expertise_helper.docs.xlsx.payment;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import ru.nsu.ccfit.khudyakov.expertise_helper.docs.xlsx.SheetTemplateBuilder;
import ru.nsu.ccfit.khudyakov.expertise_helper.files.FileManager;

import java.util.Arrays;
import java.util.List;

import static ru.nsu.ccfit.khudyakov.expertise_helper.docs.xlsx.utils.CellStyleUtils.createBorderCellStyle;
import static ru.nsu.ccfit.khudyakov.expertise_helper.docs.xlsx.utils.CellStyleUtils.createNumericCellStyle;
import static ru.nsu.ccfit.khudyakov.expertise_helper.docs.xlsx.utils.SheetUtils.*;


public class DetailedPaymentTemplateBuilder extends SheetTemplateBuilder<DetailedPayment, DetailedPaymentOutputData> {

    private static final int START_TABLE_ROW = 10;

    private static final int CONTRACT_ROW = 2;

    private static final int CONTRACT_COL = 7;

    private static final int BASE_COST_COL = 4;

    private static final int CONTRACTOR_FACTOR_COL = 5;

    private static final int REPORT_PAGES_FACTOR_COL = 7;

    private final DetailedPaymentOutputData outputData;

    public DetailedPaymentTemplateBuilder(String templatePath, FileManager fileManager) {
        super(templatePath, fileManager);
        outputData = new DetailedPaymentOutputData();
    }

    private void fillNumber(int number, XSSFRow row, XSSFCellStyle style) {
        XSSFCell cell = row.createCell(0);
        cell.setCellStyle(style);
        cell.setCellValue(number);
    }

    private void fillPayment(XSSFWorkbook workbook, XSSFRow row, ApplicationPayment payment, XSSFCellStyle style) {
        List<Object> fields = payment.getFieldsAsList();
        for (int j = 0; j < fields.size(); j++) {
            XSSFCell rowCell = row.createCell(j + 1);

            Object value = fields.get(j);
            setCellValue(workbook, rowCell, value);

            rowCell.setCellStyle(style);
        }
    }

    private void fillPaymentCost(int col, XSSFRow row, XSSFCellStyle style) {
        XSSFCell cell = row.createCell(col);
        cell.setCellStyle(style);
        setCellProductFormula(
                cell,
                Arrays.asList(
                        row.getCell(BASE_COST_COL),
                        row.getCell(CONTRACTOR_FACTOR_COL),
                        row.getCell(REPORT_PAGES_FACTOR_COL)
                )
        );
        cell.getNumericCellValue();
    }

    private void fillPaymentWithTaxesCost(XSSFWorkbook workbook, int colNum, int paymentsSize) {
        XSSFSheet sheet = workbook.getSheetAt(0);

        XSSFCell sumCell = sheet
                .getRow(START_TABLE_ROW + paymentsSize)
                .getCell(colNum);

        XSSFCellStyle numericCellStyle = createNumericCellStyle(workbook, sumCell.getCellStyle());

        setCellSumFormula(sumCell, colNum, START_TABLE_ROW, START_TABLE_ROW + paymentsSize);
        sumCell.setCellStyle(numericCellStyle);

        XSSFCell resultCell = sheet
                .getRow(START_TABLE_ROW + paymentsSize + 1)
                .getCell(colNum);

        setCellProductFormula(resultCell, sumCell, 1.271f);
        resultCell.setCellStyle(numericCellStyle);
        evaluateFormulas(workbook);

        outputData.setCost(sumCell.getNumericCellValue());
        outputData.setCostWithTaxes(resultCell.getNumericCellValue());
    }

    @Override
    protected void writeData(XSSFWorkbook workbook, DetailedPayment data) {
        XSSFSheet sheet = workbook.getSheetAt(0);

        setCellStringValue(sheet, CONTRACT_ROW, CONTRACT_COL, "По договору № " + data.getNumber());

        List<ApplicationPayment> payments = data.getPayments();

        shiftTable(sheet, START_TABLE_ROW, payments.size());
        XSSFCellStyle borderCellStyle = createBorderCellStyle(workbook);

        int paymentCellCount = ApplicationPayment.class.getDeclaredFields().length + 1;
        for (int i = 0, size = payments.size(); i < size; i++) {
            XSSFRow row = sheet.createRow(START_TABLE_ROW + i);

            ApplicationPayment payment = payments.get(i);
            fillNumber(i + 1, row, borderCellStyle);
            fillPayment(workbook, row, payment, borderCellStyle);
            fillPaymentCost(paymentCellCount, row, borderCellStyle);
        }
        fillPaymentWithTaxesCost(workbook, paymentCellCount, payments.size());
    }

    @Override
    protected DetailedPaymentOutputData getOutputData(byte[] sheetBytes) {
        outputData.setSheetBytes(sheetBytes);
        return outputData;
    }

}
