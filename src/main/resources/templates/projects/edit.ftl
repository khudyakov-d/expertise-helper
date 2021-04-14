<#import '../parts/body.ftl' as body>
<#import '../parts/navbar.ftl' as nav>
<#import '../experts/commons.ftl' as exper_commons>

<@body.page "Редактировать проект">
    <@nav.navbar/>

    <div class="container d-flex">
        <div class="row justify-content-center align-self-center w-100">
            <div class="col-sm-8 col-lg-6">
                <form action="/projects/edit" method="post" class="form-group"
                      enctype="multipart/form-data">
                    <h3 class="text-center">Редактировать проект</h3>

                    <input type="hidden" name="_method" value="PUT"/>

                    <label><input hidden name="id" value="${project.id}"/></label>

                    <div class="form-group row">
                        <label for="title" class="col-4 col-form-label">Название проекта</label>
                        <div class="col-8">
                            <div class="form-control">${project.title}</div>
                        </div>
                    </div>

                    <div class="form-group row">
                        <label for="baseRate" class="col-4 col-form-label">Базовая ставка</label>
                        <div class="col-8">
                            <input id="baseRate"
                                   type="text"
                                   name="baseRate"
                                   value="${project.baseRate}"
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
                                   type="text"
                                   name="requiredNumberExperts"
                                   value="${project.requiredNumberExperts}"
                                   class="form-control ${(requiredNumberExpertsError??)?string('is-invalid', '')}"/>
                            <div class="invalid-feedback">${requiredNumberExpertsError!}</div>
                        </div>
                    </div>

                    <div class="form-group row">
                        <label for="projectType" class="col-4 col-form-label">Тип проекта</label>
                        <div class="col-8">
                            <div class="form-control">${project.projectType.title}</div>
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
                        Сохранить
                    </button>
                </form>
            </div>
        </div>
    </div>

</@body.page>