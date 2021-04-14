<#import '../parts/body.ftl' as body>
<#import '../parts/navbar.ftl' as nav>
<#import 'commons.ftl' as commons>

<@body.page "Импорт экспертов">
    <@nav.navbar/>

    <div class="container d-flex">
        <div class="row justify-content-center align-self-center w-100">
            <div class="col-sm-8 col-lg-6">
                <form action="/experts/edit" method="post" class="form-group">

                    <h3 class="text-center">Изменение эксперта</h3>

                    <input type="hidden" name="_method" value="PUT"/>

                    <label><input hidden name="id" value="${expert.id}"/></label>

                    <div class="form-group row">
                        <label for="name" class="col-4 col-form-label">Имя</label>
                        <div class="col-8">
                            <input id="name"
                                   type="text"
                                   name="name"
                                   value="${expert.name!}"
                                   class="form-control ${(nameError??)?string('is-invalid', '')}"/>
                            <div class="invalid-feedback">${nameError!}</div>
                        </div>
                    </div>

                    <div class="form-group row">
                        <label for="organization" class="col-4 col-form-label">Организация</label>
                        <div class="col-8">
                            <input id="organization"
                                   type="text"
                                   name="organization"
                                   value="${expert.organization!}"
                                   class="form-control ${(organizationError??)?string('is-invalid', '')}"/>
                            <div class="invalid-feedback">${organizationError!}</div>
                        </div>
                    </div>

                    <div class="form-group row">
                        <label for="post" class="col-4 col-form-label">Должность</label>
                        <div class="col-8">
                            <input id="post"
                                   type="text"
                                   name="post"
                                   value="${expert.post!}"
                                   class="form-control ${(postError??)?string('is-invalid', '')}"/>
                            <div class="invalid-feedback">${postError!}</div>
                        </div>
                    </div>

                    <div class="form-group row">
                        <label for="degree" class="col-4 col-form-label">Ученая степень</label>
                        <div class="col-8">

                            <select id="degree"
                                    name="degree"
                                    class="form-control ${(degreeError??)?string('is-invalid', '')}">

                                <option hidden value="${expert.degree!}">
                                    ${expert.degree.title!}
                                </option>
                                <#list degrees as degree>
                                    <option value="${degree}">${degree.title}</option>
                                </#list>

                            </select>
                            <div class="invalid-feedback">${degreeeError!}</div>
                        </div>
                    </div>

                    <div class="form-group row">
                        <label for="scienceCategory" class="col-4 col-form-label">Категория ОУС</label>
                        <div class="col-8">
                            <select id="scienceCategory"
                                    name="scienceCategory"
                                    class="form-control ${(scienceCategoryError??)?string('is-invalid', '')}">
                                <option hidden value="${expert.scienceCategory}">${expert.scienceCategory.category!}
                                </option>
                                <#list categories as category>
                                    <option value="${category}">${category.category}</option>
                                </#list>
                            </select>
                            <div class="invalid-feedback">${scienceCategoryError!}</div>
                        </div>
                    </div>

                    <div class="form-group row">
                        <label for="email" class="col-4 col-form-label">Электронная почта</label>
                        <div class="col-8">
                            <input id="email"
                                   type="email"
                                   name="email"
                                   value="${expert.email!}"
                                   class="form-control ${(emailError??)?string('is-invalid', '')}"/>
                            <div class="invalid-feedback">${emailError!}</div>
                        </div>
                    </div>

                    <div class="form-group row">
                        <label for="workPhone" class="col-4 col-form-label">Рабочий телефон</label>
                        <div class="col-8">
                            <input id="workPhone"
                                   type="text"
                                   name="workPhone"
                                   value="${expert.workPhone!}"
                                   class="form-control ${(workPhoneError??)?string('is-invalid', '')}"/>
                            <div class="invalid-feedback">${workPhoneError!}</div>

                        </div>
                    </div>

                    <div class="form-group row">
                        <label for="personalPhone" class="col-4 col-form-label">Личный телефон</label>
                        <div class="col-8">
                            <input id="personalPhone"
                                   type="text"
                                   name="personalPhone"
                                   value="${expert.personalPhone!}"
                                   class="form-control ${(personalPhoneError??)?string('is-invalid', '')}"/>
                            <div class="invalid-feedback">${personalPhoneError!}</div>
                        </div>
                    </div>

                    <div class="form-group row">
                        <label for="birthDate" class="col-4 col-form-label">Дата рождения</label>
                        <div class="col-8">
                            <input id="birthDate"
                                   type="date"
                                   name="birthDate"
                                   value="${expert.birthDate!}"
                                   class="form-control ${(birthDateError??)?string('is-invalid', '')}"/>

                            <div class="invalid-feedback">${birthDateError!}</div>
                        </div>
                    </div>

                    <input type="hidden" name="_csrf" value="${_csrf.token}"/>

                    <button type="submit" value="Сохранить" class="btn btn-dark float-right">
                        Изменить
                    </button>

                </form>
            </div>
        </div>
    </div>

</@body.page>