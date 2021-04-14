package ru.nsu.ccfit.khudyakov.expertise_helper.docs.docx.subject_conclusion;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface SubjectConclusionDocumentMapper {

    SubjectConclusionDocumentMapper INSTANCE = Mappers.getMapper(SubjectConclusionDocumentMapper.class);

    SubjectConclusion map(SubjectConclusionDocument subjectConclusionDocument);

}
