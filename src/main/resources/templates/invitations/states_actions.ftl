<#macro conclsuion_new invitation>
    <#switch invitation.application.project.projectType>
        <#case "SUBJECT">
            <#assign conclusionUrl  = "subject"/>
            <#break>
    </#switch>
    <form class="form-inline my-2">
        <input type="submit"
               formmethod="get"
               formaction="/projects/applications/invitations/${invitation.id}/${conclusionUrl}"
               class="btn btn-dark mr-2"
               value="Заполнить заключение">

        <input type="submit"
               formmethod="post"
               formaction="/projects/applications/invitations/${invitation.id}/events/generate-conclusion"
               class="btn btn-dark ml-2"
               value="Продолжить">

        <input type="hidden" name="_csrf" value="${_csrf.token}"/>
    </form>
</#macro>

<#macro conclusion_generating_error invitation>
    <form class="form-inline my-2">
        <input type="submit"
               formmethod="post"
               formaction="/projects/applications/invitations/${invitation.id}/events/generate-conclusion-retry"
               class="btn btn-dark"
               value="Повторить">
        <input type="hidden" name="_csrf" value="${_csrf.token}"/>
    </form>
</#macro>

<#macro conclusion_generated invitation>
    <form class="form-inline my-2">
        <input type="submit"
               formmethod="get"
               formaction="/projects/applications/invitations/${invitation.id}/conclusion/download"
               class="btn btn-dark mr-2"
               value="Скачать заключение">

        <input type="submit"
               formmethod="post"
               formaction="/projects/applications/invitations/${invitation.id}/events/email-send"
               class="btn btn-dark mx-2"
               value="Отправить приглашение">

        <input type="submit"
               formmethod="post"
               formaction="/projects/applications/invitations/${invitation.id}/events/email-skip"
               class="btn btn-dark ml-2"
               value="Отправить самостоятельно">

        <input type="hidden" name="_csrf" value="${_csrf.token}"/>
    </form>
</#macro>

<#macro invitation_email_sending_error invitation>
    <form class="form-inline my-2">
        <input type="submit"
               formmethod="post"
               formaction="/projects/applications/invitations/${invitation.id}/events/email-retry"
               class="btn btn-dark"
               value="Обновить">
        <input type="hidden" name="_csrf" value="${_csrf.token}"/>
    </form>
</#macro>

<#macro invitation_email_sent invitation>
    <p>Дедлайн: ${invitation.deadlineDate}</p>
    <form class="form-inline my-2">
        <input type="submit"
               formmethod="post"
               formaction="/projects/applications/invitations/${invitation.id}/events/email-repeat"
               class="btn btn-dark mr-2"
               value="Повторить приглашение">

        <input type="submit"
               formmethod="post"
               formaction="/projects/applications/invitations/${invitation.id}/events/email-continue"
               class="btn btn-dark ml-2"
               value="Продолжить">

        <input type="hidden" name="_csrf" value="${_csrf.token}"/>
    </form>
</#macro>

<#macro invitation_decision_making invitation>
    <form class="form-inline my-2">
        <input type="submit"
               formmethod="post"
               formaction="/projects/applications/invitations/${invitation.id}/events/expert-reject"
               class="btn btn-dark mr-2"
               value="Эксперт проигнорировал">

        <input type="submit"
               formmethod="post"
               formaction="/projects/applications/invitations/${invitation.id}/events/expert-ignore"
               class="btn btn-dark mx-2"
               value="Эксперт отклонил">

        <input type="submit"
               formmethod="post"
               formaction="/projects/applications/invitations/${invitation.id}/events/expert-uploading-result"
               class="btn btn-dark ml-2"
               value="Эксперт согласился">

        <input type="hidden" name="_csrf" value="${_csrf.token}"/>
    </form>

</#macro>

<#macro expert_conclusion_uploading invitation>
    <form action="/projects/applications/invitations/${invitation.id}/events/expert-upload-result"
          method="post"
          class="form-inline my-2"
          enctype="multipart/form-data">

        <div class="form-group">
            <input type="file" class="form-control-file" name="file">
        </div>
        <input type="hidden" name="_csrf" value="${_csrf.token}"/>

        <button type="submit" class="btn btn-dark">Загрузить</button>
    </form>
</#macro>