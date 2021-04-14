package ru.nsu.ccfit.khudyakov.expertise_helper.files.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Constraint(validatedBy = FileNotEmptyValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface FileNotEmpty {

    String message() default "{file.upload.error.not.empty}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
