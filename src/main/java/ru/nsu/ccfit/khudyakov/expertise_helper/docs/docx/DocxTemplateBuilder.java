package ru.nsu.ccfit.khudyakov.expertise_helper.docs.docx;

import lombok.RequiredArgsConstructor;
import org.docx4j.Docx4J;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.w3c.dom.Document;
import ru.nsu.ccfit.khudyakov.expertise_helper.files.FileManager;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import static org.docx4j.Docx4J.*;

@RequiredArgsConstructor
public abstract class DocxTemplateBuilder<T> {

    private final File template;

    private final FileManager fileManager;

    protected abstract Document initDocument(T data);

    private WordprocessingMLPackage createTemplate() {
        try {
            return load(template);
        } catch (Docx4JException e) {
            e.printStackTrace();
            throw new IllegalStateException(e);
        }
    }

    private void bindDoc(WordprocessingMLPackage template, Document document) {
        try {
            Docx4J.bind(template, document, FLAG_BIND_INSERT_XML | FLAG_BIND_BIND_XML | FLAG_BIND_REMOVE_SDT);
        } catch (Docx4JException e) {
            e.printStackTrace();
            throw new IllegalStateException(e);
        }
    }

    private WordprocessingMLPackage prepareTemplate(T data) {
        Document document = initDocument(data);
        WordprocessingMLPackage template = createTemplate();
        bindDoc(template, document);
        return template;
    }

    public void saveToDocx(T data, String resultPath) {
        try {
            WordprocessingMLPackage template = prepareTemplate(data);
            save(template, fileManager.createAndGet(resultPath));
        } catch (Docx4JException e) {
            throw new IllegalStateException(e);
        }
    }

    public byte[] saveToDocx(T data) {
        try (ByteArrayOutputStream bufferedOutputStream = new ByteArrayOutputStream()) {
            WordprocessingMLPackage template = prepareTemplate(data);
            save(template, bufferedOutputStream);
            return bufferedOutputStream.toByteArray();
        } catch (Docx4JException | IOException e) {
            throw new IllegalStateException(e);
        }
    }

}
