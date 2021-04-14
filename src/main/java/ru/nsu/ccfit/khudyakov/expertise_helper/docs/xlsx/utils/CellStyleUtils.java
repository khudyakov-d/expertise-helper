package ru.nsu.ccfit.khudyakov.expertise_helper.docs.xlsx.utils;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class CellStyleUtils {

    public static XSSFCellStyle createNumericCellStyle(XSSFWorkbook workbook, XSSFCellStyle prevStyle) {
        XSSFCellStyle style = workbook.createCellStyle();
        style.cloneStyleFrom(prevStyle);
        style.setDataFormat((short) 4);

        return style;
    }

    public static XSSFCellStyle createBorderCellStyle(XSSFWorkbook workbook) {
        XSSFCellStyle style = workbook.createCellStyle();

        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setVerticalAlignment(VerticalAlignment.TOP);
        style.setWrapText(true);

        return style;
    }

}
