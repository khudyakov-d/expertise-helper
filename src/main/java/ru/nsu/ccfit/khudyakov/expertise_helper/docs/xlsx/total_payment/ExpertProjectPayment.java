package ru.nsu.ccfit.khudyakov.expertise_helper.docs.xlsx.total_payment;

import lombok.Data;
import org.springframework.data.domain.Page;
import ru.nsu.ccfit.khudyakov.expertise_helper.features.experts.entities.ExpertDegree;
import ru.nsu.ccfit.khudyakov.expertise_helper.features.experts.entities.ExpertScienceCategory;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Data
public class ExpertProjectPayment {

    private String name;

    private ExpertScienceCategory scienceCategory;

    private ExpertDegree academicDegree;

    private String contractNumber;

    private LocalDate contractDate;

    private Map<PageFactor, Integer> reportByGroups;

    public List<Object> getFieldsAsList() {
        List<Object> fields = new ArrayList<>(Arrays.asList(this.name,
                scienceCategory.getValue(),
                contractNumber,
                contractDate,
                academicDegree.getFactor(),
                academicDegree.getCut()));

        for (PageFactor pageFactor : PageFactor.values()) {
            fields.add(reportByGroups.get(pageFactor));
        }


        return fields;
    }

}
