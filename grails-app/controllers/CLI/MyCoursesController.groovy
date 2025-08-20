package CLI


import grails.rest.Resource

//@Secured(['ROLE_USER'])
@Resource()
class MyCoursesController {

    def springSecurityService
    def enrollmentApiService

    def index() {
        def currentUser = springSecurityService.currentUser
        def student = Student.findByUser(currentUser)

        if (!student) {
            flash.message = "Student profile not found"
            redirect controller: 'auth', action: 'logout'
            return
        }

        def enrollments = Enrollment.findAllByStudent(student, [sort: 'course.title'])
        def gpa = enrollmentApiService.calculateGPA(student.id)

        [enrollments: enrollments, student: student, gpa: gpa]
    }
}