<#import '../parts/body.ftl' as body>
<#import '../parts/navbar.ftl' as nav>
<#import 'states_actions.ftl' as actions>


<@body.page "Новое приглашение">
    <@nav.navbar/>

    <div class="container-fluid">
        <div class="row">
            <div class="col-8 w-100">

                <#if error??>
                    <div class="alert alert-danger" role="alert">
                        <@spring.message "${error}"/>
                    </div>
                </#if>

                <table class="table table-bordered">

                    <#if experts?size gt 0>
                        <thead>
                        <tr>
                            <#list expert_header as name>
                                <th><@spring.message "${name}"/></th>
                            </#list>
                        </tr>
                        </thead>
                    </#if>

                    <tbody>
                    <#list experts as expert>
                        <tr>
                            <td>${expert.name!}</td>
                            <td>${expert.organization!}</td>
                            <td>${expert.post!}</td>
                            <td>${expert.degree.title!}</td>
                            <td>${expert.scienceCategory.value!}</td>
                            <td>${expert.email!}</td>
                            <td>${expert.workPhone!}</td>
                            <td>${expert.personalPhone!}</td>
                            <td>${expert.birthDate!}</td>
                            <td><@add_invitation applicationId expert/></td>
                        </tr>
                    </#list>
                    </tbody>
                </table>
            </div>
        </div>
    </div>

</@body.page>

<#macro add_invitation applicationId expert>
    <button type="button" class="btn btn-dark" data-toggle="modal" data-target="#add-${expert.id}">
        Выбрать
    </button>

    <div class="modal fade" id="add-${expert.id}" tabindex="-1" role="dialog" aria-hidden="true">
        <div class="modal-dialog" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="addModelLabel">${expert.name}</h5>
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                    </button>
                </div>
                <div class="modal-body">
                    <form action="/projects/applications/${applicationId}/invitations/${expert.id}"
                          method="post"
                          class="form">

                        <label>
                            Дата дедлайна
                            <input type="date" name="deadlineDate" required/>
                        </label>

                        <input type="hidden" name="_csrf" value="${_csrf.token}"/>

                        <button type="submit" value="Выбрать" class="btn btn-dark float-right">
                            Выбрать
                        </button>
                    </form>

                </div>
            </div>
        </div>
    </div>
</#macro>