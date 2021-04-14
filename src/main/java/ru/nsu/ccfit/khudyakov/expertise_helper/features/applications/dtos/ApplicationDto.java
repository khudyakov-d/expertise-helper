package ru.nsu.ccfit.khudyakov.expertise_helper.features.applications.dtos;

import lombok.Data;

import java.util.UUID;

@Data
public class ApplicationDto {

    private UUID id;

    private String topic;

    private String organization;

    private Integer pagesNumber;

}
