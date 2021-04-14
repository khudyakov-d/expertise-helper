package ru.nsu.ccfit.khudyakov.expertise_helper.docs.docx.act;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class ContractorDegreeXmlAdapter extends XmlAdapter<String, ContractorDegree> {
    @Override
    public ContractorDegree unmarshal(String s) {
        return ContractorDegree.valueOf(s);
    }

    @Override
    public String marshal(ContractorDegree contractorDegree) {
        return contractorDegree.toString();
    }
}
