<#import '../parts/body.ftl' as body>
<#import '../parts/navbar.ftl' as nav>
<#import 'commons.ftl' as commons>

<@body.page "Импорт экспертов">
    <@nav.navbar/>

    <div class="container">
        <div class="row">

            <div class="col-xs-8 mx-2">
                <p>Таблица должна содержать заголовок со следующими полями</p>
                <@commons.list_header expert_header/>
            </div>

            <div class="col-xs-4 mx-2">
                <#if error??>
                    <div class="alert alert-danger" role="alert">
                        <@spring.message "${error}"/>
                    </div>
                </#if>

                <#if success??>
                    <div class="alert alert-success" role="alert">
                        <@spring.message "${success}"/>
                    </div>
                </#if>

                <form action="/experts/import/sheet" method="post" enctype="multipart/form-data">
                    <div class="form-group">
                        <label>Загрузите xlsx файл</label>
                        <input type="file"
                               class="form-control ${(fileError??)?string('is-invalid', '')}"
                               name="file">
                        <div class="invalid-feedback">${fileError!}</div>
                    </div>
                    <input type="hidden" name="_csrf" value="${_csrf.token}"/>
                    <button type="submit" class="btn btn-dark">Загрузить</button>
                </form>

            </div>


        </div>
    </div>

</@body.page>