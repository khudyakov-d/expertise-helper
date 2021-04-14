package ru.nsu.ccfit.khudyakov.expertise_helper.features.docs;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.nsu.ccfit.khudyakov.expertise_helper.features.docs.entities.ContractId;
import ru.nsu.ccfit.khudyakov.expertise_helper.features.docs.entities.ExpertContract;

@Repository
public interface ContractRepository extends JpaRepository<ExpertContract, ContractId> {
}
