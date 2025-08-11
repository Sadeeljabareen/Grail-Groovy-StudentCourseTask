package CLI

import grails.rest.Resource
import grails.validation.ValidationException
import groovy.util.logging.Slf4j

import static org.springframework.http.HttpStatus.*

@Resource()
@Slf4j
class CourseController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    CourseService courseService

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        render(view: "index", model: [
                courseList: courseService.list(params),
                courseCount: courseService.count()
        ])
    }

    def show(Long id) {
        def course = courseService.get(id)
        if (!course) {
            notFound()
            return
        }
        render(view: "show", model: [course: course])
    }

    def create() {
        render(view: "create", model: [course: new Course(params)])
    }

    def save(Course course) {
        if (!course) {
            notFound()
            return
        }

        try {
            courseService.save(course)
        } catch (ValidationException e) {
            render(view: "create", model: [course: course])
            return
        }

        flash.message = message(code: 'default.created.message', args: [
                message(code: 'course.label', default: 'Course'),
                course.id
        ])
        redirect(action: "show", id: course.id)
    }

    def edit(Long id) {
        def course = courseService.get(id)
        if (!course) {
            notFound()
            return
        }
        render(view: "edit", model: [course: course])
    }

    def update(Course course) {
        if (!course) {
            notFound()
            return
        }

        try {
            courseService.save(course)
        } catch (ValidationException e) {
            render(view: "edit", model: [course: course])
            return
        }

        flash.message = message(code: 'default.updated.message', args: [
                message(code: 'course.label', default: 'Course'),
                course.id
        ])
        redirect(action: "show", id: course.id)
    }

    def delete(Long id) {
        def course = courseService.get(id)
        if (!course) {
            notFound()
            return
        }

        courseService.delete(id)

        flash.message = message(code: 'default.deleted.message', args: [
                message(code: 'course.label', default: 'Course'),
                id
        ])
        redirect(action: "index")
    }

    protected void notFound() {
        flash.message = message(code: 'default.not.found.message', args: [
                message(code: 'course.label', default: 'Course'),
                params.id
        ])
        redirect(action: "index")
    }
}
