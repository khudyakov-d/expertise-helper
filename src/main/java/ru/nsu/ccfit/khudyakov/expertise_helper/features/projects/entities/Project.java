package ru.nsu.ccfit.khudyakov.expertise_helper.features.projects.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;
import ru.nsu.ccfit.khudyakov.expertise_helper.features.applications.entities.Application;
import ru.nsu.ccfit.khudyakov.expertise_helper.features.users.User;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Data
@Entity
@EqualsAndHashCode(exclude = {"user", "applications"})
public class Project {

    @Id
    @GeneratedValue
    private UUID id;

    @NotBlank
    private String title;

    @NotNull
    private Double baseRate;

    @NotNull
    private Integer requiredNumberExperts;

    @NotNull
    private ProjectType projectType;

    @NotBlank
    private String actPath;

    @NotBlank
    private String contractPath;

    @NotBlank
    private String conclusionPath;

    @NotNull
    @Convert(converter = Jsr310JpaConverters.LocalDateConverter.class)
    @Column(name = "creation_date")
    private LocalDate creationDate;

    @ManyToOne
    private User user;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
    private List<Application> applications;

}
