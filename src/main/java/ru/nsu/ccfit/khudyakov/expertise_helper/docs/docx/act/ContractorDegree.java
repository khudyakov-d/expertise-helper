package ru.nsu.ccfit.khudyakov.expertise_helper.docs.docx.act;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ContractorDegree {
    ACADEMICIAN("академик"),
    C_MEMBER("член-корреспондент"),
    DOCTOR("доктор наук"),
    CANDIDATE("кандидат наук");

    private final String degree;

    @Override
    public String toString() {
        return degree;
    }
}
