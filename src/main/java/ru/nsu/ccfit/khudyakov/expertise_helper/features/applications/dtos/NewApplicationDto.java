package ru.nsu.ccfit.khudyakov.expertise_helper.features.applications.dtos;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;
import ru.nsu.ccfit.khudyakov.expertise_helper.files.validation.FileExtension;
import ru.nsu.ccfit.khudyakov.expertise_helper.files.validation.FileNotEmpty;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class NewApplicationDto {

    @NotBlank(message = "{validation.field.error.default}")
    private String topicNumber;

    @NotBlank(message = "{validation.field.error.default}")
    private String topic;

    @NotBlank(message = "{validation.field.error.default}")
    private String organization;

    @NotNull(message = "{validation.field.error.default}")
    private Integer pagesCount;

    @NotNull(message = "{validation.field.error.default}")
    @FileExtension(value = "docx", message = "{application.add.error.file.extension}")
    @FileNotEmpty
    private MultipartFile applicationDocument;

}
