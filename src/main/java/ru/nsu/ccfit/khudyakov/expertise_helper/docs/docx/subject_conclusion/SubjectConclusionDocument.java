package ru.nsu.ccfit.khudyakov.expertise_helper.docs.docx.subject_conclusion;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

@Data
@Document(collection = "subject-conclusions")
public class SubjectConclusionDocument {

    @Id
    private UUID id;

    private String organizationName;

    private String founderName;

    private String scientificTopic;

    private String scientificTopicCode;

    private String registrationNumber;

}
