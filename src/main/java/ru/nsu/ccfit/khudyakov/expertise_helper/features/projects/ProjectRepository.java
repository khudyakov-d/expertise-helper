package ru.nsu.ccfit.khudyakov.expertise_helper.features.projects;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.nsu.ccfit.khudyakov.expertise_helper.features.projects.entities.Project;
import ru.nsu.ccfit.khudyakov.expertise_helper.features.users.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProjectRepository extends JpaRepository<Project, UUID> {

    boolean existsByTitleAndUser(String title, User user);

    boolean existsByUserAndId(User user, UUID id);

    Optional<Project> findByUserAndId(User user, UUID id);

    List<Project> findAllByUser(User user);

}
