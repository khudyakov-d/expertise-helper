package ru.nsu.ccfit.khudyakov.expertise_helper.docs.docx.act;


import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@Data
@XmlRootElement(name = "act")
@XmlAccessorType(XmlAccessType.FIELD)
public class Act {

    private String number;

    private String contractorName;

    @XmlJavaTypeAdapter(ContractorDegreeXmlAdapter.class)
    private ContractorDegree contractorDegree;

    private Integer expertiseCount;

    private String expertiseCost;

    private String totalCost;

    private String contractCost;

    private String cursiveCost;

    private String contractorInitials;

}