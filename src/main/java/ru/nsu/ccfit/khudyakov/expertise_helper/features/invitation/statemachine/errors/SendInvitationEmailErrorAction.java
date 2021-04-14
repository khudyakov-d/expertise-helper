package ru.nsu.ccfit.khudyakov.expertise_helper.features.invitation.statemachine.errors;

import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;
import org.springframework.stereotype.Component;
import ru.nsu.ccfit.khudyakov.expertise_helper.features.invitation.statemachine.InvitationEvent;
import ru.nsu.ccfit.khudyakov.expertise_helper.features.invitation.statemachine.InvitationState;

@Component
public class SendInvitationEmailErrorAction implements Action<InvitationState, InvitationEvent> {

    @Override
    public void execute(StateContext<InvitationState, InvitationEvent> context) {
        context.getStateMachine().sendEvent(InvitationEvent.SEND_INVITATION_FAILURE);
    }

}
