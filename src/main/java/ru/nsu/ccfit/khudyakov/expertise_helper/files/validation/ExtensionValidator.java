package ru.nsu.ccfit.khudyakov.expertise_helper.files.validation;

import org.springframework.web.multipart.MultipartFile;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import static org.apache.commons.io.FilenameUtils.isExtension;

public class ExtensionValidator implements ConstraintValidator<FileExtension, MultipartFile> {

    private String extension;

    @Override
    public void initialize(FileExtension constraintAnnotation) {
        extension = constraintAnnotation.value();
    }

    @Override
    public boolean isValid(MultipartFile value, ConstraintValidatorContext context) {
        if (value.isEmpty()) {
            return true;
        }

        return isExtension(value.getOriginalFilename(), extension);
    }

}
