<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="main" />
    <g:set var="entityName" value="${message(code: 'course.label', default: 'Course')}" />
    <title><g:message code="default.list.label" args="[entityName]" /></title>
</head>
<body>
<a href="#list-course" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
<div class="nav" role="navigation">
    <ul>
        <li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
        <li><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
    </ul>
</div>
<div id="list-course" class="content scaffold-list" role="main">
    <h1><g:message code="default.list.label" args="[entityName]" /></h1>
    <g:if test="${flash.message}">
        <div class="message" role="status">${flash.message}</div>
    </g:if>
    <table>
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
                <td><g:link action="show" id="${course.id}">${course.title}</g:link></td>
                <td>${course.description}</td>
                <td>${course.credits}</td>
                <td>
                    <g:link action="edit" id="${course.id}">Edit</g:link> |
                    <g:link action="delete" id="${course.id}" onclick="return confirm('Are you sure?');">Delete</g:link>
                </td>
            </tr>
        </g:each>
        </tbody>
    </table>

    <div class="pagination">
        <g:paginate total="${courseCount ?: 0}" />
    </div>
</div>
</body>
</html>