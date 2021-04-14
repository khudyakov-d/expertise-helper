package ru.nsu.ccfit.khudyakov.expertise_helper.features.users;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import ru.nsu.ccfit.khudyakov.expertise_helper.features.experts.entities.Expert;
import ru.nsu.ccfit.khudyakov.expertise_helper.features.projects.entities.Project;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.List;
import java.util.UUID;

@Entity(name = "usr")
@Data
@EqualsAndHashCode(of = "email")
@ToString(exclude = {"experts", "projects"})
public class User {

    @Id
    @GeneratedValue
    private UUID id;

    private String email;

    private String name;

    private String mailPassword;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Expert> experts;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Project> projects;

}
