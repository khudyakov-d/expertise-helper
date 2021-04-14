package ru.nsu.ccfit.khudyakov.expertise_helper.docs.docx.contract;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import ru.nsu.ccfit.khudyakov.expertise_helper.features.experts.entities.Expert;

import static ru.nsu.ccfit.khudyakov.expertise_helper.docs.utils.CostFormatter.formatContractorInitials;
import static ru.nsu.ccfit.khudyakov.expertise_helper.docs.utils.CostFormatter.formatCost;
import static ru.nsu.ccfit.khudyakov.expertise_helper.docs.utils.CostFormatter.formatCursiveCost;

@Mapper(componentModel = "spring")
public abstract class ContractMapper {

    @Mapping(target = "number", source = "contractNumber")
    @Mapping(target = "contractorName", source = "expert.name")
    @Mapping(target = "contractorDegree", source = "expert.degree")
    @Mapping(target = "cost", ignore = true)
    @Mapping(target = "contractorCost", ignore = true)
    @Mapping(target = "contractorInitials", ignore = true)
    public abstract Contract map(String contractNumber,
                                 Expert expert,
                                 Integer count,
                                 Double cost,
                                 Double costWithTaxes);

    @AfterMapping
    protected void mapComputableFields(Double cost, Double costWithTaxes, @MappingTarget Contract contract) {
        contract.setContractorInitials(formatContractorInitials(contract.getContractorName()));

        contract.setCost(String.format("(%s руб.) %s", formatCost(cost), formatCursiveCost(cost)));

        contract.setContractorCost(String.format("(%s руб.) %s",
                formatCost(costWithTaxes),
                formatCursiveCost(costWithTaxes))
        );
    }

}
