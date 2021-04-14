package ru.nsu.ccfit.khudyakov.expertise_helper.features.experts.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.nsu.ccfit.khudyakov.expertise_helper.features.experts.dtos.ExpertDto;
import ru.nsu.ccfit.khudyakov.expertise_helper.features.experts.entities.Expert;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ExpertMapper {

    @Mapping(target = "user", ignore = true)
    Expert toExpert(ExpertDto expertDto);

    ExpertDto toExpertDto(Expert expert);

    List<ExpertDto> toExpertDto(List<Expert> experts);

}
