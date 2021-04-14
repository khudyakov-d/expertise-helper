<#import '../parts/body.ftl' as body>
<#import '../parts/navbar.ftl' as nav>
<#import '../experts/commons.ftl' as experts>

<@body.page "Проекты">
    <@nav.navbar/>

    <div class="container">

        <div class="row">
            <div class="col-xs-12 mb-1">
                <a href="/projects/add" class="btn btn-dark" role="button" aria-pressed="true">
                    Создать проект
                </a>
            </div>
        </div>

        <div class="row">
            <div class="col-xs-12 w-100">
                <ul class="list-group w-100">
                    <#if projects?size gt 0>
                        <#list projects as project>
                            <li class="list-group-item my-3">
                                <h5 class="card-title">${project.title}</h5>
                                <p class="card-text"> Дата создания: ${project.creationDate}</p>
                                <a href="/projects/${project.id}/applications" class="btn btn-dark">Управление заявками</a>
                                <a href="/projects/${project.id}/docs" class="btn btn-dark">Работа с документами</a>
                                <a href="/projects/${project.id}/edit" class="btn btn-dark">Редактирование параметров</a>
                            </li>
                        </#list>
                    <#else>
                        <h4>Список проектов пуст</h4>
                    </#if>
                </ul>
            </div>
        </div>
    </div>

</@body.page>