<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="main"/>
    <title>Continue to Login Page</title>
</head>
<body>
<h1>Continue to Login Page</h1>

<g:if test="${flash.message}">
    <div class="alert">${flash.message}</div>
</g:if>

<g:form controller="login" action="auth">

    <div>
        <g:submitButton name="Continue" value="Continue"/>
    </div>
</g:form>

</body>
</html>
