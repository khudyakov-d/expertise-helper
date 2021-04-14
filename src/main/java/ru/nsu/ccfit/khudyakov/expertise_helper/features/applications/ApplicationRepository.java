package ru.nsu.ccfit.khudyakov.expertise_helper.features.applications;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.nsu.ccfit.khudyakov.expertise_helper.features.applications.entities.Application;
import ru.nsu.ccfit.khudyakov.expertise_helper.features.users.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, UUID>  {

    List<Application> findAllByProjectId(UUID projectId);

    Optional<Application> findByProjectUserAndId(User user, UUID id);

}
