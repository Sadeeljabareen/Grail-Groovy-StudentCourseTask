<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="main"/>
    <title>Edit Student</title>
    <asset:stylesheet src="application.css"/>
    <asset:javascript src="application.js"/>
    <style>
    .student-photo-preview {
        max-width: 200px;
        max-height: 200px;
        margin-bottom: 10px;
        border-radius: 4px;
    }

    .photo-controls {
        margin-bottom: 15px;
    }
    </style>
    <script>
        $(document).ready(function () {
            // File size validation
            $('input[type="file"]').change(function () {
                if (this.files[0] && this.files[0].size > 2000000) {
                    alert('File size must be less than 2MB');
                    $(this).val('');
                } else {
                    // Preview new image
                    if (this.files && this.files[0]) {
                        var reader = new FileReader();
                        reader.onload = function (e) {
                            $('#photoPreview').attr('src', e.target.result);
                        }
                        reader.readAsDataURL(this.files[0]);
                    }
                }
            });

            // Email validation (exclude current email)
            $('#email').blur(function () {
                const email = $(this).val();
                const currentEmail = '${student?.email}';
                if (email && email !== currentEmail) {
                    $.get("${createLink(controller:'student', action:'checkEmail')}", {email: email}, function (data) {
                        if (data.exists) {
                            $('#emailError').text('Email already exists').show();
                        } else {
                            $('#emailError').hide();
                        }
                    });
                } else {
                    $('#emailError').hide();
                }
            });

            // Handle remove photo checkbox
            $('#removePhoto').change(function () {
                if ($(this).is(':checked')) {
                    $('#photoPreview').hide();
                    $('#photoFileInput').val('');
                } else {
                    $('#photoPreview').show();
                }
            });
        });
    </script>
</head>

<body>
<div class="container">
    <h1>Edit Student</h1>

    <g:if test="${flash.message}">
        <div class="alert alert-success">${flash.message}</div>
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

    <g:uploadForm controller="student" action="update" id="${student?.id}" method="POST">
        <input type="hidden" name="_method" value="PUT"/>
        <input type="hidden" name="version" value="${student?.version}"/>

        <div class="form-group">
            <label for="name">Full Name*</label>
            <g:textField name="name" value="${student?.name}" class="form-control" required=""/>
        </div>

        <div class="form-group">
            <label for="email">Email*</label>
            <g:field type="email" name="email" id="email" value="${student?.email}" class="form-control" required=""/>
            <small id="emailError" class="text-danger" style="display:none;"></small>
        </div>

        <div class="form-group">
            <label>Profile Photo</label>

            <div class="photo-controls">
                <g:if test="${student?.photoUrl}">
                    <img id="photoPreview"
                         src="${createLink(controller: 'student', action: 'serveImage', params: [filename: student.photoUrl])}"
                         class="student-photo-preview"/>

                    <div class="form-check">
                        <input class="form-check-input" type="checkbox" name="removePhoto" id="removePhoto"
                               value="true">
                        <label class="form-check-label" for="removePhoto">
                            Remove current photo
                        </label>
                    </div>
                </g:if>
                <g:else>
                    <img id="photoPreview" src="#" class="student-photo-preview" style="display:none;"/>
                </g:else>
            </div>

            <input type="file" name="photo" id="photoFileInput" class="form-control" accept="image/*"/>
            <small class="text-muted">Max size: 2MB (JPEG, PNG)</small>
        </div>

        <div class="form-group">
            <g:submitButton name="update" class="btn btn-primary" value="Update"/>
            <g:link class="btn btn-secondary" action="show" id="${student?.id}">Cancel</g:link>
        </div>
    </g:uploadForm>
</div>
</body>
</html>