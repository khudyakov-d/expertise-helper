package ru.nsu.ccfit.khudyakov.expertise_helper.features.invitation.statemachine;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum InvitationState {
    CONCLUSION_NEW("Предзаполнение заключения"),
    CONCLUSION_GENERATING("Генерация заключения"),
    CONCLUSION_GENERATING_ERROR("Ошибка генерации заключения"),
    CONCLUSION_GENERATED("Заключение сгенерировано"),
    INVITATION_EMAIL_SENDING("Отправление приглашения"),
    INVITATION_EMAIL_SENDING_ERROR("Ошибка отправки почты"),
    INVITATION_EMAIL_SENT("Отправлено приглашение"),
    INVITATION_DECISION_MAKING("Вынесение решения"),
    EXPERT_NO_ANSWER("Нет ответа"),
    EXPERT_REJECTED("Отклонено"),
    EXPERT_CONCLUSION_UPLOADING("Загрузка заключения"),
    EXPERT_CONCLUSION_UPLOADED("Заключение сохранено");

    private final String description;
}
