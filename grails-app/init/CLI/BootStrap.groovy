package CLI

import grails.plugin.springsecurity.SpringSecurityService

class BootStrap {

    SpringSecurityService springSecurityService

    def init = { servletContext ->
        File uploadDir = new File("${servletContext.getRealPath('/')}uploads")
        if (!uploadDir.exists()) {
            uploadDir.mkdirs()
        }
        User.withTransaction {
            def adminRole = Role.findByAuthority('ROLE_ADMIN') ?: new Role(authority: 'ROLE_ADMIN').save(failOnError: true)
            def userRole = Role.findByAuthority('ROLE_USER') ?: new Role(authority: 'ROLE_USER').save(failOnError: true)

            if (!User.findByUsername('admin')) {
                def adminUser = new User(
                        username: 'admin',
                        password: 'admin123',
                        enabled: true
                ).save(failOnError: true)

                UserRole.create(adminUser, adminRole, true)

                new Student(
                        name: 'Admin User',
                        email: 'admin@gmail.com',
                        user: adminUser
                ).save(failOnError: true)
            }

            if (!User.findByUsername('student1')) {
                def studentUser = new User(
                        username: 'student1',
                        password: 'student123',
                        enabled: true
                ).save(failOnError: true)

                UserRole.create(studentUser, userRole, true)

                new Student(
                        name: 'John Doe',
                        email: 'john@gmail.com',
                        user: studentUser,
                        photoUrl: '/assets/images/student1.png'
                ).save(failOnError: true)
            }
        }

        Course.withTransaction {
            if (Course.count() == 0) {
                new Course(title: 'Mathematics', description: 'Basic Mathematics', credits: 3.0).save(failOnError: true)
                new Course(title: 'Physics', description: 'Basic Physics', credits: 4.0).save(failOnError: true)
                new Course(title: 'Chemistry', description: 'Basic Chemistry', credits: 3.0).save(failOnError: true)
            }
        }
    }

    def destroy = {}
}