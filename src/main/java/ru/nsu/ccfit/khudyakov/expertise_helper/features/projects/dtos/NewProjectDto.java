package ru.nsu.ccfit.khudyakov.expertise_helper.features.projects.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;
import ru.nsu.ccfit.khudyakov.expertise_helper.files.validation.FileExtension;
import ru.nsu.ccfit.khudyakov.expertise_helper.files.validation.FileNotEmpty;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewProjectDto {

    @NotBlank(message = "{validation.field.error.default}")
    private String title;

    @NotNull(message = "{validation.field.error.default}")
    private Double baseRate;

    @NotNull(message = "{validation.field.error.default}")
    private Integer requiredNumberExperts;

    @NotNull(message = "{validation.field.error.default}")
    private ProjectTypeDto projectType;

    @FileNotEmpty
    @FileExtension(value = "docx", message = "{project.add.error.act.extension}")
    private MultipartFile act;

    @FileNotEmpty
    @FileExtension(value = "docx", message = "{project.add.error.contract.extension}")
    private MultipartFile contract;

    @FileNotEmpty
    @FileExtension(value = "docx", message = "{project.add.error.conclusion.extension}")
    private MultipartFile conclusion;

}
