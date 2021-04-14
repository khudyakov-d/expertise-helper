package ru.nsu.ccfit.khudyakov.expertise_helper.docs.docx.contract;

import lombok.Data;
import ru.nsu.ccfit.khudyakov.expertise_helper.docs.docx.act.ContractorDegree;
import ru.nsu.ccfit.khudyakov.expertise_helper.docs.docx.act.ContractorDegreeXmlAdapter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@Data
@XmlRootElement(name = "contract")
@XmlAccessorType(XmlAccessType.FIELD)
public class Contract {

    private String number;

    private String contractorName;

    @XmlJavaTypeAdapter(ContractorDegreeXmlAdapter.class)
    private ContractorDegree contractorDegree;

    private Integer count;

    private String cost;

    private String contractorCost;

    private String contractorInitials;

}