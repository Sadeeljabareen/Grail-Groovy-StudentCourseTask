package api

import CLI.Student
import CLI.User
import CLI.Role
import CLI.UserRole

import grails.rest.RestfulController

class StudentController extends RestfulController<Student> {
    static namespace = 'api'
    static allowedMethods = [
            index : 'GET',
            show  : 'GET',
            save  : 'POST',
            update: 'PUT',
            delete: 'DELETE'
    ]

    StudentController() { super(Student) }

    def index(Integer max, Integer offset) {
        params.max = Math.min(max ?: 20, 100)
        params.offset = offset ?: 0
        respond Student.list(params), [status: 200]
    }

    @Override
    def save() {
        if (!(request.contentType?.toLowerCase()?.contains('application/json'))) {
            respond([message: 'Content-Type must be application/json'], [status: 415])
            return
        }

        def json = request.JSON
        if (!json) { respond([message: 'Empty body'], [status: 400]); return }

        if (!json.name || !json.email) {
            respond([message: 'name and email are required'], [status: 422]); return
        }
        if (!json.username || !json.password) {
            respond([message: 'username and password are required'], [status: 422]); return
        }

        if (User.findByUsername(json.username)) {
            respond([message: "username '${json.username}' already exists"], [status: 409]); return
        }

        def toFieldErrors = { errs ->
            if (!errs) return []
            (errs.fieldErrors ?: []).collect { fe ->
                [field: fe.field, rejectedValue: fe.rejectedValue, code: fe.code, message: fe.defaultMessage]
            }
        }

        User.withTransaction { tx ->
            try {
                def user = new User(
                        username: json.username,
                        password: json.password
                )
                if (user.metaClass.hasProperty(user, 'enabled') && user.enabled == null) user.enabled = true
                if (user.metaClass.hasProperty(user, 'accountExpired'))  user.accountExpired  = false
                if (user.metaClass.hasProperty(user, 'accountLocked'))   user.accountLocked   = false
                if (user.metaClass.hasProperty(user, 'passwordExpired')) user.passwordExpired = false

                if (!user.validate()) {
                    tx.setRollbackOnly()
                    respond([fieldErrors: toFieldErrors(user.errors)], [status: 422])
                    return
                }
                user.save(flush: true)

                def roleUser = Role.findByAuthority('ROLE_USER') ?: new Role(authority: 'ROLE_USER').save()
                if (!UserRole.findByUserAndRole(user, roleUser)) {
                    new UserRole(user: user, role: roleUser).save()
                }

                def student = new Student(
                        name    : json.name,
                        email   : json.email,
                        photoUrl: (json.photoUrl ?: null),
                        user    : user
                )

                if (!student.validate()) {
                    tx.setRollbackOnly()
                    respond([fieldErrors: toFieldErrors(student.errors)], [status: 422])
                    return
                }

                student.save(flush: true)

                respond student, [status: 201]

            } catch (Exception e) {
                tx.setRollbackOnly()
                respond([error: e.message], [status: 500])
            }
        }
    }

    @Override
    def update() {
        if (!(request.contentType ?: '').toLowerCase().contains('application/json')) {
            respond([message: 'Content-Type must be application/json'], [status: 415])
            return
        }

        def id = params.long('id')
        def student = Student.get(id)
        if (!student) { render status: 404; return }

        def json
        try {
            json = request.JSON
        } catch (ignored) {
            respond([message: 'Invalid JSON body'], [status: 400])
            return
        }

        Student.withTransaction { status ->
            if (json.containsKey('name')) student.name = json.name
            if (json.containsKey('email')) student.email = json.email
            if (json.containsKey('photoUrl')) student.photoUrl = json.photoUrl
            if (json.containsKey('profilePhotoFilename')) student.profilePhotoFilename = json.profilePhotoFilename

            if (json.user?.id) {
                def newUser = User.get(json.user.id)
                if (!newUser) {
                    respond([message: "User ${json.user.id} not found"], [status: 404])
                    return
                }

                def other = Student.findByUser(newUser)
                if (other && other.id != student.id) {
                    respond([message: "User ${json.user.id} is already linked to another student"], [status: 409])
                    return
                }

                student.user = newUser
            }

            if (!student.validate()) {
                status.setRollbackOnly()
                respond([fieldErrors: student.errors.fieldErrors.collect { fe ->
                    [field: fe.field, rejectedValue: fe.rejectedValue, code: fe.code, message: fe.defaultMessage]
                }], [status: 422])
                return
            }

            student.save()
            respond student, [status: 200]
        }
    }

    @Override
    def delete() {
        def id = params.long('id')
        def student = Student.get(id)
        if (!student) { render status: 404; return }

        Student.withTransaction { student.delete() }
        render status: 204
    }
}
