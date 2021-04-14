package ru.nsu.ccfit.khudyakov.expertise_helper.features.projects.dtos;

import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
public class ProjectInfoDto {

    private UUID id;

    private String title;

    private LocalDate creationDate;

    private ProjectTypeDto projectType;

}
