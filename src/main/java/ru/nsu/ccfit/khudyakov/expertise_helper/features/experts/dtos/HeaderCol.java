package ru.nsu.ccfit.khudyakov.expertise_helper.features.experts.dtos;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface
HeaderCol {

    String name() default "";

}
