package ru.nsu.ccfit.khudyakov.expertise_helper.docs.docx.subject_conclusion;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@Data
@XmlRootElement(name = "conclusion")
@XmlAccessorType(XmlAccessType.FIELD)
public class SubjectConclusion {

    private String organizationName;

    private String founderName;

    private String scientificTopic;

    private String scientificTopicCode;

    private String registrationNumber;


}
