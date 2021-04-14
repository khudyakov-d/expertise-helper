package ru.nsu.ccfit.khudyakov.expertise_helper.features.projects.mappers;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import ru.nsu.ccfit.khudyakov.expertise_helper.features.projects.dtos.NewProjectDto;
import ru.nsu.ccfit.khudyakov.expertise_helper.features.projects.dtos.ProjectInfoDto;
import ru.nsu.ccfit.khudyakov.expertise_helper.features.projects.entities.Project;

import java.time.LocalDate;
import java.util.List;

import static java.nio.file.Paths.get;
import static java.time.LocalDate.now;
import static java.util.UUID.randomUUID;


@Mapper(componentModel = "spring")
public abstract class ProjectMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "creationDate", ignore = true)
    @Mapping(target = "conclusionPath", ignore = true)
    @Mapping(target = "actPath", ignore = true)
    @Mapping(target = "contractPath", ignore = true)
    @Mapping(target = "applications", ignore = true)
    abstract public Project toProject(NewProjectDto newProjectDto);

    @AfterMapping
    protected void mapProps(@MappingTarget Project project) {
        LocalDate now = now();
        project.setActPath(getTemplatePath("templates/" + now + "/acts"));
        project.setContractPath(getTemplatePath("templates/" + now + "/contracts"));
        project.setConclusionPath(getTemplatePath("templates/" + now + "/conclusions"));
        project.setCreationDate(now);
    }

    private String getTemplatePath(String s) {
        return get(s, randomUUID().toString() + ".docx").toString();
    }

    abstract public List<ProjectInfoDto> toProjectDto(List<Project> projects);

}
