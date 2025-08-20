package CLI

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

    def logout() {
        redirect uri: '/logout'
    }
}