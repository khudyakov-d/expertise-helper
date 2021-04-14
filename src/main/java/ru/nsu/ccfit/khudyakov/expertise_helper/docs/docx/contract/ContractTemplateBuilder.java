package ru.nsu.ccfit.khudyakov.expertise_helper.docs.docx.contract;

import org.w3c.dom.Document;
import ru.nsu.ccfit.khudyakov.expertise_helper.docs.docx.DocxTemplateBuilder;
import ru.nsu.ccfit.khudyakov.expertise_helper.files.FileManager;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;

import static javax.xml.bind.Marshaller.JAXB_FORMATTED_OUTPUT;


public class ContractTemplateBuilder extends DocxTemplateBuilder<Contract> {

    private final Marshaller jaxbMarshaller;

    public ContractTemplateBuilder(File template, FileManager fileManager) {
        super(template, fileManager);

        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(Contract.class);
            jaxbMarshaller = jaxbContext.createMarshaller();
            jaxbMarshaller.setProperty(JAXB_FORMATTED_OUTPUT, true);
        } catch (JAXBException e) {
            e.printStackTrace();
            throw new IllegalStateException(e);
        }
    }

    @Override
    protected Document initDocument(Contract data) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            Document doc = factory.newDocumentBuilder().newDocument();
            jaxbMarshaller.marshal(data, doc);

            return doc;
        } catch (ParserConfigurationException | JAXBException e) {
            throw new IllegalStateException(e);
        }
    }

}
