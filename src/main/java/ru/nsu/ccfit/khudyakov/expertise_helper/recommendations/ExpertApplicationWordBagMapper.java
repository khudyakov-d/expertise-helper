package ru.nsu.ccfit.khudyakov.expertise_helper.recommendations;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;
import ru.nsu.ccfit.khudyakov.expertise_helper.features.applications.entities.Application;
import ru.nsu.ccfit.khudyakov.expertise_helper.features.experts.entities.Expert;
import ru.nsu.ccfit.khudyakov.expertise_helper.features.invitation.InvitationService;
import ru.nsu.ccfit.khudyakov.expertise_helper.features.invitation.entities.Invitation;
import ru.nsu.ccfit.khudyakov.expertise_helper.recommendations.model.Doc;
import ru.nsu.ccfit.khudyakov.expertise_helper.recommendations.model.ExpertApplicationWordBag;

import java.util.List;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

@Mapper(componentModel = "spring")
public abstract class ExpertApplicationWordBagMapper {

    @Autowired
    private DataNormalizer dataNormalizer;

    @Autowired
    private InvitationService invitationService;

    @Mapping(source = "expert.id", target = "expertId")
    @Mapping(target = "applicationsWordBag", ignore = true)
    public abstract ExpertApplicationWordBag map(Expert expert);

    @AfterMapping
    protected void mapWordBag(Expert expert, @MappingTarget ExpertApplicationWordBag expertApplicationWordBag) {
        List<Application> applications = getApplications(expert);

        String words = applications.stream()
                .map(Application::getTopic)
                .collect(joining());

        List<String> terms = dataNormalizer.normalizeText(words);

        applications.forEach(a -> terms.add(a.getTopic()));
    }

    private List<Application> getApplications(Expert expert) {
        return expert.getInvitations().stream()
                .filter(invitationService::isInvitationCompleted)
                .map(Invitation::getApplication)
                .collect(toList());
    }

    public abstract List<Doc> map(List<Expert> experts);


}
