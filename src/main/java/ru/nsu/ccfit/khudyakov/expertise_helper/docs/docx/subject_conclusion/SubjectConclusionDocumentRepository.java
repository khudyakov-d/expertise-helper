package ru.nsu.ccfit.khudyakov.expertise_helper.docs.docx.subject_conclusion;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface SubjectConclusionDocumentRepository extends MongoRepository<SubjectConclusionDocument, UUID> {
    Optional<SubjectConclusionDocument> findById(UUID id);

}
