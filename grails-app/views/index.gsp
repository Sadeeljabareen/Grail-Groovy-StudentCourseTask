<%@ page import="CLI.Student" %>
<%@ page import="CLI.User" %>
<!doctype html>
<html>
<head>
    <meta name="layout" content="main"/>
    <title>University System</title>
    <style>
    .dashboard-card {
        border: 1px solid #ddd;
        border-radius: 8px;
        padding: 20px;
        margin-bottom: 20px;
        box-shadow: 0 2px 4px rgba(0,0,0,0.1);
        transition: all 0.3s ease;
        background-color: #f8f9fa;
    }
    .dashboard-card:hover {
        transform: translateY(-5px);
        box-shadow: 0 5px 15px rgba(0,0,0,0.1);
    }
    .dashboard-card h3 {
        color: #8a307f; /* Changed from #2E7D32 */
        margin-top: 0;
    }
    .action-links {
        margin-top: 15px;
    }
    .action-links a {
        display: inline-block;
        margin-right: 10px;
        color: #6883bc; /* Changed from #4CAF50 */
        font-weight: 500;
    }
    .welcome-message {
        background-color: #f8f9fa;
        padding: 20px;
        border-radius: 8px;
        margin-bottom: 30px;
        border-left: 5px solid #79a7d3; /* New accent color */
    }
    .btn {
        padding: 0.375rem 0.75rem;
        border-radius: 0.25rem;
        text-decoration: none;
    }
    .btn-success {
        color: #fff;
        background-color: #6883bc; /* Changed from #28a745 */
        border-color: #6883bc; /* Changed from #28a745 */
    }
    .btn-primary {
        color: #fff;
        background-color: #79a7d3; /* Changed from #007bff */
        border-color: #79a7d3; /* Changed from #007bff */
    }
    .btn-outline-primary {
        color: #79a7d3; /* Changed from #007bff */
        background-color: transparent;
        border: 1px solid #79a7d3; /* Changed from #007bff */
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

                <div class="row">
                    <div class="col-md-4">
                        <div class="dashboard-card">
                            <h3>👤 Student Management</h3>
                            <p>Manage all student records and profiles in the system.</p>
                            <div class="action-links">
                                <g:link controller="student" action="index" class="btn btn-outline-primary">View Students</g:link>
                                <g:link controller="student" action="create" class="btn btn-primary">Add New</g:link>
                            </div>
                        </div>
                    </div>

                    <div class="col-md-4">
                        <div class="dashboard-card">
                            <h3>📘 Course Management</h3>
                            <p>Manage all available courses and their details.</p>
                            <div class="action-links">
                                <g:link controller="course" action="index" class="btn btn-outline-primary">View Courses</g:link>
                                <g:link controller="course" action="create" class="btn btn-primary">Add New</g:link>
                            </div>
                        </div>
                    </div>

                    <div class="col-md-4">
                        <div class="dashboard-card">
                            <h3>📝 Enrollment Management</h3>
                            <p>Manage student enrollments and grades.</p>
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

                <div class="row">
                    <div class="col-md-6">
                        <div class="dashboard-card">
                            <h3>📚 My Courses and GPA</h3>
                            <p>View all courses you are enrolled in and your progress and academic performance.</p>
                            <div class="action-links">
                                <g:link controller="myCourses" action="index" class="btn btn-primary">View My Courses</g:link>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="dashboard-card">
                    <h3>🔔 Notifications</h3>
                    <p>You have no new notifications.</p>
                </div>
            </sec:ifAllGranted>
        </section>
    </div>
</div>
</body>
</html>