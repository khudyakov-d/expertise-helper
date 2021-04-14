package ru.nsu.ccfit.khudyakov.expertise_helper.features.experts.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import ru.nsu.ccfit.khudyakov.expertise_helper.features.experts.dtos.ExpertSheetDto;
import ru.nsu.ccfit.khudyakov.expertise_helper.features.experts.entities.Expert;
import ru.nsu.ccfit.khudyakov.expertise_helper.features.experts.entities.ExpertDegree;
import ru.nsu.ccfit.khudyakov.expertise_helper.features.experts.entities.ExpertScienceCategory;

import static ru.nsu.ccfit.khudyakov.expertise_helper.features.experts.entities.ExpertScienceCategory.NONE;
import static ru.nsu.ccfit.khudyakov.expertise_helper.features.experts.entities.ExpertScienceCategory.get;

@Mapper(componentModel = "spring")
public interface ExpertSheetMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "degree", qualifiedByName = "convertAcademicDegree")
    @Mapping(target = "scienceCategory", qualifiedByName = "convertScienceCategory")
    Expert toExpert(ExpertSheetDto expertDto);

    @Named("convertAcademicDegree")
    default ExpertDegree convertAcademicDegree(String academicDegree) {
        ExpertDegree degree = ExpertDegree.get(academicDegree);
        if (degree == null) {
            throw new IllegalArgumentException("Некорректная научная степень");
        } else {
            return degree;
        }
    }

    @Named("convertScienceCategory")
    default ExpertScienceCategory convertScienceCategory(String scienceCategory) {

        if (scienceCategory == null) {
            return NONE;
        }

        ExpertScienceCategory category = get(scienceCategory);
        if (category == null) {
            throw new IllegalArgumentException("Некорректная научная степень");
        } else {
            return category;
        }
    }

}
