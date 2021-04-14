package ru.nsu.ccfit.khudyakov.expertise_helper.features.applications.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import ru.nsu.ccfit.khudyakov.expertise_helper.features.invitation.entities.Invitation;
import ru.nsu.ccfit.khudyakov.expertise_helper.features.projects.entities.Project;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@ToString(exclude = {"invitations", "project"})
@EqualsAndHashCode(exclude = {"project", "invitations"})
public class Application {

    @Id
    @GeneratedValue
    private UUID id;

    @NotBlank
    private String topicNumber;

    @NotBlank
    private String topic;

    @NotBlank
    private String organization;

    @NotNull
    private Integer pagesCount;

    @NotBlank
    private String location;

    @ManyToOne
    private Project project;

    @OneToMany(mappedBy = "application", cascade = CascadeType.ALL)
    private List<Invitation> invitations;

}
