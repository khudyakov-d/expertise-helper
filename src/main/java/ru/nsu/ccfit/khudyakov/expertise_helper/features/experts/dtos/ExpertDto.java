package ru.nsu.ccfit.khudyakov.expertise_helper.features.experts.dtos;


import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
public class ExpertDto {

    private UUID id;

    @HeaderCol(name = "expert.text.name")
    @NotBlank(message = "{validation.field.error.default}")
    private String name;

    @HeaderCol(name = "expert.text.organization")
    @NotBlank(message = "{validation.field.error.default}")
    private String organization;

    @HeaderCol(name = "expert.text.post")
    private String post;

    @HeaderCol(name = "expert.text.degree")
    @NotNull(message = "{validation.field.error.default}")
    private ExpertDegreeDto degree;

    @HeaderCol(name = "expert.text.scienceCategory")
    @NotNull(message = "{validation.field.error.default}")
    private ExpertScienceCategoryDto scienceCategory;

    @HeaderCol(name = "expert.text.email")
    @Email
    @NotBlank(message = "{validation.field.error.default}")
    private String email;

    @HeaderCol(name = "expert.text.workPhone")
    private String workPhone;

    @HeaderCol(name = "expert.text.personalPhone")
    private String personalPhone;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @HeaderCol(name = "expert.text.birthDate")
    private LocalDate birthDate;

    public static List<String> getExpertFieldsNames() {
        List<String> colNames = new ArrayList<>();
        Field[] fields = ExpertDto.class.getDeclaredFields();

        for (Field field : fields) {
            HeaderCol headerCol = field.getAnnotation(HeaderCol.class);

            if (headerCol == null) {
                continue;
            }

            colNames.add(headerCol.name());
        }

        return colNames;
    }

}
