<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="main"/>
    <title>Create Enrollment</title>
    <style>
    .form-container {
        max-width: 600px;
        margin: 0 auto;
        padding: 20px;
        background-color: #f8f9fa;
        border-radius: 8px;
        box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
    }

    .form-label {
        font-weight: 500;
        margin-bottom: 5px;
    }

    .select-field {
        width: 100%;
        padding: 8px;
        border-radius: 4px;
        border: 1px solid #ced4da;
    }

    .grade-input {
        width: 100px;
        padding: 8px;
        border-radius: 4px;
        border: 1px solid #ced4da;
    }
    </style>
</head>

<body>
<div class="container mt-4">
    <div class="form-container">
        <h2 class="text-center mb-4">Create New Enrollment</h2>

        <g:if test="${flash.message}">
            <div class="alert alert-${flash.messageType ?: 'warning'} mt-3">
                ${flash.message}
            </div>
        </g:if>

        <g:hasErrors bean="${enrollment}">
            <div class="alert alert-danger mt-3">
                <ul class="mb-0">
                    <g:eachError bean="${enrollment}" var="error">
                        <li><g:message error="${error}"/></li>
                    </g:eachError>
                </ul>
            </div>
        </g:hasErrors>

        <g:form controller="enrollment" action="save" class="mt-4">
            <div class="mb-4">
                <label for="student.id" class="form-label">Student:</label>
                <g:select name="student.id"
                          from="${students}"
                          optionKey="id"
                          optionValue="name"
                          value="${enrollment?.student?.id}"
                          class="form-control select-field"
                          noSelection="['': '-- Select Student --']"
                          required="true"/>
                <g:fieldError field="student" bean="${enrollment}" class="text-danger small"/>
            </div>

            <div class="mb-4">
                <label for="course.id" class="form-label">Course:</label>
                <g:select name="course.id"
                          from="${courses}"
                          optionKey="id"
                          optionValue="title"
                          value="${enrollment?.course?.id}"
                          class="form-control select-field"
                          noSelection="['': '-- Select Course --']"
                          required="true"/>
                <g:fieldError field="course" bean="${enrollment}" class="text-danger small"/>
            </div>

            <div class="mb-4">
                <label for="grade" class="form-label">Grade (0-4):</label>
                <g:field type="number" name="grade"
                         min="0" max="4" step="0.1"
                         value="${enrollment?.grade}"
                         class="form-control grade-input"/>
                <g:fieldError field="grade" bean="${enrollment}" class="text-danger small"/>
            </div>

            <div class="d-flex justify-content-between mt-4">
                <g:link controller="enrollment" action="index" class="btn btn-secondary">
                    <i class="fas fa-arrow-left"></i> Back to List
                </g:link>
                <button type="submit" class="btn btn-primary">
                    <i class="fas fa-save"></i> Enroll Student
                </button>
            </div>
        </g:form>
    </div>
</div>
</body>
</html>