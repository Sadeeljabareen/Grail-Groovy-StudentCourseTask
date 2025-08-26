package api

import grails.rest.RestfulController
import CLI.Enrollment
import CLI.Student
import CLI.Course

class EnrollmentController extends RestfulController<Enrollment> {
    static namespace = 'api'
    static responseFormats = ['json']
    static allowedMethods = [index: 'GET', show: 'GET', save: 'POST', update: 'PUT', delete: 'DELETE']

    EnrollmentApiService enrollmentApiService

    EnrollmentController() { super(Enrollment) }

    def index(Integer max, Integer offset) {
        params.max = Math.min(max ?: 20, 100)
        params.offset = offset ?: 0
        respond Enrollment.list(params), [status: 200]
    }

    @Override
    def save() {
        def json = request.JSON
        Long studentId = (json.studentId as Long)
        Long courseId = (json.courseId as Long)
        if (!studentId || !courseId) {
            render(status: 400, contentType: 'application/json') {
                [message: 'Both studentId and courseId are required and must be numbers']
            }
            return
        }

        def result = enrollmentApiService.enroll(studentId, courseId)
        if (!result.ok) {
            if (result.errors) {
                respond([errors: result.errors], [status: (result.status as int)])
            } else {
                respond([message: result.msg], [status: (result.status as int)])
            }
            return
        }
        respond result.data, [status: 201]
    }

    @Override
    def update() {
        Long id = (params.long('id') as Long)
        def enrollment = Enrollment.get(id)
        if (!enrollment) {
            render status: 404; return
        }

        def json = request.JSON
        boolean changed = false

        if (json?.studentId != null) {
            def s = Student.get(json.studentId as Long)
            if (!s) {
                respond([message: 'Student not found'], [status: 404]); return
            }
            enrollment.student = s; changed = true
        }
        if (json?.courseId != null) {
            def c = Course.get(json.courseId as Long)
            if (!c) {
                respond([message: 'Course not found'], [status: 404]); return
            }
            enrollment.course = c; changed = true
        }

        if (changed && !enrollment.validate()) {
            transactionStatus.setRollbackOnly()
            respond([errors: enrollment.errors], [status: 422]); return
        }
        if (changed) enrollment.save(flush: true)
        respond enrollment, [status: 200]
    }

    @Override
    def delete() {
        Long id = (params.long('id') as Long)
        def enrollment = Enrollment.get(id)
        if (!enrollment) {
            render status: 404; return
        }
        enrollment.delete(flush: true)
        render status: 204
    }
}
