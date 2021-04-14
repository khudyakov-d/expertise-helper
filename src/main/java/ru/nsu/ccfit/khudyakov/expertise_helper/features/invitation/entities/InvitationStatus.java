package ru.nsu.ccfit.khudyakov.expertise_helper.features.invitation.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum InvitationStatus {
    IN_PROCESS("В работе"),
    REJECTED("Отклонено"),
    NO_ANSWER("Нет ответа"),
    COMPLETED("Завершено");

    private final String description;
}
