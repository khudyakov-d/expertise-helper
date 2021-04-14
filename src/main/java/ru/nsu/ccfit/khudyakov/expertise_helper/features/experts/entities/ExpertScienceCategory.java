package ru.nsu.ccfit.khudyakov.expertise_helper.features.experts.entities;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static java.util.Collections.unmodifiableMap;

@Getter
@RequiredArgsConstructor
public enum ExpertScienceCategory {
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

    private final String value;

    private static final Map<String, ExpertScienceCategory> ENUM_MAP;

    static {
        Map<String, ExpertScienceCategory> map = new ConcurrentHashMap<>();
        for (ExpertScienceCategory instance : ExpertScienceCategory.values()) {
            map.put(instance.getValue(), instance);
        }
        ENUM_MAP = unmodifiableMap(map);
    }

    public static ExpertScienceCategory get(String name) {
        return ENUM_MAP.get(name.toLowerCase());
    }

}