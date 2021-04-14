<#macro navbar>
    <#include 'context.ftl'>

    <nav class="navbar navbar-expand-lg navbar-dark bg-dark mb-1">
        <div class="container-fluid">

            <div class="collapse navbar-collapse" id="navbarText">
                <ul class="navbar-nav me-auto mb-2 mb-lg-0">
                    <li class="nav-item">
                        <a class="nav-link" href="/projects">Проекты</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="/experts">Эксперты</a>
                    </li>

                </ul>
            </div>

            <div class="text-white">
                ${user.name}
            </div>

            <div class="mx-2">
                <form action="/logout" method="post">
                    <button type="submit" class="btn btn-dark">Выйти</button>
                    <input type="hidden" name="_csrf" value="${_csrf.token}"/>
                </form>
            </div>
        </div>
    </nav>
</#macro>