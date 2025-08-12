<!DOCTYPE html>
<html>
<head>
  <meta name="layout" content="main"/>
  <title>Student Details</title>
  <style>
  .student-profile {
    display: flex;
    flex-wrap: wrap;
    gap: 2rem;
    margin-top: 2rem;
  }
  .student-photo-container {
    flex: 0 0 300px;
  }
  .student-photo {
    width: 100%;
    max-width: 300px;
    height: auto;
    border-radius: 8px;
    box-shadow: 0 2px 8px rgba(0,0,0,0.1);
  }
  .student-details {
    flex: 1;
    min-width: 300px;
  }
  .detail-row {
    margin-bottom: 1rem;
    padding: 0.5rem;
    background: #f8f9fa;
    border-radius: 4px;
  }
  .detail-label {
    font-weight: bold;
    color: #495057;
    margin-bottom: 0.25rem;
  }
  .detail-value {
    color: #212529;
  }
  .enrollments-table {
    width: 100%;
    margin-top: 2rem;
    border-collapse: collapse;
  }
  .enrollments-table th {
    background-color: #343a40;
    color: white;
    padding: 0.75rem;
    text-align: left;
  }
  .enrollments-table td {
    padding: 0.75rem;
    border-bottom: 1px solid #dee2e6;
  }
  .enrollments-table tr:nth-child(even) {
    background-color: #f8f9fa;
  }
  .action-buttons {
    margin-top: 2rem;
    display: flex;
    gap: 1rem;
  }
  .no-photo {
    width: 300px;
    height: 300px;
    background-color: #e9ecef;
    display: flex;
    align-items: center;
    justify-content: center;
    border-radius: 8px;
    color: #6c757d;
  }
  </style>
</head>
<body>
<div class="nav" role="navigation">
  <ul>
    <li><g:link class="list" action="index">Students List</g:link></li>
    <li><g:link class="create" action="create">Add New Student</g:link></li>
  </ul>
</div>

<div id="show-student" class="content scaffold-show" role="main">
  <h1>Student Details</h1>

  <g:if test="${flash.message}">
    <div class="message" role="status">${flash.message}</div>
  </g:if>

  <div class="student-profile">
    <div class="student-photo-container">
      <g:if test="${student.photoUrl}">
        <img src="${createLink(controller: 'student', action: 'serveImage', params: [filename: student.photoUrl])}"
             alt="Student Photo" class="student-photo"/>
      </g:if>
      <g:else>
        <div class="no-photo">No Photo Available</div>
      </g:else>
    </div>

    <div class="student-details">
      <div class="detail-row">
        <div class="detail-label">Full Name</div>
        <div class="detail-value">${student.name}</div>
      </div>

      <div class="detail-row">
        <div class="detail-label">Email</div>
        <div class="detail-value">${student.email}</div>
      </div>

      <div class="detail-row">
        <div class="detail-label">Username</div>
        <div class="detail-value">${student.user.username}</div>
      </div>

    </div>
  </div>

  <h2 style="margin-top: 2rem;">Enrolled Courses</h2>
  <g:if test="${enrollments}">
    <table class="enrollments-table">
      <thead>
      <tr>
        <th>Course Title</th>
        <th>Description</th>
        <th>Credit Hours</th>
        <th>Grade</th>
        <th>Enrollment Date</th>
      </tr>
      </thead>
      <tbody>
      <g:each in="${enrollments}" var="enrollment">
        <tr>
          <td><g:link controller="course" action="show" id="${enrollment.course.id}">${enrollment.course.title}</g:link></td>
          <td>${enrollment.course.description}</td>
          <td>${enrollment.course.credits}</td>
          <td>${enrollment.grade ?: 'Not Graded Yet'}</td>
          <td><g:formatDate date="${enrollment.enrollmentDate}" format="yyyy-MM-dd"/></td>
        </tr>
      </g:each>
      </tbody>
    </table>
  </g:if>
  <g:else>
    <p>Student is not enrolled in any courses yet</p>
  </g:else>

  <div class="action-buttons">
    <g:form resource="${student}" method="DELETE">
      <g:link class="edit" action="edit" resource="${student}">
        <button type="button" class="btn btn-primary">Edit Details</button>
      </g:link>
      <button type="submit" class="btn btn-danger" onclick="return confirm('Are you sure you want to delete this student?');">Delete Student</button>
    </g:form>
  </div>
</div>
</body>
</html>