<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="main"/>
    <g:set var="entityName" value="${message(code: 'course.label', default: 'Course')}"/>
    <title><g:message code="default.list.label" args="[entityName]"/></title>
</head>

<body>
<div id="content" role="main">
    <div class="container">
        <!-- Navigation -->
        <section class="row">
            <a href="#list-course" class="skip" tabindex="-1">
                <g:message code="default.link.skip.label" default="Skip to content…"/>
            </a>

            <div class="nav" role="navigation">
                <ul>
                    <li><a class="home" href="${createLink(uri: '/')}">
                        <g:message code="default.home.label"/>
                    </a></li>
                    <li><g:link class="create" action="create">
                        <g:message code="default.new.label" args="[entityName]"/>
                    </g:link></li>
                </ul>
            </div>
        </section>


        <section class="row">
            <div class="col-12">
                <g:form action="index" method="GET">
                    <div class="card mb-4">
                        <div class="card-header">Search Courses</div>

                        <div class="card-body">
                            <div class="row">
                                <div class="col-md-4">
                                    <div class="form-group">
                                        <label for="title">Title</label>
                                        <g:textField name="title" value="${params.title}" class="form-control"
                                                     placeholder="Search by title"/>
                                    </div>
                                </div>

                                <div class="col-md-4">
                                    <div class="form-group">
                                        <label for="description">Description</label>
                                        <g:textField name="description" value="${params.description}"
                                                     class="form-control" placeholder="Search by description"/>
                                    </div>
                                </div>

                                <div class="col-md-4">
                                    <div class="form-group">
                                        <label for="credits">Credits</label>
                                        <g:textField name="credits" value="${params.credits}" class="form-control"
                                                     placeholder="Search by credits"/>
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


        <!-- Course List -->
        <section class="row">
            <div id="list-course" class="col-12 content scaffold-list" role="main">
                <h1><g:message code="default.list.label" args="[entityName]"/></h1>

                <g:if test="${flash.message}">
                    <div class="message" role="status">${flash.message}</div>
                </g:if>

                <table class="table table-striped">
                    <thead>
                    <tr>
                        <th>Title</th>
                        <th>Description</th>
                        <th>Credits</th>
                        <th>Actions</th>
                    </tr>
                    </thead>
                    <tbody>
                    <g:each in="${courseList}" var="course">
                        <tr>
                            <td>
                                <g:link action="edit" resource="${course}">${course.title}</g:link>
                            </td>
                            <td>
                                <g:link action="edit" resource="${course}">${course.description}</g:link>
                            </td>
                            <td>
                                <g:link action="edit" resource="${course}">${course.credits}</g:link>
                            </td>
                            <td>
                                <g:link class="btn btn-sm btn-primary" action="edit" resource="${course}">Edit</g:link>
                                <g:form action="delete" resource="${course}" method="DELETE" style="display:inline;">
                                    <input type="submit" class="btn btn-sm btn-danger" value="Delete"
                                           onclick="return confirm('Are you sure you want to delete this course?');"/>
                                </g:form>
                            </td>
                        </tr>
                    </g:each>
                    </tbody>
                </table>

                <g:if test="${courseCount > params.int('max')}">
                    <div class="pagination">
                        <g:paginate total="${courseCount ?: 0}" params="${params}" action="index" controller="course"/>
                    </div>
                </g:if>
            </div>
        </section>
    </div>
</div>
</body>
</html>
