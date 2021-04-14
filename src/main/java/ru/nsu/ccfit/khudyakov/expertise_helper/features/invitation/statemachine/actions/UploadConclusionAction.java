package ru.nsu.ccfit.khudyakov.expertise_helper.features.invitation.statemachine.actions;

import lombok.RequiredArgsConstructor;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;
import org.springframework.stereotype.Component;
import ru.nsu.ccfit.khudyakov.expertise_helper.features.invitation.InvitationService;
import ru.nsu.ccfit.khudyakov.expertise_helper.features.invitation.entities.Invitation;
import ru.nsu.ccfit.khudyakov.expertise_helper.features.invitation.statemachine.InvitationEvent;
import ru.nsu.ccfit.khudyakov.expertise_helper.features.invitation.statemachine.InvitationState;
import ru.nsu.ccfit.khudyakov.expertise_helper.files.FileManager;

import static ru.nsu.ccfit.khudyakov.expertise_helper.features.invitation.entities.InvitationStatus.COMPLETED;
import static ru.nsu.ccfit.khudyakov.expertise_helper.features.invitation.statemachine.StateMachineHeader.CONCLUSION_RESULT;

@Component
@RequiredArgsConstructor
public class UploadConclusionAction implements Action<InvitationState, InvitationEvent> {

    private final FileManager fileManager;

    private final InvitationService invitationService;

    @Override
    public void execute(StateContext<InvitationState, InvitationEvent> context) {
        byte[] bytes = (byte[]) context.getMessageHeader(CONCLUSION_RESULT.toString());
        Invitation invitation = invitationService.getInvitation(context.getStateMachine().getId());
        fileManager.save(invitation.getConclusionPath(), bytes);
        invitationService.changeInvitationStatus(invitation, COMPLETED);
    }

}

