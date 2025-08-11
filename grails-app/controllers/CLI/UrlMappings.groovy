package CLI

class UrlMappings {

    static mappings = {
        "/uploads/$filename**"(controller: 'student', action: 'serveImage')
        // RESTful endpoints + GSP pages
        "/student"(controller: 'student') {
            action = [GET: 'index', POST: 'save']
        }
        "/student/create"(controller: 'student', action: 'create')
        "/student/edit/$id"(controller: 'student', action: 'edit')
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


        // errors
        "500"(view: '/error')
        "404"(view: '/notFound')
    }
}
