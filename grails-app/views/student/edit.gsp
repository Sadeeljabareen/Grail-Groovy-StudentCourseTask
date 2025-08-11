<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="main"/>
    <title>Edit Student</title>
    <asset:stylesheet src="application.css"/>
    <asset:javascript src="application.js"/>
    <script>
        $(document).ready(function() {
            $('input[type="file"]').change(function() {
                if (this.files[0] && this.files[0].size > 2000000) {
                    alert('File size must be less than 2MB');
                    $(this).val('');
                }
            });

            // التحقق من البريد الإلكتروني (لا يشمل البريد الحالي)
            $('#email').blur(function() {
                const email = $(this).val();
                if (email && email != '${student?.email}') {
                    $.get("${createLink(controller:'student', action:'checkEmail')}", {email: email}, function(data) {
                        if (data.exists) {
                            $('#emailError').text('Email already exists').show();
                        } else {
                            $('#emailError').hide();
                        }
                    });
                }
            });
        });
    </script>
</head>
<body>
<div class="container">
    <h1>Edit Student</h1>

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

    <g:uploadForm action="update" id="${student?.id}">
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
            <label>Current Photo</label>
            <g:if test="${student?.photoUrl}">
                <img src="${student.photoUrl}" class="img-thumbnail" style="max-width: 200px;"/>
                <div class="form-check mt-2">
                    <input class="form-check-input" type="checkbox" name="removePhoto" id="removePhoto">
                    <label class="form-check-label" for="removePhoto">
                        Remove current photo
                    </label>
                </div>
            </g:if>
            <g:else>
                <p class="text-muted">No photo uploaded</p>
            </g:else>
            <input type="file" name="photo" class="form-control mt-2" accept="image/*"/>
            <small class="text-muted">Max size: 2MB (JPEG, PNG)</small>
        </div>

        <div class="form-group">
            <g:submitButton name="update" class="btn btn-primary" value="Update"/>
            <g:link class="btn btn-default" action="index">Cancel</g:link>
        </div>
    </g:uploadForm>
</div>
</body>
</html>