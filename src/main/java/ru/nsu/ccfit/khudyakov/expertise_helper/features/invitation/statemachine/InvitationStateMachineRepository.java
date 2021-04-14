package ru.nsu.ccfit.khudyakov.expertise_helper.features.invitation.statemachine;

import org.springframework.statemachine.data.jpa.JpaStateMachineRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InvitationStateMachineRepository extends JpaStateMachineRepository {

    boolean existsByMachineIdAndState(String uuid, String state);

}
