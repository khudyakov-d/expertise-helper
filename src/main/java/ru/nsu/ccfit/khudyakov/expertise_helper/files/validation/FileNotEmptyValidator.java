package ru.nsu.ccfit.khudyakov.expertise_helper.files.validation;

import org.springframework.web.multipart.MultipartFile;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class FileNotEmptyValidator implements ConstraintValidator<FileNotEmpty, MultipartFile> {

    @Override
    public boolean isValid(MultipartFile value, ConstraintValidatorContext context) {
        return !value.isEmpty();
    }

}
