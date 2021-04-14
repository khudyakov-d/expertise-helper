package ru.nsu.ccfit.khudyakov.expertise_helper.features.experts.entities;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static java.util.Collections.unmodifiableMap;

@Getter
@RequiredArgsConstructor
public enum ExpertDegree {
    ACADEMICIAN("академик", 1.3),
    C_MEMBER("член-корреспондент", 1.2),
    DOCTOR("доктор наук", 1.1),
    CANDIDATE("кандидат наук", 1);

    private final String title;

    private final double factor;

    private static final Map<String, ExpertDegree> ENUM_MAP;

    static {
        Map<String, ExpertDegree> map = new ConcurrentHashMap<>();
        for (ExpertDegree instance : ExpertDegree.values()) {
            map.put(instance.getTitle(), instance);
        }
        ENUM_MAP = unmodifiableMap(map);
    }

    public static ExpertDegree get(String name) {
        return ENUM_MAP.get(name.toLowerCase());
    }

    public String getCut() {
        return title.substring(0, 1).toUpperCase();
    }

}