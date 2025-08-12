<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="main"/>
    <g:set var="entityName" value="${message(code: 'student.label', default: 'Student')}"/>
    <title><g:message code="default.list.label" args="[entityName]"/></title>
    <style>
    .student-photo-thumb {
        width: 50px;
        height: 50px;
        object-fit: cover;
        border-radius: 4px;
    }
    .no-photo-thumb {
        width: 50px;
        height: 50px;
        background-color: #f8f9fa;
        display: flex;
        align-items: center;
        justify-content: center;
        border-radius: 4px;
        color: #6c757d;
        font-size: 0.8rem;
    }
    </style>
</head>
<body>
<div id="content" role="main">
    <div class="container">
        <section class="row">
            <a href="#list-student" class="skip" tabindex="-1">
                <g:message code="default.link.skip.label" default="Skip to content…"/>
            </a>
            <div class="nav" role="navigation">
                <ul>
                    <li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
                    <li><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]"/></g:link></li>
                </ul>
            </div>
        </section>

        <section class="row">
            <div id="list-student" class="col-12 content scaffold-list" role="main">
                <h1><g:message code="default.list.label" args="[entityName]"/></h1>

                <g:if test="${flash.message}">
                    <div class="alert alert-info" role="alert">${flash.message}</div>
                </g:if>

                <table class="table table-striped">
                    <thead>
                    <tr>
                        <th>Photo</th>
                        <th>Name</th>
                        <th>Email</th>
                        <th>Username</th>
                        <th>Actions</th>
                    </tr>
                    </thead>
                    <tbody>
                    <g:each in="${studentList}" var="student">
                        <tr>
                            <td>
                                <g:if test="${student.photoUrl}">
                                    <img src="${createLink(controller: 'student', action: 'serveImage', params: [filename: student.photoUrl])}"
                                         class="student-photo-thumb" alt="Student photo"/>
                                </g:if>
                                <g:else>
                                    <div class="no-photo-thumb">No Photo</div>
                                </g:else>
                            </td>
                            <td>${student.name}</td>
                            <td>${student.email}</td>
                            <td>${student.user.username}</td>
                            <td>
                                <g:link class="btn btn-sm btn-primary" action="edit" resource="${student}">
                                    <g:message code="default.button.edit.label" default="Edit"/>
                                </g:link>
                                <g:form action="delete" resource="${student}" method="DELETE" style="display:inline;">
                                    <input type="submit" class="btn btn-sm btn-danger" value="${message(code: 'default.button.delete.label', default: 'Delete')}"
                                           onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');"/>
                                </g:form>
                            </td>
                        </tr>
                    </g:each>
                    </tbody>
                </table>

                <g:if test="${studentCount > params.int('max')}">
                    <div class="pagination">
                        <g:paginate total="${studentCount ?: 0}"/>
                    </div>
                </g:if>
            </div>
        </section>
    </div>
</div>
</body>
</html>