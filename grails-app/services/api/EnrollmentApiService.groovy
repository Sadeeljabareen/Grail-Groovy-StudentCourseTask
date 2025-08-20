package api

import grails.gorm.transactions.Transactional
import CLI.Enrollment
import CLI.Student
import CLI.Course

@Transactional
class EnrollmentApiService {

    Map enroll(Long studentId, Long courseId) {
        def student = Student.get(studentId)
        def course  = Course.get(courseId)
        if (!student || !course) {
            return [ok: false, status: 404, msg: "Student or Course not found"]
        }

        def exists = Enrollment.findByStudentAndCourse(student, course)
        if (exists) {
            return [ok: false, status: 409, msg: "Enrollment already exists"]
        }

        def enrollment = new Enrollment(student: student, course: course)
        if (!enrollment.validate()) {
            return [ok: false, status: 422, errors: enrollment.errors]
        }
        enrollment.save(flush: true)
        return [ok: true, status: 201, data: enrollment]
    }
}
