package ru.nsu.ccfit.khudyakov.expertise_helper.features.applications.mappers;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import ru.nsu.ccfit.khudyakov.expertise_helper.features.applications.dtos.ApplicationDto;
import ru.nsu.ccfit.khudyakov.expertise_helper.features.applications.dtos.NewApplicationDto;
import ru.nsu.ccfit.khudyakov.expertise_helper.features.applications.entities.Application;

import java.time.LocalDate;
import java.util.List;

import static java.nio.file.Paths.get;
import static java.util.UUID.randomUUID;

@Mapper(componentModel = "spring")
public abstract class ApplicationMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "project", ignore = true)
    @Mapping(target = "location", ignore = true)
    @Mapping(target = "invitations", ignore = true)
    abstract public Application toApplication(NewApplicationDto newApplicationDto);

    @AfterMapping
    protected void mapLocation(@MappingTarget Application application) {
        application.setLocation(get("applications",
                LocalDate.now().toString(),
                randomUUID().toString() + ".docx").toString());
    }

    abstract public ApplicationDto toApplicationDto(Application application);

    abstract public List<ApplicationDto> toApplicationDto(List<Application> application);

}
