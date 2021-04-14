    <#import 'parts/body.ftl' as c>

<@c.page "Вход">
    <div class="container d-flex min-vh-100">
        <div class="row justify-content-center align-self-center w-100">
            <div class="col-sm-8 col-lg-4 ">

                <#if message??>
                    <div class="alert alert-${messageType}" role="alert">${message!}</div>
                </#if>

                <h3 class="text-center">Вход</h3>
                <div class="d-flex justify-content-center">
                    <a class="btn btn-dark m-1" href="/oauth2/authorization/google" role="button">Google</a>
                    <a class="btn btn-dark m-1" href="/oauth2/authorization/yandex" role="button">Yandex</a>
                </div>

            </div>
        </div>
    </div>
</@c.page>
