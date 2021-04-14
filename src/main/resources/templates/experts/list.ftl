<#import '../parts/body.ftl' as body>
<#import '../parts/navbar.ftl' as nav>
<#import '../parts/pager.ftl' as pag>
<#import 'commons.ftl' as commons>

<@body.page "Эксперты">
    <@nav.navbar/>

    <div class="container-fluid px-5">
        <div class="row">
            <div class="col-xs-12 mb-1">
                <a href="/experts/import/sheet" class="btn btn-dark" role="button" aria-pressed="true">
                    Импорт из таблицы
                </a>

                <a href="/experts/add" class="btn btn-dark" role="button" aria-pressed="true">
                    Добавить
                </a>

                <@pag.pager "/experts" page/>
            </div>
        </div>

        <div class="row">
            <div class="col-xs-12">
                <#if page.getTotalPages() gt 0>
                    <table class="table table-bordered">
                        <@commons.expert_table expert_header page.getContent()/>
                    </table>
                <#else>
                    <h4>Список экспертов пуст</h4>
                </#if>
            </div>
        </div>
    </div>

</@body.page>
