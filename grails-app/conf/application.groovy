grails.plugin.springsecurity.userLookup.userDomainClassName = 'CLI.User'
grails.plugin.springsecurity.userLookup.authorityJoinClassName = 'CLI.UserRole'
grails.plugin.springsecurity.authority.className = 'CLI.Role'

grails.plugin.springsecurity.password.algorithm = 'bcrypt'
grails.plugin.springsecurity.logout.postOnly = false
grails.upload.directory = "${System.getProperty('user.home')}/Desktop/student_uploads"
grails.upload.path = "/student/serveImage"

grails.plugin.springsecurity.controllerAnnotations.staticRules = [
        [pattern: '/', access: ['permitAll']],
        [pattern: '/error', access: ['permitAll']],
        [pattern: '/index', access: ['permitAll']],
        [pattern: '/index.gsp', access: ['permitAll']],
        [pattern: '/shutdown', access: ['permitAll']],
        [pattern: '/assets/**', access: ['permitAll']],
        [pattern: '/**/js/**', access: ['permitAll']],
        [pattern: '/**/css/**', access: ['permitAll']],
        [pattern: '/**/images/**', access: ['permitAll']],
        [pattern: '/**/favicon.ico', access: ['permitAll']],
        [pattern: '/auth/**', access: ['permitAll']],

        // Admin routes
        [pattern: '/admin/**', access: ['ROLE_ADMIN']],
        [pattern: '/student/create', access: ['permitAll']],
        [pattern: '/student/save', access: ['permitAll']],
        [pattern: '/student/**', access: ['ROLE_ADMIN']],
        [pattern: '/course/**', access: ['ROLE_ADMIN']],
        [pattern: '/enrollment/**', access: ['ROLE_ADMIN']],

        // User routes
        [pattern: '/user/**', access: ['ROLE_USER']],
        [pattern: '/mycourses/**', access: ['ROLE_USER']],

        // Any other route requires authentication
        [pattern: '/**', access: ['IS_AUTHENTICATED_FULLY']],
        [pattern: '/uploads/**', access: ['permitAll']],
        [pattern: '/api/**', access: ['permitAll']]
]

grails.plugin.springsecurity.filterChain.chainMap = [
        [pattern: '/assets/**', filters: 'none'],
        [pattern: '/**/js/**', filters: 'none'],
        [pattern: '/**/css/**', filters: 'none'],
        [pattern: '/**/images/**', filters: 'none'],
        [pattern: '/**/favicon.ico', filters: 'none'],
        [pattern: '/**', filters: 'JOINED_FILTERS']
]