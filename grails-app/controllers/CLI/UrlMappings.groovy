package CLI

class UrlMappings {

    static mappings = {
        "/student/serveImage/$filename"(controller: 'student', action: 'serveImage')

        "/uploads/$filename**"(controller: 'student', action: 'serveImage')
        "/student"(controller: 'student') {
            action = [GET: 'index', POST: 'save']
        }
        "/student/create"(controller: 'student', action: 'create')
        "/student/edit/$id"(controller: 'student', action: 'edit')
        "/student/$id"(controller: 'student', action: 'update', method: "PUT")
        "/student/show/$id"(controller: 'student', action: 'show')
        "/student/delete/$id"(controller: 'student', action: 'delete', method: "DELETE")

        "/course"(controller: 'course') {
            action = [GET: 'index', POST: 'save']
        }
        "/course/create"(controller: 'course', action: 'create')
        "/course/edit/$id"(controller: 'course', action: 'edit')
        "/course/show/$id"(controller: 'course', action: 'show')
        "/course/delete/$id"(controller: 'course', action: 'delete', method: "DELETE")

        "/enrollment"(controller: 'enrollment') {
            action = [GET: 'index', POST: 'save']
        }
        "/enrollment/create"(controller: 'enrollment', action: 'create')
        "/enrollment/edit/$id"(controller: 'enrollment', action: 'edit')
        "/enrollment/show/$id"(controller: 'enrollment', action: 'show')
        "/enrollment/delete/$id"(controller: 'enrollment', action: 'delete', method: "DELETE")
        "/myCourses"(controller: 'myCourses', action: 'index')

        // GPA custom route
        "/enrollment/gpa"(controller: 'enrollment', action: 'gpa', method: 'GET')

        // fallback
        "/$controller/$action?/$id?(.$format)?" {
            constraints {
                // apply constraints here
            }
        }

        // home page
        "/"(view: "/index")

// API
        "/api/students"(resources: "student", namespace: 'api')
        "/api/courses"(resources: "course", namespace: 'api')
        "/api/enrollments"(resources: "enrollment", namespace: 'api')
        "/api/users"(resources: "user", namespace: 'api')

        // errors
        "500"(view: '/error')
        "404"(view: '/notFound')
    }
}
