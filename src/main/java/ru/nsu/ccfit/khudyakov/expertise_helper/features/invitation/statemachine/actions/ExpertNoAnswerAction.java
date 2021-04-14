package ru.nsu.ccfit.khudyakov.expertise_helper.features.invitation.statemachine.actions;

import lombok.RequiredArgsConstructor;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;
import org.springframework.stereotype.Component;
import ru.nsu.ccfit.khudyakov.expertise_helper.features.invitation.InvitationService;
import ru.nsu.ccfit.khudyakov.expertise_helper.features.invitation.entities.Invitation;
import ru.nsu.ccfit.khudyakov.expertise_helper.features.invitation.statemachine.InvitationEvent;
import ru.nsu.ccfit.khudyakov.expertise_helper.features.invitation.statemachine.InvitationState;

import static ru.nsu.ccfit.khudyakov.expertise_helper.features.invitation.entities.InvitationStatus.NO_ANSWER;

@Component
@RequiredArgsConstructor
public class ExpertNoAnswerAction implements Action<InvitationState, InvitationEvent> {

    private final InvitationService invitationService;

    @Override
    public void execute(StateContext<InvitationState, InvitationEvent> context) {
        Invitation invitation = invitationService.getInvitation(context.getStateMachine().getId());
        invitationService.changeInvitationStatus(invitation, NO_ANSWER);
    }

}

