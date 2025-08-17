package CLI

import grails.plugin.springsecurity.annotation.Secured
import grails.plugin.springsecurity.SpringSecurityService
import grails.rest.Resource

@Resource()

class AuthController {

    SpringSecurityService springSecurityService

    def login() {
        if (springSecurityService.isLoggedIn()) {
            redirect uri: springSecurityService.securityConfig.successHandler.defaultTargetUrl
            return
        }
        render view: 'login'
    }

    def register() {
        render view: 'register'
    }

    def saveUser() {
        if (User.findByUsername(params.username)) {
            flash.error = "user name exist"
            render view: 'register', model: [user: params]
            return
        }

        // create user
        def user = new User(
                username: params.username,
                password: params.password,
                enabled: true
        ).save(flush: true, failOnError: true)

        // user role
        def userRole = Role.findByAuthority("ROLE_USER")
        UserRole.create(user, userRole, true)

        def student = new Student(
                name: params.name ?: params.username,
                email: params.email,
                user: user
        ).save(flush: true, failOnError: true)

        springSecurityService.reauthenticate(user.username)
        redirect controller: 'student', action: 'show', id: student.id
    }

    def logout() {
        redirect uri: '/logout'
    }
}