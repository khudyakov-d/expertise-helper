<#import '../parts/body.ftl' as body>
<#import '../parts/navbar.ftl' as nav>
<#import '../experts/commons.ftl' as exper_commons>

<@body.page "Создать проект">
    <@nav.navbar/>

    <div class="container d-flex">
        <div class="row justify-content-center align-self-center w-100">
            <div class="col-8 m-2">
                <div class="shadow-lg">
                    <form action="/projects/add" method="post" class="border-end border-start"
                          enctype="multipart/form-data">
                        <h3 class="text-center">Создать проект</h3>

                        <#if error??>
                            <div class="alert alert-danger" role="alert">
                                <@spring.message "${error}"/>
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
                            <label for="baseRate" class="col-4 col-form-label">Базовая ставка</label>
                            <div class="col-8">
                                <input id="baseRate"
                                       type="number"
                                       name="baseRate"
                                       class="form-control ${(baseRateError??)?string('is-invalid', '')}"/>
                                <div class="invalid-feedback">${baseRateError!}</div>
                            </div>
                        </div>

                        <div class="form-group row">
                            <label for="requiredNumberExperts" class="col-4 col-form-label">
                                Количество экспертов на заявку
                            </label>
                            <div class="col-8">
                                <input id="requiredNumberExperts"
                                       type="number"
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
                            <label>Шаблон акта</label>
                            <input type="file"
                                   class="form-control ${(actError??)?string('is-invalid', '')}"
                                   name="act">
                            <div class="invalid-feedback">${actError!}</div>
                        </div>

                        <div class="form-group">
                            <label>Шаблон договора</label>
                            <input type="file"
                                   class="form-control ${(contractError??)?string('is-invalid', '')}"
                                   name="contract">
                            <div class="invalid-feedback">${contractError!}</div>
                        </div>

                        <div class="form-group">
                            <label>Шаблон экспертного заключения</label>
                            <input type="file"
                                   class="form-control ${(actError??)?string('is-invalid', '')}"
                                   name="conclusion">
                            <div class="invalid-feedback">${conclusionError!}</div>
                        </div>

                        <input type="hidden" name="_csrf" value="${_csrf.token}"/>

                        <button type="submit" value="Создать" class="btn btn-dark float-right">
                            Создать
                        </button>

                    </form>
                </div>
            </div>
        </div>
    </div>

</@body.page>