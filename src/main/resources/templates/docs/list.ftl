<#import '../parts/body.ftl' as body>
<#import '../parts/navbar.ftl' as nav>
<#import '../parts/pager.ftl' as pag>

<@body.page "Документы">
    <@nav.navbar/>

    <div class="container px-5">

        <div class="row">
            <div class="col-xs-12 mb-1">
                <a href="/projects/${projectId}/docs/total-payment" class="btn btn-dark" role="button"
                   aria-pressed="true">
                    Скачать итоговую таблицу
                </a>
            </div>
        </div>

        <div class="row">
            <div class="col-xs-12 w-100">
                <ul class="list-group w-100">

                    <#list docsService.getExperts(projectId) as expert>
                        <#if docsService.getExpertApplications(expert)?size gt 0>
                            <li class="list-group-item my-3">
                                <div class="my-2">
                                    <h5 class="card-title">${expert.name!}</h5>
                                    <a href="http://localhost:8080/projects/${projectId}/docs/${expert.id}"
                                       class="btn btn-dark my-1">
                                        Скачать документы
                                    </a>
                                </div>

                                <@expert_applications expert/>
                            </li>
                        </#if>
                    </#list>
                </ul>
            </div>
        </div>

    </div>


</@body.page>

<#macro expert_applications expert>
    <table class="table">
        <tr>
            <th>Номер</th>
            <th>Название</th>
            <th>Количество страниц</th>
            <th></th>
        </tr>

        <#list docsService.getExpertApplications(expert) as application>
            <tr>
                <td>${application.topicNumber}</td>
                <td>${application.topic}</td>
                <td>${application.pagesCount}</td>
                <td>
                    <a href="http://localhost:8080/projects/applications/${application.id}/invitations"
                       class="btn btn-dark">
                        Просмотр заявки
                    </a>
                    <#assign invitation = docsService.getInitiationByExpertAndApplication(expert, application)>
                    <a href="/projects/applications/invitations/${invitation.id}/conclusion/download"
                       class="btn btn-dark mr-2">
                        Скачать заключение
                    </a>
                </td>
            </tr>
        </#list>
    </table>
</#macro>
