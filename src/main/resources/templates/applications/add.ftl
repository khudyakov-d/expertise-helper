<#import '../parts/body.ftl' as body>
<#import '../parts/navbar.ftl' as nav>

<@body.page "Добавить заявку">
    <@nav.navbar/>

    <div class="container d-flex">
        <div class="row justify-content-center align-self-center w-100">
            <div class="col-sm-8 col-lg-6">
                <form action="/projects/${projectId}/applications"
                      method="post"
                      class="form-group"
                      enctype="multipart/form-data">

                    <h3 class="text-center">Добавить заявку</h3>

                    <#if error??>
                        <div class="alert alert-danger" role="alert">
                            <@spring.message "${error}"/>
                        </div>
                    </#if>

                    <div class="form-group row">
                        <label for="topicNumber" class="col-4 col-form-label">Номер темы</label>
                        <div class="col-8">
                            <input id="topicNumber"
                                   type="text"
                                   name="topicNumber"
                                   class="form-control ${(topicNumberError??)?string('is-invalid', '')}"/>
                            <div class="invalid-feedback">${topicNumberError!}</div>
                        </div>
                    </div>

                    <div class="form-group row">
                        <label for="topic" class="col-4 col-form-label">Полное наименование научной темы</label>
                        <div class="col-8">
                            <input id="topic"
                                   type="text"
                                   name="topic"
                                   class="form-control ${(topicError??)?string('is-invalid', '')}"/>
                            <div class="invalid-feedback">${topicError!}</div>
                        </div>
                    </div>

                    <div class="form-group row">
                        <label for="organization" class="col-4 col-form-label">Организация исполнитель</label>
                        <div class="col-8">
                            <input id="organization"
                                   type="text"
                                   name="organization"
                                   class="form-control ${(organizationError??)?string('is-invalid', '')}"/>
                            <div class="invalid-feedback">${organizationError!}</div>
                        </div>
                    </div>

                    <div class="form-group row">
                        <label for="pagesCount" class="col-4 col-form-label">Количество страниц</label>
                        <div class="col-8">
                            <input id="pagesCount"
                                   type="number"
                                   name="pagesCount"
                                   class="form-control ${(pagesCountError??)?string('is-invalid', '')}"/>
                            <div class="invalid-feedback">${pagesCountError!}</div>
                        </div>
                    </div>

                    <div class="form-group">
                        <label>Документ заявки</label>
                        <input type="file"
                               class="form-control-file ${(applicationDocumentError??)?string('is-invalid', '')}"
                               name="applicationDocument">
                        <div class="invalid-feedback">${applicationDocumentError!}</div>
                    </div>

                    <input type="hidden" name="_csrf" value="${_csrf.token}"/>

                    <button type="submit" value="Добавить" class="btn btn-dark float-right">
                        Добавить
                    </button>

                </form>
            </div>

        </div>
    </div>

</@body.page>