<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="main"/>
    <title>Edit Grade</title>
    <style>
    .form-container {
        max-width: 500px;
        margin: 30px auto;
        padding: 20px;
        background-color: #f8f9fa;
        border-radius: 8px;
        box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
    }

    .form-group {
        margin-bottom: 20px;
    }

    .info-box {
        background-color: #e9ecef;
        padding: 15px;
        border-radius: 5px;
        margin-bottom: 20px;
    }
    </style>
</head>

<body>
<div class="container">
    <div class="form-container">
        <h2 class="text-center mb-4">Edit Grade</h2>

        <div class="info-box">
            <p><strong>Student:</strong> ${enrollment.student.name}</p>

            <p><strong>Course:</strong> ${enrollment.course.title}</p>
        </div>

        <g:if test="${flash.message}">
            <div class="alert alert-info">${flash.message}</div>
        </g:if>

        <g:hasErrors bean="${enrollment}">
            <div class="alert alert-danger">
                <ul>
                    <g:eachError bean="${enrollment}" var="error">
                        <li><g:message error="${error}"/></li>
                    </g:eachError>
                </ul>
            </div>
        </g:hasErrors>

        <g:form action="updateGrade" id="${enrollment.id}" method="POST">
            <input type="hidden" name="returnTo" value="${params.returnTo}"/>

            <div class="form-group">
                <label for="grade">Grade (0-4):</label>
                <g:field type="number" name="grade"
                         value="${enrollment.grade}"
                         min="0" max="4" step="0.1"
                         class="form-control" required=""/>
                <small class="text-muted">Enter a value between 0.0 and 4.0</small>
            </div>

            <div class="form-group text-right">
                <g:link controller="${params.returnTo == 'student' ? 'student' : 'enrollment'}"
                        action="show"
                        id="${params.returnTo == 'student' ? enrollment.student.id : enrollment.id}"
                        class="btn btn-secondary mr-2">
                    Cancel
                </g:link>
                <button type="submit" class="btn btn-primary">
                    Update Grade
                </button>
            </div>
        </g:form>
    </div>
</div>
</body>
</html>