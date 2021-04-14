package ru.nsu.ccfit.khudyakov.expertise_helper.features.invitation.statemachine.actions;

import lombok.RequiredArgsConstructor;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;
import org.springframework.stereotype.Component;
import ru.nsu.ccfit.khudyakov.expertise_helper.docs.docx.subject_conclusion.SubjectConclusionDocument;
import ru.nsu.ccfit.khudyakov.expertise_helper.docs.docx.subject_conclusion.SubjectConclusionDocumentRepository;
import ru.nsu.ccfit.khudyakov.expertise_helper.docs.docx.subject_conclusion.SubjectConclusionTemplateBuilder;
import ru.nsu.ccfit.khudyakov.expertise_helper.features.invitation.InvitationService;
import ru.nsu.ccfit.khudyakov.expertise_helper.features.invitation.entities.Invitation;
import ru.nsu.ccfit.khudyakov.expertise_helper.features.invitation.statemachine.InvitationEvent;
import ru.nsu.ccfit.khudyakov.expertise_helper.features.invitation.statemachine.InvitationState;
import ru.nsu.ccfit.khudyakov.expertise_helper.files.FileManager;

import java.io.File;

@Component
@RequiredArgsConstructor
public class ConclusionGenerateAction implements Action<InvitationState, InvitationEvent> {

    private final FileManager fileManager;

    private final SubjectConclusionDocumentRepository subjectConclusionDocumentRepository;

    private final InvitationService invitationService;

    @Override
    public void execute(StateContext<InvitationState, InvitationEvent> context) {
        Invitation invitation = invitationService.getInvitation(context.getStateMachine().getId());

        switch (invitation.getApplication().getProject().getProjectType()) {
            case SUBJECT:
                fillSubjectConclusion(invitation);
                break;
            default:
                throw new IllegalStateException();
        }

        context.getStateMachine().sendEvent(InvitationEvent.CONCLUSION_GENERATE_SUCCESS);
    }

    private void fillSubjectConclusion(Invitation invitation) {
        SubjectConclusionDocument conclusion = subjectConclusionDocumentRepository
                .findById(invitation.getId())
                .orElseGet(SubjectConclusionDocument::new);

        invitationService.setInvitationConclusionPath(invitation);

        File template = fileManager.load(invitation.getApplication().getProject().getConclusionPath());
        SubjectConclusionTemplateBuilder templateBuilder = new SubjectConclusionTemplateBuilder(template, fileManager);
        templateBuilder.saveToDocx(conclusion, invitation.getConclusionPath());
    }

}
