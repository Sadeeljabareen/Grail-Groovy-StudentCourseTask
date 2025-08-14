<!DOCTYPE html>
<html>
    <head>
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'enrollment.label', default: 'Enrollment')}" />
        <title><g:message code="default.list.label" args="[entityName]" /></title>
    </head>
    <body>
    <div id="content" role="main">
        <div class="container">
            <section class="row">
                <a href="#list-enrollment" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
                <div class="nav" role="navigation">
                    <ul>
                        <li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
                        <li><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
                    </ul>
                </div>
            </section>

            <section class="row">
                <div class="col-12">
                    <g:form action="index" method="GET">
                        <div class="card mb-4">
                            <div class="card-header">Search Enrollments</div>
                            <div class="card-body">
                                <div class="row">
                                    <div class="col-md-4">
                                        <div class="form-group">
                                            <label for="studentName">Student Name</label>
                                            <g:textField name="studentName" value="${params.studentName}" class="form-control" placeholder="Search by student name"/>
                                        </div>
                                    </div>
                                    <div class="col-md-4">
                                        <div class="form-group">
                                            <label for="courseTitle">Course Title</label>
                                            <g:textField name="courseTitle" value="${params.courseTitle}" class="form-control" placeholder="Search by course title"/>
                                        </div>
                                    </div>
                                    <div class="col-md-4">
                                        <div class="form-group">
                                            <label for="grade">Grade</label>
                                            <g:textField name="grade" value="${params.grade}" class="form-control" placeholder="Search by grade"/>
                                        </div>
                                    </div>
                                </div>
                                <div class="row mt-2">
                                    <div class="col-12">
                                        <g:submitButton name="search" value="Search" class="btn btn-primary"/>
                                        <g:link action="index" class="btn btn-secondary">Reset</g:link>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </g:form>
                </div>
            </section>

            <section class="row">
                <div id="list-enrollment" class="col-12 content scaffold-list" role="main">
                    <h1><g:message code="default.list.label" args="[entityName]" /></h1>
                    <g:if test="${flash.message}">
                        <div class="message" role="status">${flash.message}</div>
                    </g:if>
                    <f:table collection="${enrollmentList}" />

                    <g:if test="${enrollmentCount > params.int('max')}">
                    <div class="pagination">
                        <g:paginate total="${enrollmentCount ?: 0}" />
                    </div>
                    </g:if>
                </div>
            </section>
        </div>
    </div>
    </body>
</html>