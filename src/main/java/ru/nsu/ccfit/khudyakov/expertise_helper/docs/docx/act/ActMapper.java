package ru.nsu.ccfit.khudyakov.expertise_helper.docs.docx.act;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import ru.nsu.ccfit.khudyakov.expertise_helper.features.experts.entities.Expert;

import static ru.nsu.ccfit.khudyakov.expertise_helper.docs.utils.CostFormatter.formatContractorInitials;
import static ru.nsu.ccfit.khudyakov.expertise_helper.docs.utils.CostFormatter.formatCost;
import static ru.nsu.ccfit.khudyakov.expertise_helper.docs.utils.CostFormatter.formatCursiveCost;

@Mapper(componentModel = "spring")
public abstract class ActMapper {

    @Mapping(target = "number", source = "contractNumber")
    @Mapping(target = "contractorName", source = "expert.name")
    @Mapping(target = "contractorDegree", source = "expert.degree")
    @Mapping(target = "expertiseCost", ignore = true)
    @Mapping(target = "totalCost", ignore = true)
    @Mapping(target = "contractCost", ignore = true)
    @Mapping(target = "cursiveCost", ignore = true)
    @Mapping(target = "contractorInitials", ignore = true)
    public  abstract Act map(String contractNumber,
                             Expert expert,
                             Integer expertiseCount,
                             Double cost,
                             Double costWithTaxes);
    
    @AfterMapping
    protected void mapComputableFields(Double cost, Double costWithTaxes, @MappingTarget Act act) {
        act.setExpertiseCost(formatCost(cost));
        act.setTotalCost(formatCost(cost));
        act.setContractCost(formatCost(costWithTaxes));
        act.setCursiveCost(formatCursiveCost(costWithTaxes));
        act.setContractorInitials(formatContractorInitials(act.getContractorName()));
    }


}
