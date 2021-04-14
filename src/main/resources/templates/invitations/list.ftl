<#import '../parts/body.ftl' as body>
<#import '../parts/navbar.ftl' as nav>
<#import 'states_actions.ftl' as actions>


<@body.page "Приглашения">
    <@nav.navbar/>

    <div class="container">

        <div class="row">
            <div class="col-8 w-100">

                <div>
                    <a href="/projects/applications/${applicationId}/invitations/add" class="btn btn-dark"
                       role="button">
                        Новое приглашение
                    </a>
                </div>

                <ul class="list-group w-100">
                    <#if invitationsInProcess?size gt 0>
                        <#list invitationsInProcess as invitation, state>
                            <div class="card my-3">
                                <div class="card-body">
                                    <h5 class="card-title text-muted">${invitation.expert.name}</h5>
                                    <h5 class="card-title mb-4">${state.description}</h5>
                                    <div>
                                        <#switch state>
                                            <#case "CONCLUSION_NEW">
                                                <@actions.conclsuion_new invitation/>
                                                <#break>
                                            <#case "CONCLUSION_GENERATING_ERROR">
                                                <@actions.conclusion_generating_error invitation/>
                                                <#break>
                                            <#case "CONCLUSION_GENERATED">
                                                <@actions.conclusion_generated invitation/>
                                                <#break>
                                            <#case "INVITATION_EMAIL_SENDING_ERROR">
                                                <@actions.invitation_email_sending_error invitation/>
                                                <#break>
                                            <#case "INVITATION_EMAIL_SENT">
                                                <@actions.invitation_email_sent invitation/>
                                                <#break>
                                            <#case "INVITATION_DECISION_MAKING">
                                                <@actions.invitation_decision_making invitation/>
                                                <#break>
                                            <#case "EXPERT_CONCLUSION_UPLOADING">
                                                <@actions.expert_conclusion_uploading invitation/>
                                                <#break>
                                        </#switch>
                                    </div>
                                </div>
                            </div>
                        </#list>
                    <#else>
                        <h4>Список приглашений пуст</h4>
                    </#if>
                </ul>
            </div>

            <div class="col-4 w-100">

                <#if invitations?size gt 0>
                    <#list statuses as status>
                        <ul class="list-group">
                            <#assign inv_in_status =  invitations?filter(x -> x.status = status)/>
                            <#if inv_in_status?size gt 0>
                                <h4>${status.description}</h4>
                                <#list inv_in_status as invitation>
                                    <li class="list-group-item">
                                        ${invitation.expert.name}
                                    </li>
                                </#list>
                            </#if>
                        </ul>
                    </#list>
                </#if>

            </div>
        </div>
    </div>

</@body.page>