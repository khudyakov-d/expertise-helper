package ru.nsu.ccfit.khudyakov.expertise_helper.features.experts.dtos;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;
import ru.nsu.ccfit.khudyakov.expertise_helper.files.validation.FileExtension;
import ru.nsu.ccfit.khudyakov.expertise_helper.files.validation.FileNotEmpty;

@Data
public class SheetFileBucket {

    @FileNotEmpty(message = "{file.upload.error.not.null}")
    @FileExtension(value = "xlsx")
    private MultipartFile file;

}
