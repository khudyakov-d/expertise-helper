package ru.nsu.ccfit.khudyakov.expertise_helper.features.invitation;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.service.StateMachineService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.nsu.ccfit.khudyakov.expertise_helper.features.invitation.entities.Invitation;
import ru.nsu.ccfit.khudyakov.expertise_helper.features.invitation.entities.InvitationStatus;
import ru.nsu.ccfit.khudyakov.expertise_helper.features.invitation.statemachine.InvitationEvent;
import ru.nsu.ccfit.khudyakov.expertise_helper.features.invitation.statemachine.InvitationState;
import ru.nsu.ccfit.khudyakov.expertise_helper.features.users.User;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static ru.nsu.ccfit.khudyakov.expertise_helper.features.invitation.statemachine.InvitationEvent.*;
import static ru.nsu.ccfit.khudyakov.expertise_helper.features.invitation.statemachine.StateMachineHeader.CONCLUSION_RESULT;

@Service
@RequiredArgsConstructor
public class InvitationMachineService {

    private final InvitationService invitationService;

    private final StateMachineService<InvitationState, InvitationEvent> stateMachineService;

    public Invitation getInvitation(User user, UUID invitationId) {
        return invitationService.getInvitation(user, invitationId);
    }

    private synchronized StateMachine<InvitationState, InvitationEvent> getStateMachine(UUID invitationId) {
        return stateMachineService.acquireStateMachine(invitationId.toString());
    }

    private void sendEvent(UUID invitationId, InvitationEvent event) {
        StateMachine<InvitationState, InvitationEvent> stateMachine = getStateMachine(invitationId);
        stateMachine.sendEvent(event);
        stateMachineService.releaseStateMachine(invitationId.toString());
    }

    private void sendEvent(UUID invitationId, Message<InvitationEvent> event) {
        StateMachine<InvitationState, InvitationEvent> stateMachine = getStateMachine(invitationId);
        stateMachine.sendEvent(event);
        stateMachineService.releaseStateMachine(invitationId.toString());
    }

    public List<Invitation> getInvitations(User user, UUID applicationId) {
        return invitationService.getAllByApplicationId(user,applicationId);
    }

    public Map<Invitation, InvitationState> getInProcessInvitations(User user, UUID applicationId) {
        List<Invitation> invitations = invitationService.getAllByApplicationIdAndStatus(user,
                applicationId,
                InvitationStatus.IN_PROCESS);

        Map<Invitation, InvitationState> invitationMap = new HashMap<>();

        for (Invitation invitation : invitations) {
            InvitationState state = getStateMachine(invitation.getId()).getState().getId();
            invitationMap.put(invitation, state);
        }

        return invitationMap;
    }

    public void conclusionGenerateEvent(UUID invitationId) {
        sendEvent(invitationId, CONCLUSION_GENERATE);
    }

    public void conclusionGeneratingRetryEvent(UUID invitationId) {
        sendEvent(invitationId, CONCLUSION_GENERATE_RETRY);
    }

    public void sendInvitationEmailSkipEvent(UUID invitationId) {
        sendEvent(invitationId, SEND_INVITATION_EMAIL_SKIP);
    }

    public void sendInvitationEmailEvent(UUID invitationId) {
        sendEvent(invitationId, SEND_INVITATION_EMAIL);
    }

    public void sendInvitationEmailRetryEvent(UUID invitationId) {
        sendEvent(invitationId, SEND_INVITATION_RETRY);
    }

    public void sendInvitationEmailRepeatEvent(UUID invitationId) {
        sendEvent(invitationId, SEND_INVITATION_EMAIL_REPEAT);
    }

    public void sendInvitationEmailContinueEvent(UUID invitationId) {
        sendEvent(invitationId, SEND_INVITATION_EMAIL_CONTINUE);
    }

    public void expertRejectEvent(UUID invitationId) {
        sendEvent(invitationId, EXPERT_REJECT);
    }

    public void expertIgnoreEvent(UUID invitationId) {
        sendEvent(invitationId, EXPERT_IGNORE);
    }

    public void expertUploadingResultEvent(UUID invitationId) {
        sendEvent(invitationId, EXPERT_RESULT_UPLOADING);
    }

    public void expertUploadResultEvent(UUID invitationId, MultipartFile file) {
        try {
            if (file == null) {
                return;
            }

            Message<InvitationEvent> message = MessageBuilder
                    .withPayload(EXPERT_RESULT_UPLOADED)
                    .setHeader(CONCLUSION_RESULT.toString(), file.getBytes())
                    .build();

            sendEvent(invitationId, message);
        } catch (IOException e) {
            throw new IllegalStateException();
        }
    }

}
