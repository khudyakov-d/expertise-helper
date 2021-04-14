package ru.nsu.ccfit.khudyakov.expertise_helper.features.projects.dtos;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ProjectTypeDto {
    SUBJECT("проект тематики");

    private final String title;

}
