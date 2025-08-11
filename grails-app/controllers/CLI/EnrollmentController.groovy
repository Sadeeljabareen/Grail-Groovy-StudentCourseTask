package CLI

import grails.gorm.transactions.Transactional
import grails.rest.Resource
import static org.springframework.http.HttpStatus.*
import groovy.util.logging.Slf4j

@Resource()
@Slf4j
class EnrollmentController {

    EnrollmentService enrollmentService

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        def criteria = Enrollment.createCriteria()
        def enrollments = criteria.list(params) {
            if (params.studentId) {
                eq('student.id', params.long('studentId'))
            }
            if (params.courseId) {
                eq('course.id', params.long('courseId'))
            }
        }
        render(view: "index", model: [enrollmentList: enrollments, enrollmentCount: enrollments.totalCount])
    }

    def show(Long id) {
        def enrollment = enrollmentService.get(id)
        if (!enrollment) {
            notFound()
            return
        }
        render(view: "show", model: [enrollment: enrollment])
    }

    @Transactional
    def create() {
        render(view: "create", model: [
                enrollment: new Enrollment(params),
                students: Student.list(),
                courses: Course.list()
        ])
    }

    @Transactional
    def save(Enrollment enrollment) {
        if (enrollment == null) {
            notFound()
            return
        }

        if (!enrollment.student || !enrollment.course) {
            flash.message = "يجب تحديد الطالب والمادة"
            render(view: 'create', model: [
                    enrollment: enrollment,
                    students: Student.list(),
                    courses: Course.list()
            ])
            return
        }

        try {
            enrollment = enrollmentService.save(enrollment)
            flash.message = "تم تسجيل الطالب في المادة بنجاح"
            redirect(action: "show", id: enrollment.id)
        } catch (RuntimeException e) {
            flash.message = e.message
            render(view: 'create', model: [
                    enrollment: enrollment,
                    students: Student.list(),
                    courses: Course.list()
            ])
        }
    }

    def edit(Long id) {
        def enrollment = enrollmentService.get(id)
        if (!enrollment) {
            notFound()
            return
        }
        render(view: "edit", model: [enrollment: enrollment])
    }

    def update(Enrollment enrollment) {
        if (enrollment == null) {
            notFound()
            return
        }

        try {
            enrollmentService.save(enrollment)
        } catch (RuntimeException e) {
            flash.message = e.message
            render(view: 'edit', model: [enrollment: enrollment])
            return
        }

        flash.message = message(code: 'default.updated.message', args: [
                message(code: 'enrollment.label', default: 'Enrollment'),
                enrollment.id
        ])
        redirect(action: "show", id: enrollment.id)
    }

    def delete(Long id) {
        if (id == null) {
            notFound()
            return
        }

        enrollmentService.delete(id)

        flash.message = message(code: 'default.deleted.message', args: [
                message(code: 'enrollment.label', default: 'Enrollment'),
                id
        ])
        redirect(action: "index")
    }

    def gpa() {
        Long studentId = params.long("studentId")
        if (!studentId) {
            render status: BAD_REQUEST, text: "Missing studentId"
            return
        }

        log.info("📊 [gpa] Starting GPA calculation for studentId: $studentId")
        def gpa = enrollmentService.calculateGPA(studentId)
        log.info("🎓 [gpa] GPA for student $studentId is: $gpa")

        render(view: "gpa", model: [studentId: studentId, gpa: gpa])
    }

    protected void notFound() {
        flash.message = message(code: 'default.not.found.message', args: [
                message(code: 'enrollment.label', default: 'Enrollment'),
                params.id
        ])
        redirect(action: "index")
    }
}
