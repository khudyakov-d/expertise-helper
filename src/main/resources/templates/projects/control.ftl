<#import '../parts/body.ftl' as body>
<#import '../parts/navbar.ftl' as nav>
<#import '../experts/commons.ftl' as exper_commons>

<@body.page "Редактировать проект">
    <@nav.navbar/>

    <div class="container d-flex">
        <div class="row justify-content-center align-self-center w-100">
            <div class="col-sm-8 col-lg-6">
                <form action="/projects/edit" method="post" class="form-group" enctype="multipart/form-data">
                    <h3 class="text-center">Редактировать проект</h3>

                    <#if error??>
                        <div class="alert alert-danger" role="alert">
                            ${error}
                        </div>
                    </#if>

                    <div class="form-group row">
                        <label for="title" class="col-4 col-form-label">Название проекта</label>
                        <div class="col-8">
                            <input id="title"
                                   type="text"
                                   name="title"
                                   class="form-control ${(titleError??)?string('is-invalid', '')}"/>
                            <div class="invalid-feedback">${titleError!}</div>
                        </div>
                    </div>

                    <div class="form-group row">
                        <label for="basicRate" class="col-4 col-form-label">Базовая ставка</label>
                        <div class="col-8">
                            <input id="basicRate"
                                   type="text"
                                   name="basicRate"
                                   class="form-control ${(basicRateError??)?string('is-invalid', '')}"/>
                            <div class="invalid-feedback">${basicRateError!}</div>
                        </div>
                    </div>

                    <div class="form-group row">
                        <label for="requiredNumberExperts" class="col-4 col-form-label">
                            Необходимое количество экспертов на заявку
                        </label>
                        <div class="col-8">
                            <input id="requiredNumberExperts"
                                   type="text"
                                   name="requiredNumberExperts"
                                   class="form-control ${(requiredNumberExpertsError??)?string('is-invalid', '')}"/>
                            <div class="invalid-feedback">${requiredNumberExpertsError!}</div>
                        </div>
                    </div>

                    <div class="form-group row">
                        <label for="projectType" class="col-4 col-form-label">Тип проекта</label>
                        <div class="col-8">
                            <select id="projectType"
                                    name="projectType"
                                    class="form-control ${(projectTypeError??)?string('is-invalid', '')}">
                                <option disabled selected></option>
                                <#list projectTypes as projectType>
                                    <option value="${projectType}">${projectType.title}</option>
                                </#list>
                            </select>
                            <div class="invalid-feedback">${projectTypeError!}</div>
                        </div>
                    </div>

                    <div class="form-group">
                        <label>Шаблон экспертного заключения</label>
                        <input type="file"
                               class="form-control ${(reviewTemplateError??)?string('is-invalid', '')}"
                               name="reviewTemplate">
                        <div class="invalid-feedback">${reviewTemplateError!}</div>

                    </div>

                    <div class="form-group">
                        <label>Шаблон акта заключения</label>
                        <input type="file"
                               class="form-control ${(actTemplateError??)?string('is-invalid', '')}"
                               name="actTemplate">
                        <div class="invalid-feedback">${actTemplateError!}</div>
                    </div>

                    <div class="form-group">
                        <label>Шаблон договора</label>
                        <input type="file"
                               class="form-control  ${(contractTemplateError??)?string('is-invalid', '')}"
                               name="contractTemplate">
                        <div class="invalid-feedback">${contractTemplateError!}</div>
                    </div>

                    <input type="hidden" name="_csrf" value="${_csrf.token}"/>

                    <button type="submit" value="Создать" class="btn btn-dark float-right">
                        Создать
                    </button>

                </form>
            </div>
        </div>
    </div>

</@body.page>