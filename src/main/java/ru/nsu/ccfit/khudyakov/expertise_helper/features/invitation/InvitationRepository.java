package ru.nsu.ccfit.khudyakov.expertise_helper.features.invitation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.nsu.ccfit.khudyakov.expertise_helper.features.applications.entities.Application;
import ru.nsu.ccfit.khudyakov.expertise_helper.features.experts.entities.Expert;
import ru.nsu.ccfit.khudyakov.expertise_helper.features.invitation.entities.Invitation;
import ru.nsu.ccfit.khudyakov.expertise_helper.features.invitation.entities.InvitationStatus;
import ru.nsu.ccfit.khudyakov.expertise_helper.features.users.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface InvitationRepository extends JpaRepository<Invitation, UUID> {

    List<Invitation> findAllByApplicationProjectUserAndApplicationId(User user, UUID applicationId);

    List<Invitation> findAllByApplicationProjectUserAndApplicationIdAndStatus(User user,
                                                                              UUID applicationId,
                                                                              InvitationStatus status);

    Optional<Invitation> findByApplicationProjectUserAndId(User user, UUID id);

    Optional<Invitation> findByExpertAndApplication(Expert expert, Application application);

}
