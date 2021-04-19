package ru.nsu.ccfit.khudyakov.expertise_helper.features.invitation.statemachine;

public enum InvitationEvent {
    CONCLUSION_GENERATE,
    CONCLUSION_GENERATE_FAILURE,
    CONCLUSION_GENERATE_RETRY,
    CONCLUSION_GENERATE_SUCCESS,
    SEND_INVITATION_EMAIL,
    SEND_INVITATION_EMAIL_FAILURE,
    SEND_INVITATION_EMAIL_RETRY,
    SEND_INVITATION_EMAIL_SUCCESS,
    SEND_INVITATION_EMAIL_REPEAT,
    SEND_INVITATION_EMAIL_SKIP,
    SEND_INVITATION_EMAIL_CONTINUE,
    EXPERT_IGNORE,
    EXPERT_REJECT,
    EXPERT_RESULT_UPLOADING,
    EXPERT_RESULT_UPLOADED
}
