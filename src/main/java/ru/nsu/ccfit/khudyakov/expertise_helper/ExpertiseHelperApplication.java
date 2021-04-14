package ru.nsu.ccfit.khudyakov.expertise_helper;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

@EntityScan(
        value = "org.springframework.statemachine.data.jpa",
        basePackageClasses = {ExpertiseHelperApplication.class, Jsr310JpaConverters.class}
)
@SpringBootApplication
public class ExpertiseHelperApplication {

    public static void main(String[] args) {
        SpringApplication.run(ExpertiseHelperApplication.class, args);
    }

}
