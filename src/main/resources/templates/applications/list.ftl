<#import '../parts/body.ftl' as body>
<#import '../parts/navbar.ftl' as nav>

<@body.page "Заявки">
    <@nav.navbar/>

    <div class="container">

        <div class="row">
            <div class="col-xs-12 mb-1">
                <a href="/projects/${projectId}/applications/add" class="btn btn-dark" role="button"
                   aria-pressed="true">
                    Добавить заявку
                </a>
            </div>
        </div>

        <div class="row">
            <div class="col-xs-12 w-100">
                <ul class="list-group w-100">
                    <#if applications?size gt 0>
                        <#list applications as application>
                            <li class="list-group-item my-3">
                                <h5 class="card-title">${application.topic}</h5>

                                <#assign count = applicationService.getCompletedInvitationCount(user, application.id)/>
                                <p class="card-text">Экспертов найдено: ${count}/${requiredNumberExperts}</p>

                                <a href="/projects/applications/${application.id}/invitations"
                                   class="btn btn-dark">
                                    Просмотр
                                </a>
                            </li>
                        </#list>
                    <#else>
                        <h4>Список заявок пуст</h4>
                    </#if>
                </ul>
            </div>

        </div>
    </div>

</@body.page>