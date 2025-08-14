<%@ page import="CLI.Student" %>
<%@ page import="CLI.User" %>
<!doctype html>
<html>
<head>
    <meta name="layout" content="main"/>
    <title>University System</title>
    <style>
    .card-container {
        display: flex;
        flex-wrap: wrap;
        gap: 20px;
    }

    .card-item {
        flex: 1;
        min-width: 300px;
        display: flex;
    }

    .dashboard-card {
        border: 1px solid #ddd;
        border-radius: 8px;
        padding: 20px;
        box-shadow: 0 2px 4px rgba(0,0,0,0.1);
        transition: all 0.3s ease;
        background-color: #f8f9fa;
        width: 100%;
        display: flex;
        flex-direction: column;
    }

    .dashboard-card:hover {
        transform: translateY(-5px);
        box-shadow: 0 5px 15px rgba(0,0,0,0.1);
    }

    .dashboard-card h3 {
        color: #8a307f;
        margin-top: 0;
    }

    .dashboard-card-content {
        flex-grow: 1;
        margin-bottom: 15px;
    }

    .action-links {
        margin-top: auto;
    }

    .action-links a {
        display: inline-block;
        margin-right: 10px;
        color: #6883bc;
        font-weight: 500;
    }

    .welcome-message {
        background-color: #f8f9fa;
        padding: 20px;
        border-radius: 8px;
        margin-bottom: 30px;
        border-left: 5px solid #79a7d3;
    }

    .btn {
        padding: 0.375rem 0.75rem;
        border-radius: 0.25rem;
        text-decoration: none;
    }

    .btn-success {
        color: #fff;
        background-color: #6883bc;
        border-color: #6883bc;
    }

    .btn-primary {
        color: #fff;
        background-color: #79a7d3;
        border-color: #79a7d3;
    }

    .btn-outline-primary {
        color: #79a7d3;
        background-color: transparent;
        border: 1px solid #79a7d3;
    }
    </style>
</head>
<body>
<div id="content" role="main">
    <div class="container">
        <section>
            <sec:ifNotLoggedIn>
                <div class="welcome-message">
                    <h1>Welcome to University System</h1>
                    <p class="lead">
                        Please <g:link controller="auth" action="login" class="btn btn-success">Login</g:link> or
                        to access the system.
                    </p>
                </div>
            </sec:ifNotLoggedIn>

            <sec:ifAllGranted roles="ROLE_ADMIN">
                <div class="welcome-message">
                    <h1>Welcome, <sec:username/>! <small>(Administrator)</small></h1>
                    <p>You have full access to manage the university system.</p>
                </div>

                <div class="card-container">
                    <div class="card-item">
                        <div class="dashboard-card">
                            <div class="dashboard-card-content">
                                <h3>👤 Student Management</h3>
                                <p>Manage all student records and profiles in the system.</p>
                            </div>
                            <div class="action-links">
                                <g:link controller="student" action="index" class="btn btn-outline-primary">View Students</g:link>
                                <g:link controller="student" action="create" class="btn btn-primary">Add New</g:link>
                            </div>
                        </div>
                    </div>

                    <div class="card-item">
                        <div class="dashboard-card">
                            <div class="dashboard-card-content">
                                <h3>📘 Course Management</h3>
                                <p>Manage all available courses and their details.</p>
                            </div>
                            <div class="action-links">
                                <g:link controller="course" action="index" class="btn btn-outline-primary">View Courses</g:link>
                                <g:link controller="course" action="create" class="btn btn-primary">Add New</g:link>
                            </div>
                        </div>
                    </div>

                    <div class="card-item">
                        <div class="dashboard-card">
                            <div class="dashboard-card-content">
                                <h3>📝 Enrollment Management</h3>
                                <p>Manage student enrollments and grades.</p>
                            </div>
                            <div class="action-links">
                                <g:link controller="enrollment" action="index" class="btn btn-outline-primary">View Enrollments</g:link>
                                <g:link controller="enrollment" action="create" class="btn btn-primary">Add New</g:link>
                            </div>
                        </div>
                    </div>
                </div>
            </sec:ifAllGranted>

            <sec:ifAllGranted roles="ROLE_USER">
                <div class="welcome-message">
                    <h1>Welcome, <sec:username/>! <small>(Student)</small></h1>
                    <p>You can view your courses and academic progress.</p>
                </div>

                <div class="card-container">
                    <div class="card-item">
                        <div class="dashboard-card">
                            <div class="dashboard-card-content">
                                <h3>📚 My Courses and GPA</h3>
                                <p>View all courses you are enrolled in and your progress and academic performance.</p>
                            </div>
                            <div class="action-links">
                                <g:link controller="myCourses" action="index" class="btn btn-primary">View My Courses</g:link>
                            </div>
                        </div>
                    </div>

                    <div class="card-item">
                        <div class="dashboard-card">
                            <div class="dashboard-card-content">
                                <h3>🔔 Notifications</h3>
                                <p>You have no new notifications.</p>
                            </div>
                        </div>
                    </div>
                </div>
            </sec:ifAllGranted>
        </section>
    </div>
</div>
</body>
</html>