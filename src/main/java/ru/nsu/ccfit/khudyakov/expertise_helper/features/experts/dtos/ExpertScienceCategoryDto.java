package ru.nsu.ccfit.khudyakov.expertise_helper.features.experts.dtos;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static java.util.Collections.unmodifiableMap;

@Getter
@RequiredArgsConstructor
public enum ExpertScienceCategoryDto {
    NONE("без категории"),
    MATHEMATIC_INFORMATICS("математика и информатика"),
    PHYSICAL("физические науки"),
    NANO_IT("нано и информационные технологии"),
    CHEMICAL("химические науки"),
    BIOLOGICAL("биологические науки"),
    EARTH("науки о Земле"),
    ECONOMIC("экономические науки"),
    HUMANITARIAN("гуманитарные науки"),
    MEDICAL("медицинские науки"),
    AGRICULTURAL("сельскохозяйственные науки");

    private final String category;

    private static final Map<String, ExpertScienceCategoryDto> ENUM_MAP;

    static {
        Map<String, ExpertScienceCategoryDto> map = new ConcurrentHashMap<>();
        for (ExpertScienceCategoryDto instance : ExpertScienceCategoryDto.values()) {
            map.put(instance.getCategory(), instance);
        }
        ENUM_MAP = unmodifiableMap(map);
    }

    public static ExpertScienceCategoryDto get(String name) {
        return ENUM_MAP.get(name.toLowerCase());
    }

}