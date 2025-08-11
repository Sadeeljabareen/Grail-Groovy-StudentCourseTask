<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="main"/>
    <title>Create Student</title>
    <asset:stylesheet src="application.css"/>
    <asset:javascript src="application.js"/>
    <script>
        $(document).ready(function() {
            // التحقق من حجم الملف
            $('input[type="file"]').change(function() {
                if (this.files[0] && this.files[0].size > 2000000) {
                    alert('File size must be less than 2MB');
                    $(this).val('');
                }
            });

            // التحقق من البريد الإلكتروني فورياً
            $('#email').blur(function() {
                const email = $(this).val();
                if (email) {
                    $.get("${createLink(controller:'student', action:'checkEmail')}", {email: email}, function(data) {
                        if (data.exists) {
                            $('#emailError').text('Email already exists').show();
                        } else {
                            $('#emailError').hide();
                        }
                    });
                }
            });

            // التحقق من اسم المستخدم فورياً
            $('#username').blur(function() {
                const username = $(this).val();
                if (username) {
                    $.get("${createLink(controller:'student', action:'checkUsername')}", {username: username}, function(data) {
                        if (data.exists) {
                            $('#usernameError').text('Username already exists').show();
                        } else {
                            $('#usernameError').hide();
                        }
                    });
                }
            });
        });
    </script>
</head>
<body>
<div class="container">
    <h1>Create Student</h1>

    <g:if test="${flash.message}">
        <div class="alert alert-info">${flash.message}</div>
    </g:if>

    <g:if test="${flash.error}">
        <div class="alert alert-danger">${flash.error}</div>
    </g:if>

    <g:hasErrors bean="${student}">
        <div class="alert alert-danger">
            <ul>
                <g:eachError bean="${student}" var="error">
                    <li><g:message error="${error}"/></li>
                </g:eachError>
            </ul>
        </div>
    </g:hasErrors>

    <g:uploadForm action="save">
        <div class="form-group">
            <label>Full Name*</label>
            <g:textField name="name" value="${student?.name}" class="form-control" required=""/>
        </div>

        <div class="form-group">
            <label>Email*</label>
            <g:field type="email" name="email" id="email" value="${student?.email}" class="form-control" required=""/>
            <small id="emailError" class="text-danger" style="display:none;"></small>
        </div>

        <div class="form-group">
            <label>Username*</label>
            <g:textField name="username" id="username" value="${user?.username}" class="form-control" required=""/>
            <small id="usernameError" class="text-danger" style="display:none;"></small>
        </div>

        <div class="form-group">
            <label>Password*</label>
            <g:passwordField name="password" class="form-control" required=""/>
            <small class="text-muted">Minimum 6 characters</small>
        </div>

        <div class="form-group">
            <label>Profile Photo</label>
            <input type="file" name="photo" class="form-control" accept="image/*"/>
            <small class="text-muted">Max size: 2MB (JPEG, PNG)</small>
        </div>

        <div class="form-group">
            <g:submitButton name="create" class="btn btn-primary" value="Create"/>
            <g:link class="btn btn-default" action="index">Cancel</g:link>
        </div>
    </g:uploadForm>
</div>
</body>
</html>