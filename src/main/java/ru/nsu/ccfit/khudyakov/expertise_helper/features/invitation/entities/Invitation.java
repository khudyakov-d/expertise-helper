package ru.nsu.ccfit.khudyakov.expertise_helper.features.invitation.entities;

import lombok.Data;
import lombok.ToString;
import ru.nsu.ccfit.khudyakov.expertise_helper.features.applications.entities.Application;
import ru.nsu.ccfit.khudyakov.expertise_helper.features.experts.entities.Expert;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.UUID;

@Data
@Entity
@ToString(exclude = "expert")
public class Invitation {

    @Id
    @GeneratedValue
    private UUID id;

    private String conclusionPath;

    @NotNull
    private LocalDate deadlineDate;

    @NotNull
    @Enumerated(EnumType.ORDINAL)
    private InvitationStatus status;

    @ManyToOne
    private Application application;

    @ManyToOne(cascade = CascadeType.ALL)
    private Expert expert;

}
