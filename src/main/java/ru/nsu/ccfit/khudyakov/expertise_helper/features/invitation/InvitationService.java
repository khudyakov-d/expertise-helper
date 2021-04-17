package ru.nsu.ccfit.khudyakov.expertise_helper.features.invitation;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.nsu.ccfit.khudyakov.expertise_helper.docs.docx.subject_conclusion.SubjectConclusionDocument;
import ru.nsu.ccfit.khudyakov.expertise_helper.docs.docx.subject_conclusion.SubjectConclusionDocumentRepository;
import ru.nsu.ccfit.khudyakov.expertise_helper.exceptions.NotFoundException;
import ru.nsu.ccfit.khudyakov.expertise_helper.exceptions.ServiceException;
import ru.nsu.ccfit.khudyakov.expertise_helper.features.applications.ApplicationService;
import ru.nsu.ccfit.khudyakov.expertise_helper.features.applications.entities.Application;
import ru.nsu.ccfit.khudyakov.expertise_helper.features.experts.ExpertService;
import ru.nsu.ccfit.khudyakov.expertise_helper.features.experts.entities.Expert;
import ru.nsu.ccfit.khudyakov.expertise_helper.features.invitation.entities.Invitation;
import ru.nsu.ccfit.khudyakov.expertise_helper.features.invitation.entities.InvitationStatus;
import ru.nsu.ccfit.khudyakov.expertise_helper.features.invitation.statemachine.InvitationStateMachineRepository;
import ru.nsu.ccfit.khudyakov.expertise_helper.features.users.User;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static java.nio.file.Paths.get;
import static java.time.LocalDate.now;
import static ru.nsu.ccfit.khudyakov.expertise_helper.features.invitation.entities.InvitationStatus.IN_PROCESS;
import static ru.nsu.ccfit.khudyakov.expertise_helper.features.invitation.statemachine.InvitationState.EXPERT_CONCLUSION_UPLOADING;

@Service
@RequiredArgsConstructor
public class InvitationService {

    private static final String CONCLUSIONS_DIR = "conclusions";

    private final InvitationRepository invitationRepository;

    private final SubjectConclusionDocumentRepository subjectConclusionDocumentRepository;

    private final ApplicationService applicationService;

    private final ExpertService expertService;

    private final InvitationStateMachineRepository stateMachineRepository;

    private String buildConclusionPath() {
        return get(CONCLUSIONS_DIR, now().toString(), UUID.randomUUID().toString() + ".docx").toString();
    }

    public void createInvitation(User user, UUID applicationId, UUID expertId, LocalDate localDate) {
        Application application = applicationService.findByUserAndId(user, applicationId);
        Expert expert = expertService.findByUserAndId(user, expertId);

        List<Invitation> invitations = application.getInvitations();
        Optional<Invitation> optional = invitations.stream()
                .filter(i -> i.getExpert().equals(expert))
                .findFirst();

        if (optional.isPresent()) {
            throw new ServiceException("application.add.error.expert.already.invited");
        }

        Invitation invitation = new Invitation();
        invitation.setStatus(IN_PROCESS);
        invitation.setApplication(application);
        invitation.setExpert(expert);
        invitation.setDeadlineDate(localDate);

        expert.getInvitations().add(invitation);
        invitations.add(invitation);

        invitationRepository.save(invitation);
    }

    public Invitation getInvitation(String invitationId) {
        return invitationRepository.findById(UUID.fromString(invitationId))
                .orElseThrow(NotFoundException::new);
    }

    public Invitation getInvitation(User user, UUID invitationId) {
        return invitationRepository.findByApplicationProjectUserAndId(user, invitationId)
                .orElseThrow(NotFoundException::new);
    }

    public List<Invitation> getAllByApplicationId(User user, UUID applicationId) {
        return invitationRepository.findAllByApplicationProjectUserAndApplicationId(user, applicationId);
    }

    public List<Invitation> getAllByApplicationIdAndStatus(User user, UUID applicationId, InvitationStatus status) {
        return invitationRepository.findAllByApplicationProjectUserAndApplicationIdAndStatus(user,
                applicationId,
                status);
    }

    public void setInvitationConclusionPath(Invitation invitation) {
        invitation.setConclusionPath(buildConclusionPath());
        invitationRepository.save(invitation);
    }

    public void addSubjectConclusion(User user, UUID invitationId, SubjectConclusionDocument subjectConclusion) {
        Invitation invitation = getInvitation(user, invitationId);
        subjectConclusion.setId(invitation.getId());
        subjectConclusionDocumentRepository.save(subjectConclusion);
    }

    public void changeInvitationStatus(Invitation invitation, InvitationStatus invitationStatus) {
        if (invitationStatus == null) {
            return;
        }

        invitation.setStatus(invitationStatus);
        invitationRepository.save(invitation);
    }

    public boolean isInvitationApproved(Invitation invitation) {
        if (invitation.getStatus().equals(InvitationStatus.COMPLETED)) {
            return true;
        }

        if (invitation.getStatus().equals(InvitationStatus.IN_PROCESS)) {
            return stateMachineRepository.existsByMachineIdAndState(
                    invitation.getId().toString(),
                    EXPERT_CONCLUSION_UPLOADING.toString()
            );
        }

        return false;
    }

    public boolean isInvitationCompleted(Invitation invitation) {
        return invitation.getStatus().equals(InvitationStatus.COMPLETED);
    }

    public Invitation getInitiationByExpertAndApplication(Expert expert, Application application) {
        return invitationRepository.findByExpertAndApplication(expert, application)
                .orElseThrow(IllegalStateException::new);
    }

}
