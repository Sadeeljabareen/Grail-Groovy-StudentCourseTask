package api

import grails.rest.RestfulController
import grails.gorm.transactions.Transactional
import CLI.User
import CLI.Role
import CLI.UserRole

class UserController extends RestfulController<User> {
    static namespace = 'api'
    static responseFormats = ['json']
    static allowedMethods = [
            index: 'GET', show: 'GET',
            save: 'POST'
    ]

    UserController() { super(User) }

    def index(Integer max, Integer offset) {
        params.max = Math.min(max ?: 50, 200)
        params.offset = offset ?: 0
        def users = User.list(params)
        def items = users.collect { u ->
            [
                    id: u.id,
                    username: u.username,
                    enabled: u.enabled,
                    roles: (u.authorities*.authority) as List,
                    attachedStudentId: (CLI.Student.findByUser(u)?.id)
            ]
        }
        respond items, [status: 200]
    }

    @Override
    def show() {
        def u = User.get(params.long('id'))
        if (!u) { render status: 404; return }
        respond([
                id: u.id,
                username: u.username,
                enabled: u.enabled,
                roles: (u.authorities*.authority) as List,
                attachedStudentId: (CLI.Student.findByUser(u)?.id)
        ], [status: 200])
    }

    @Transactional
    @Override
    def save() {
        if (!(request.contentType ?: '').toLowerCase().contains('application/json')) {
            respond([status: 415]); return
        }
        def json = request.JSON
        if (!json?.username || !json?.password) {
            respond([message: 'username and password req'], [status: 422]); return
        }

        def user = new User(
                username: json.username,
                password: json.password,
                enabled : (json.enabled instanceof Boolean ? json.enabled : true)
        )

        if (!user.validate()) {
            respond([message: 'Validation failed', errors: user.errors], [status: 422]); return
        }
        user.save()

        def roleUser = Role.findByAuthority('ROLE_USER') ?: new Role(authority: 'ROLE_USER').save()

        if (!UserRole.findByUserAndRole(user, roleUser)) {
            new UserRole(user: user, role: roleUser).save()
        }

        respond([id: user.id, username: user.username, roles: ['ROLE_USER']], [status: 201])
    }
}
