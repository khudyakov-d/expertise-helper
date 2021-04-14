package ru.nsu.ccfit.khudyakov.expertise_helper.docs.xlsx;

import lombok.RequiredArgsConstructor;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import ru.nsu.ccfit.khudyakov.expertise_helper.files.FileManager;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;

@RequiredArgsConstructor
public abstract class SheetTemplateBuilder<T, S extends BuilderOutputData> {

    private final String templatePath;

    private final FileManager fileManager;

    private XSSFWorkbook createTemplate() {
        try {
            return new XSSFWorkbook(new FileInputStream(fileManager.load(templatePath)));
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    protected abstract void writeData(XSSFWorkbook workbook, T data);

    protected abstract S getOutputData(byte[] sheetBytes);

    private S save(XSSFWorkbook workbook) {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            workbook.write(out);
            return getOutputData(out.toByteArray());
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    public S toSheet(T data) {
        try (XSSFWorkbook template = createTemplate()) {
            writeData(template, data);
            return save(template);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

}
