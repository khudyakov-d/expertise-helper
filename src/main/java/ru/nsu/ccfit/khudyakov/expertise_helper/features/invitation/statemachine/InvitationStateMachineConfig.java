package ru.nsu.ccfit.khudyakov.expertise_helper.features.invitation.statemachine;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.core.task.SyncTaskExecutor;
import org.springframework.statemachine.config.EnableStateMachineFactory;
import org.springframework.statemachine.config.EnumStateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;
import org.springframework.statemachine.persist.StateMachineRuntimePersister;
import org.springframework.stereotype.Component;
import ru.nsu.ccfit.khudyakov.expertise_helper.features.invitation.statemachine.actions.ConclusionGenerateAction;
import ru.nsu.ccfit.khudyakov.expertise_helper.features.invitation.statemachine.actions.ExpertRejectAction;
import ru.nsu.ccfit.khudyakov.expertise_helper.features.invitation.statemachine.actions.ExpertIgnoreAction;
import ru.nsu.ccfit.khudyakov.expertise_helper.features.invitation.statemachine.actions.SendInvitationEmailAction;
import ru.nsu.ccfit.khudyakov.expertise_helper.features.invitation.statemachine.actions.UploadConclusionAction;
import ru.nsu.ccfit.khudyakov.expertise_helper.features.invitation.statemachine.errors.ConclusionGenerateErrorAction;
import ru.nsu.ccfit.khudyakov.expertise_helper.features.invitation.statemachine.errors.SendInvitationEmailErrorAction;

import javax.transaction.Transactional;
import java.util.EnumSet;

import static ru.nsu.ccfit.khudyakov.expertise_helper.features.invitation.statemachine.InvitationEvent.*;
import static ru.nsu.ccfit.khudyakov.expertise_helper.features.invitation.statemachine.InvitationState.*;

@Component
@Configurable
@RequiredArgsConstructor
@EnableStateMachineFactory
@SuppressWarnings("all")
public class InvitationStateMachineConfig extends EnumStateMachineConfigurerAdapter<InvitationState, InvitationEvent> {

    private final ConclusionGenerateAction conclusionGenerateAction;

    private final ConclusionGenerateErrorAction conclusionGenerateErrorAction;

    private final SendInvitationEmailAction sendInvitationEmailAction;

    private final SendInvitationEmailErrorAction sendInvitationEmailErrorAction;

    private final UploadConclusionAction uploadConclusionAction;

    private final ExpertIgnoreAction expertIgnoreAction;

    private final ExpertRejectAction expertRejectAction;

    private final StateMachineRuntimePersister<InvitationState, InvitationEvent, String> stateMachineRuntimePersister;

    @Override
    @Transactional
    public void configure(StateMachineConfigurationConfigurer<InvitationState, InvitationEvent> config) throws Exception {
        config
                .withConfiguration()
                .taskExecutor(new SyncTaskExecutor())
                .and()
                .withPersistence()
                .runtimePersister(stateMachineRuntimePersister);
    }

    @Override
    public void configure(StateMachineStateConfigurer<InvitationState, InvitationEvent> states) throws Exception {
        states.
                withStates()
                .initial(CONCLUSION_NEW)
                .stateEntry(CONCLUSION_GENERATING, conclusionGenerateAction, conclusionGenerateErrorAction)
                .stateEntry(INVITATION_EMAIL_SENDING, sendInvitationEmailAction, sendInvitationEmailErrorAction)
                .end(EXPERT_REJECTED)
                .end(EXPERT_NO_ANSWER)
                .end(EXPERT_CONCLUSION_UPLOADED)
                .states(EnumSet.allOf(InvitationState.class));
    }

    @Override
    public void configure(StateMachineTransitionConfigurer<InvitationState, InvitationEvent> transitions) throws Exception {
        transitions
                .withExternal()
                .source(CONCLUSION_NEW)
                .target(CONCLUSION_GENERATING)
                .event(CONCLUSION_GENERATE)

                .and()
                .withExternal()
                .source(CONCLUSION_GENERATING)
                .target(CONCLUSION_GENERATING_ERROR)
                .event(CONCLUSION_GENERATE_FAILURE)

                .and()
                .withExternal()
                .source(CONCLUSION_GENERATING_ERROR)
                .target(CONCLUSION_NEW)
                .event(CONCLUSION_GENERATE_RETRY)

                .and()
                .withExternal()
                .source(CONCLUSION_GENERATING)
                .target(CONCLUSION_GENERATED)
                .event(CONCLUSION_GENERATE_SUCCESS)

                .and()
                .withExternal()
                .source(CONCLUSION_GENERATED)
                .target(INVITATION_EMAIL_SENDING)
                .event(SEND_INVITATION_EMAIL)

                .and()
                .withExternal()
                .source(CONCLUSION_GENERATED)
                .target(INVITATION_DECISION_MAKING)
                .event(SEND_INVITATION_EMAIL_SKIP)

                .and()
                .withExternal()
                .source(INVITATION_EMAIL_SENDING)
                .target(INVITATION_EMAIL_SENT)
                .event(SEND_INVITATION_EMAIL_SUCCESS)

                .and()
                .withExternal()
                .source(INVITATION_EMAIL_SENDING)
                .target(INVITATION_EMAIL_SENDING_ERROR)
                .event(SEND_INVITATION_EMAIL_FAILURE)

                .and()
                .withExternal()
                .source(INVITATION_EMAIL_SENDING_ERROR)
                .target(CONCLUSION_GENERATED)
                .event(SEND_INVITATION_EMAIL_RETRY)

                .and()
                .withExternal()
                .source(INVITATION_EMAIL_SENT)
                .target(INVITATION_EMAIL_SENDING)
                .event(SEND_INVITATION_EMAIL_REPEAT)

                .and()
                .withExternal()
                .source(INVITATION_EMAIL_SENT)
                .target(INVITATION_DECISION_MAKING)
                .event(SEND_INVITATION_EMAIL_CONTINUE)

                .and()
                .withExternal()
                .source(INVITATION_DECISION_MAKING)
                .target(EXPERT_NO_ANSWER)
                .event(EXPERT_IGNORE)
                .action(expertIgnoreAction)

                .and()
                .withExternal()
                .source(INVITATION_DECISION_MAKING)
                .target(EXPERT_REJECTED)
                .event(EXPERT_REJECT)
                .action(expertRejectAction)

                .and()
                .withExternal()
                .source(INVITATION_DECISION_MAKING)
                .target(EXPERT_CONCLUSION_UPLOADING)
                .event(EXPERT_RESULT_UPLOADING)

                .and()
                .withExternal()
                .source(EXPERT_CONCLUSION_UPLOADING)
                .target(EXPERT_CONCLUSION_UPLOADED)
                .event(EXPERT_RESULT_UPLOADED)
                .action(uploadConclusionAction);

    }

}
