package ru.nsu.ccfit.khudyakov.expertise_helper.docs.xlsx.total_payment;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Qualifier;
import ru.nsu.ccfit.khudyakov.expertise_helper.features.applications.entities.Application;
import ru.nsu.ccfit.khudyakov.expertise_helper.features.docs.entities.ExpertContract;
import ru.nsu.ccfit.khudyakov.expertise_helper.features.experts.entities.Expert;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static ru.nsu.ccfit.khudyakov.expertise_helper.docs.xlsx.total_payment.PageFactor.getFromCount;
import static ru.nsu.ccfit.khudyakov.expertise_helper.docs.xlsx.total_payment.PageFactor.values;

@Mapper(componentModel = "spring")
public abstract class ExpertProjectPaymentMapper {

    @Mapping(source = "expert.name", target = "name")
    @Mapping(source = "expert.degree", target = "academicDegree")
    @Mapping(source = "expert.scienceCategory", target = "scienceCategory")
    @Mapping(source = "contract.contractNumber", target = "contractNumber")
    @Mapping(source = "contract.contractDate", target = "contractDate")
    @Mapping(source = "applications", target = "reportByGroups", qualifiedBy = ReportsMapping.class)
    public abstract ExpertProjectPayment map(Expert expert, List<Application> applications, ExpertContract contract);

    @Qualifier
    @Target(ElementType.METHOD)
    @interface ReportsMapping {
    }

    @ReportsMapping
    protected Map<PageFactor, Integer> mapReports(List<Application> applications) {
        Map<PageFactor, Integer> reportByGroups = new HashMap<>();

        PageFactor[] pageFactors = values();
        for (PageFactor pageFactor : pageFactors) {
            reportByGroups.put(pageFactor, 0);
        }

        applications.forEach(a -> reportByGroups.computeIfPresent(getFromCount(a.getPagesCount()), (k, v) -> v + 1));

        return reportByGroups;
    }

}
