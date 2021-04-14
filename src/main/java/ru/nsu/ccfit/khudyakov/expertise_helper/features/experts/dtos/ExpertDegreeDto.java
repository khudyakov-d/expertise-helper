package ru.nsu.ccfit.khudyakov.expertise_helper.features.experts.dtos;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static java.util.Collections.unmodifiableMap;

@Getter
@RequiredArgsConstructor
public enum ExpertDegreeDto {
    ACADEMICIAN("академик"),
    C_MEMBER("член-корреспондент"),
    DOCTOR("доктор наук"),
    CANDIDATE("кандидат наук");

    private final String title;

    private static final Map<String, ExpertDegreeDto> ENUM_MAP;

    static {
        Map<String, ExpertDegreeDto> map = new ConcurrentHashMap<>();
        for (ExpertDegreeDto instance : ExpertDegreeDto.values()) {
            map.put(instance.getTitle(), instance);
        }
        ENUM_MAP = unmodifiableMap(map);
    }

    public static ExpertDegreeDto get(String name) {
        return ENUM_MAP.get(name.toLowerCase());
    }

}