<!doctype html>
<html>
<head>
    <meta name="layout" content="main"/>
    <title>My Courses</title>
    <style>
    .gpa-display {
        background-color: #f8f9fa;
        border-radius: 8px;
        padding: 20px;
        margin-bottom: 30px;
        text-align: center;
    }

    .gpa-value {
        font-size: 3rem;
        font-weight: bold;
        color: #2E7D32;
    }

    .course-card {
        border: 1px solid #ddd;
        border-radius: 8px;
        padding: 15px;
        margin-bottom: 15px;
    }

    .grade-badge {
        display: inline-block;
        padding: 3px 8px;
        border-radius: 4px;
        font-weight: bold;
        background-color: #e9ecef;
    }
    </style>
</head>

<body>
<div class="container">
    <h1>My Courses <small>${student.name}</small></h1>

    <div class="gpa-display">
        <h3>Your Cumulative GPA</h3>

        <div class="gpa-value">${gpa ?: 'N/A'}</div>

        <p>Based on all completed courses</p>
    </div>

    <div class="row">
        <div class="col-md-12">
            <h3>Current Enrollments</h3>

            <g:if test="${enrollments}">
                <g:each in="${enrollments}" var="enrollment">
                    <div class="course-card">
                        <h4>${enrollment.course.title}
                            <small>${enrollment.course.credits} credits</small>
                        </h4>

                        <p>${enrollment.course.description}</p>

                        <g:if test="${enrollment.grade != null}">
                            <div>Grade: <span class="grade-badge">${enrollment.grade}</span></div>

                            <div>Status: Completed</div>
                        </g:if>
                        <g:else>
                            <div>Status: In Progress</div>
                        </g:else>
                    </div>
                </g:each>
            </g:if>
            <g:else>
                <div class="alert alert-info">You are not enrolled in any courses yet.</div>
            </g:else>
        </div>
    </div>
</div>
</body>
</html>