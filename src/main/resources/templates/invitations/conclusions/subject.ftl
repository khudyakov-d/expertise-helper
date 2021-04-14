<#import '../../parts/navbar.ftl' as nav>
<#import '../../parts/body.ftl' as body>

<@body.page "Заключение тематики">
    <@nav.navbar/>

    <div class="container d-flex">
        <div class="row justify-content-center align-self-center w-100">
            <div class="col-sm-8 col-lg-6">
                <form action="/projects/applications/invitations/${invitationId}/subject"
                      method="post"
                      class="form-group">

                    <h3 class="text-center">Добавить заявку</h3>

                    <#if error??>
                        <div class="alert alert-danger" role="alert">
                            ${error}
                        </div>
                    </#if>

                    <div class="form-group row">
                        <label for="organizationName" class="col-4 col-form-label">Наименование организации</label>
                        <div class="col-8">
                            <input id="organizationName" type="text" name="organizationName" class="form-control"/>
                        </div>
                    </div>

                    <div class="form-group row">
                        <label for="founderName" class="col-4 col-form-label">Наименование учредителя</label>
                        <div class="col-8">
                            <input id="founderName" type="text" name="founderName" class="form-control"/>
                        </div>
                    </div>

                    <div class="form-group row">
                        <label for="scientificTopic" class="col-4 col-form-label">Наименование научной темы</label>
                        <div class="col-8">
                            <input id="scientificTopic" type="text" name="scientificTopic" class="form-control"/>
                        </div>
                    </div>

                    <div class="form-group row">
                        <label for="scientificTopicCode" class="col-4 col-form-label">Код (шифр) научной темы</label>
                        <div class="col-8">
                            <input id="scientificTopicCode" type="text" name="scientificTopicCode"
                                   class="form-control"/>
                        </div>
                    </div>

                    <div class="form-group row">
                        <label for="registrationNumber" class="col-4 col-form-label">
                            Номер государственного учета научно-исследовательской
                        </label>
                        <div class="col-8">
                            <input id="registrationNumber" type="text" name="registrationNumber" class="form-control"/>
                        </div>
                    </div>

                    <input type="hidden" name="_csrf" value="${_csrf.token}"/>

                    <button type="submit" value="Добавить" class="btn btn-dark float-right">
                        Прикрепить
                    </button>

                </form>
            </div>

        </div>
    </div>

</@body.page>