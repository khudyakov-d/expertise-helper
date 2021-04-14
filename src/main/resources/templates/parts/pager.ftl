<#macro pager url page>
    <#if page.getTotalPages() gt 0>
        <#assign body  = 1..page.getTotalPages()/>
    <#else>
        <#assign body  = []/>
    </#if>

    <div class="mt-3">
        <ul class="pagination">

            <#if page.getTotalPages() gt 0>
                <li class="page-item disabled">
                    <a class="page-link" href="#" tabindex="-1">Страницы</a>
                </li>
            </#if>

            <#list body as p>
                <#if (p - 1) == page.getNumber()>
                    <li class="page-item active">
                        <a class="page-link" href="#" tabindex="-1">${p}</a>
                    </li>
                <#else>
                    <li class="page-item">
                        <a class="page-link" href="${url}?page=${p - 1}&size=${page.getSize()}" tabindex="-1">${p}</a>
                    </li>
                </#if>
            </#list>

        </ul>
    </div>

</#macro>