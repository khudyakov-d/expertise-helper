package ru.nsu.ccfit.khudyakov.expertise_helper.features.invitation.statemachine;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.data.jpa.JpaPersistingStateMachineInterceptor;
import org.springframework.statemachine.data.jpa.JpaStateMachineRepository;
import org.springframework.statemachine.persist.StateMachineRuntimePersister;
import org.springframework.statemachine.service.DefaultStateMachineService;
import org.springframework.statemachine.service.StateMachineService;

@Configuration
@ComponentScan
@SuppressWarnings("all")
public class StateMachineServiceConfig {

    @Bean
    public StateMachineRuntimePersister<InvitationState, InvitationEvent, String> stateMachineRuntimePersister(
            JpaStateMachineRepository jpaStateMachineRepository
    ) {
        return new JpaPersistingStateMachineInterceptor<>(jpaStateMachineRepository);
    }

    @Bean
    public StateMachineService<InvitationState, InvitationEvent> stateMachineService(
            StateMachineFactory<InvitationState, InvitationEvent> stateMachineFactory,
            StateMachineRuntimePersister<InvitationState, InvitationEvent, String> stateMachineRuntimePersister
    ) {
        return new DefaultStateMachineService<>(stateMachineFactory, stateMachineRuntimePersister);
    }

}
