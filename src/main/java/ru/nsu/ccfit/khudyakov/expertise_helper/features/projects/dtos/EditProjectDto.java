package ru.nsu.ccfit.khudyakov.expertise_helper.features.projects.dtos;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;
import ru.nsu.ccfit.khudyakov.expertise_helper.files.validation.FileExtension;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
public class EditProjectDto {

    @NotNull(message = "{validation.field.error.default}")
    private UUID id;

    @NotNull(message = "{validation.field.error.default}")
    private Double baseRate;

    @NotNull(message = "{validation.field.error.default}")
    private Integer requiredNumberExperts;

    @FileExtension(value = "docx", message = "{project.add.error.act.extension}")
    private MultipartFile act;

    @FileExtension(value = "docx", message = "{project.add.error.contract.extension}")
    private MultipartFile contract;

    @FileExtension(value = "docx", message = "{project.add.error.conclusion.extension}")
    private MultipartFile conclusion;

}
