<!DOCTYPE html>
<html>
<head>
  <meta name="layout" content="main"/>
  <title>Register</title>
</head>
<body>
<h2>Create a New Account</h2>

<g:if test="${flash.message}">
  <div class="alert">${flash.message}</div>
</g:if>

<g:form controller="auth" action="saveUser">
  <div>
    <label for="username">Username</label><br>
    <g:textField name="username" required="true"/>
  </div>
  <div>
    <label for="password">Password</label><br>
    <g:passwordField name="password" required="true"/>
  </div>
  <div>
    <g:submitButton name="register" value="Register"/>
  </div>
</g:form>

<p>Already have an account? <g:link controller="auth" action="login">Login here</g:link>.</p>
</body>
</html>
