package ru.nsu.ccfit.khudyakov.expertise_helper.features.projects.entities;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ProjectType {
    SUBJECT("проект тематики");

    private final String title;

}