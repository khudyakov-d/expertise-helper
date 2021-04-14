package ru.nsu.ccfit.khudyakov.expertise_helper.features.experts;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.nsu.ccfit.khudyakov.expertise_helper.features.experts.entities.Expert;
import ru.nsu.ccfit.khudyakov.expertise_helper.features.users.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ExpertRepository extends JpaRepository<Expert, UUID> {

    Expert findByEmail(String email);

    List<Expert> findByUser(User user);

    Page<Expert> findByUser(User user, Pageable pageable);

    Optional<Expert> findByUserAndId(User user, UUID expertId);

    @Query("select distinct e from Expert e " +
            "join e.invitations i " +
            "join i.application a " +
            "join a.project p " +
            "where p.id = :projectId")
    List<Expert> findInvolvedInProject(@Param("projectId") UUID projectId);

}