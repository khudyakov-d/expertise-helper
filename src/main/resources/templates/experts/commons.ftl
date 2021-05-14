<#macro list_header expert_header>
    <ul class="list-group">
        <#list expert_header as name>
            <li class="list-group-item">
                <@spring.message "${name}"/>
            </li>
        </#list>
    </ul>
</#macro>

<#macro expert_table expert_header experts>
    <div class="table-responsive">
        <table class="table table-bordered">

            <#if experts?size gt 0>
                <thead>
                <tr>
                    <#list expert_header as name>
                        <th><@spring.message "${name}"/></th>
                    </#list>
                </tr>
                </thead>
            </#if>

            <tbody>
            <#list experts as expert>
                <tr>
                    <td>${expert.name!}</td>
                    <td>${expert.organization!}</td>
                    <td>${expert.post!}</td>
                    <td>${expert.degree.title!}</td>
                    <td>${expert.scienceCategory.category!}</td>
                    <td>${expert.email!}</td>
                    <td>${expert.workPhone!}</td>
                    <td>${expert.personalPhone!}</td>
                    <td>${expert.birthDate!}</td>
                    <td><a href="/experts/${expert.id}" class="btn btn-dark">Изменить</a></td>
                </tr>
            </#list>
            </tbody>

        </table>
    </div>
</#macro>

