<!doctype html>
<html lang="en" class="no-js">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
    <title>
    <g:layoutTitle default="Grails"/>
    </title>
    <meta name="viewport" content="width=device-width, initial-scale=1"/>
    <asset:link rel="icon" href="favicon.ico" type="image/x-ico"/>
    <asset:stylesheet src="application.css"/>

    <style>
:root {
    --primary-color: #8a307f; /* Changed from #4CAF50 */
    --secondary-color: #6883bc; /* Changed from #2E7D32 */
    --light-color: #f8f9fa;
    --dark-color: #343a40;
    --text-color: #212529;
    --accent-color: #79a7d3; /* New color */
}

body {
    font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
    color: var(--text-color);
    display: flex;
    flex-direction: column;
    min-height: 100vh;
    margin: 0;
}

.navbar {
    background-color: var(--primary-color) !important;
    box-shadow: 0 2px 4px rgba(0,0,0,0.1);
    padding: 0.8rem 1rem;
}

    .navbar-brand {
        display: flex;
        align-items: center;
        font-weight: 600;
        color: white !important;
    }

    .navbar-brand img {
        height: 30px;
        margin-right: 10px;
    }

    .navbar-dark .navbar-nav .nav-link {
        color: rgba(255,255,255,0.9);
        padding: 0.5rem 1rem;
        transition: all 0.3s ease;
    }

    .navbar-dark .navbar-nav .nav-link:hover {
        color: white;
        transform: translateY(-2px);
    }

    .main-content {
        flex: 1;
        padding: 2rem 0;
    }

    .footer {
        background-color: var(--dark-color);
        color: white;
        padding: 3rem 0;
        margin-top: auto;
    }

    .footer a {
        color: #6c757d;
        text-decoration: none;
        transition: color 0.3s ease;
    }

    .footer a:hover {
        color: white;
        text-decoration: underline;
    }

    .footer .col {
        margin-bottom: 1.5rem;
        padding: 0 15px;
    }

    .footer img {
        height: 50px;
        margin-right: 15px;
        margin-bottom: 15px;
        float: left;
    }

    .footer strong {
        display: block;
        color: white;
        margin-bottom: 1rem;
        font-size: 1.2rem;
        clear: both;
    }

    .footer p {
        color: #adb5bd;
        line-height: 1.6;
        margin-bottom: 0;
    }

    .footer .footer-copyright {
        border-top: 1px solid rgba(255,255,255,0.1);
        padding-top: 20px;
        margin-top: 20px;
        text-align: center;
        color: #6c757d;
    }

    @media (max-width: 768px) {
        .navbar-brand img {
            height: 25px;
        }
        .footer .row {
            flex-direction: column;
        }
        .footer .col {
            text-align: center;
            margin-bottom: 30px;
        }
        .footer img {
            float: none !important;
            display: block;
            margin: 0 auto 15px;
        }
    }

.alert-danger {
    color: #721c24;
    background-color: #f8d7da;
    border-color: #f5c6cb;
    padding: 0.75rem 1.25rem;
    margin-bottom: 1rem;
    border: 1px solid transparent;
    border-radius: 0.25rem;
}

/* New button styles using the accent color */
.btn-success {
    background-color: var(--secondary-color);
    border-color: var(--secondary-color);
}

.btn-primary {
    background-color: var(--accent-color);
    border-color: var(--accent-color);
}

.btn-outline-primary {
    color: var(--accent-color);
    border-color: var(--accent-color);
}
    </style>

    <g:layoutHead/>
</head>

<body>
<nav class="navbar navbar-expand-lg navbar-dark navbar-static-top" role="navigation">
    <div class="container">
        <a class="navbar-brand" href="/">
            <asset:image src="grails.svg" alt="Grails Logo"/>
            <span>Grails Application</span>
        </a>
        <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarContent">
            <span class="navbar-toggler-icon"></span>
        </button>

        <div class="collapse navbar-collapse" id="navbarContent">
            <ul class="navbar-nav ml-auto">
                <li class="nav-item">
                    <a class="nav-link" href="/">🏠 Home</a>
                </li>

                <sec:ifNotLoggedIn>
                    <li class="nav-item">
                        <g:link class="nav-link" controller="auth" action="login">🔐 Login</g:link>
                    </li>

                </sec:ifNotLoggedIn>

                <sec:ifLoggedIn>
                    <sec:ifAllGranted roles="ROLE_ADMIN">
                        <li class="nav-item">
                            <g:link class="nav-link" controller="student" action="index">👤 Students</g:link>
                        </li>
                        <li class="nav-item">
                            <g:link class="nav-link" controller="course" action="index">📘 Courses</g:link>
                        </li>
                        <li class="nav-item">
                            <g:link class="nav-link" controller="enrollment" action="index">📝 Enrollments</g:link>
                        </li>
                    </sec:ifAllGranted>

                    <sec:ifAllGranted roles="ROLE_USER">
                        <li class="nav-item">
                            <g:link class="nav-link" controller="myCourses" action="index">📚 My Courses and GPA</g:link>
                        </li>
                    </sec:ifAllGranted>
                    <sec:ifAnyGranted roles="ROLE_USER,ROLE_ADMIN">
                        <li class="nav-item">
                            <g:link class="nav-link" controller="book" action="index">📚 Library</g:link>
                        </li>
                    </sec:ifAnyGranted>

                    <li class="nav-item">
                        <g:link class="nav-link" controller="auth" action="logout">🚪 Logout (<sec:username/>)</g:link>
                    </li>
                </sec:ifLoggedIn>
            </ul>
        </div>
    </div>
</nav>

<main class="main-content">
    <div class="container">
        <g:if test="${flash.error}">
            <div class="alert alert-danger">${flash.error}</div>
        </g:if>
        <g:layoutBody/>
    </div>
</main>

<footer class="footer" role="contentinfo">
    <div class="container">

        <div class="footer-copyright">
            <p>&copy; <g:formatDate date="${new Date()}" format="yyyy"/> Grails Application. All rights reserved.</p>
        </div>
    </div>
</footer>

<asset:javascript src="application.js"/>
</body>
</html>