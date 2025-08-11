package CLI

import grails.plugin.springsecurity.annotation.Secured
import grails.plugin.springsecurity.SpringSecurityService

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
        // التحقق من وجود المستخدم مسبقًا
        if (User.findByUsername(params.username)) {
            flash.error = "اسم المستخدم موجود مسبقاً"
            render view: 'register', model: [user: params]
            return
        }

        // إنشاء المستخدم
        def user = new User(
                username: params.username,
                password: params.password,
                enabled: true
        ).save(flush: true, failOnError: true)

        // تعيين دور المستخدم
        def userRole = Role.findByAuthority("ROLE_USER")
        UserRole.create(user, userRole, true)

        // إنشاء سجل الطالب
        def student = new Student(
                name: params.name ?: params.username,
                email: params.email,
                user: user
        ).save(flush: true, failOnError: true)

        // تسجيل الدخول تلقائيًا بعد التسجيل
        springSecurityService.reauthenticate(user.username)
        redirect controller: 'student', action: 'show', id: student.id
    }

    def logout() {
        redirect uri: '/logout'
    }
}