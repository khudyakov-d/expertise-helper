package ru.nsu.ccfit.khudyakov.expertise_helper.features.experts.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.NaturalId;
import ru.nsu.ccfit.khudyakov.expertise_helper.features.invitation.entities.Invitation;
import ru.nsu.ccfit.khudyakov.expertise_helper.features.users.User;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@ToString(exclude = {"user", "invitations"})
@EqualsAndHashCode(of = "email")
public class Expert {

    @Id
    @GeneratedValue
    private UUID id;

    @NotBlank
    private String name;

    @NotBlank
    private String organization;

    private String post;

    @NotNull
    @Enumerated(EnumType.ORDINAL)
    private ExpertDegree degree;

    @NotNull
    @Enumerated(EnumType.ORDINAL)
    private ExpertScienceCategory scienceCategory;

    @Email
    @NotBlank
    private String email;

    private String workPhone;

    private String personalPhone;

    private Date birthDate;

    @ManyToOne
    private User user;

    @OneToMany
    private List<Invitation> invitations;


}
