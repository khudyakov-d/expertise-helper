package ru.nsu.ccfit.khudyakov.expertise_helper.features.docs.entities;

import lombok.Data;
import ru.nsu.ccfit.khudyakov.expertise_helper.features.experts.entities.Expert;
import ru.nsu.ccfit.khudyakov.expertise_helper.features.projects.entities.Project;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import java.time.LocalDate;

@Data
@Entity
public class ExpertContract {

    @EmbeddedId
    private ContractId contractId;

    @ManyToOne
    @MapsId("projectId")
    private Project project;

    @ManyToOne
    @MapsId("expertId")
    private Expert expert;

    private String contractNumber;

    private LocalDate contractDate;

}
