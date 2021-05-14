<#import '../parts/body.ftl' as body>
<#import '../parts/navbar.ftl' as nav>
<#import '../parts/pager.ftl' as pag>

<@body.page "Документы">
    <@nav.navbar/>

    <div class="container px-5">

        <#if experts?size gt 0>
            <div class="row">
                <div class="col-xs-12 mb-1">
                    <a href="/projects/${projectId}/docs/total-payment" class="btn btn-dark" role="button"
                       aria-pressed="true">
                        Скачать итоговую таблицу
                    </a>
                </div>
            </div>
        </#if>

        <div class="row">
            <div class="col-xs-12 w-100">
                <div class="list-group w-100">
                    <#if experts?size gt 0>
                        <#list experts as expert>
                            <#if docsService.getExpertApplications(expert)?size gt 0>
                                <div class="list-group-item my-3">
                                    <div class="my-2">
                                        <h5 class="card-title">${expert.name!}</h5>
                                        <a href="http://localhost:8080/projects/${projectId}/docs/${expert.id}"
                                           class="btn btn-dark my-1">
                                            Скачать документы
                                        </a>
                                        <@contract_data projectId expert/>
                                    </div>
                                    <@expert_applications expert/>
                                </div>
                            </#if>
                        </#list>
                    <#else>
                        <h4>Список одобренных приглашений пуст!</h4>
                    </#if>

                </div>
            </div>
        </div>

    </div>


</@body.page>

<#macro expert_applications expert>
    <div class="table-responsive">

        <table class="table">
            <tr>
                <th>Номер</th>
                <th>Название</th>
                <th>Количество страниц</th>
                <th>Заявка</th>
                <th>Заключение</th>
            </tr>

            <#list docsService.getExpertApplications(expert) as application>
                <tr>
                    <td>${application.topicNumber}</td>
                    <td>${application.topic}</td>
                    <td>${application.pagesCount}</td>
                    <td>
                        <a href="http://localhost:8080/projects/applications/${application.id}/invitations"
                           class="btn btn-dark">
                            Просмотр
                        </a>
                    </td>
                    <td>
                        <#assign invitation = invitationService.getInitiationByExpertAndApplication(expert, application)>
                        <a href="/projects/applications/invitations/${invitation.id}/conclusion/download"
                           class="btn btn-dark mr-2">
                            Скачать
                        </a>
                    </td>
                </tr>
            </#list>
        </table>
    </div>
</#macro>

<#macro contract_data projectId expert>
    <button type="button" class="btn btn-dark" data-toggle="modal" data-target="#contract-${expert.id}">
        Данные договора
    </button>

    <div class="modal fade" id="contract-${expert.id}" tabindex="-1" role="dialog" aria-hidden="true">
        <div class="modal-dialog" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title">${expert.name}</h5>
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                    </button>
                </div>
                <div class="modal-body">
                    <form action="/projects/${projectId}/docs/${expert.id}/contract"
                          method="post"
                          class="form my-2">

                        <div class="form-group row">
                            <label for="contractDate" class="col-6 col-form-label">Дата договора</label>
                            <div class="col-6">
                                <input id="contractDate" type="date" name="contractDate" required/>
                            </div>
                        </div>

                        <div class="form-group row">
                            <label for="contractNumber" class="col-6 col-form-label">Номер договора</label>
                            <div class="col-6">
                                <input id="contractNumber" type="text" name="contractNumber" required/>
                            </div>
                        </div>

                        <input type="hidden" name="_csrf" value="${_csrf.token}"/>

                        <button type="submit" class="btn btn-dark">Подтвердить</button>
                    </form>
                </div>
            </div>
        </div>
    </div>
</#macro>