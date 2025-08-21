package CLI

import grails.gorm.transactions.Transactional
import grails.validation.ValidationException
import groovy.util.logging.Slf4j
import org.springframework.web.multipart.MultipartFile
import grails.core.GrailsApplication

@Slf4j
class StudentController {

    static allowedMethods = [
            save: "POST",
            update: ["PUT", "POST"],
            delete: "DELETE"
    ]
    StudentService studentService
    GrailsApplication grailsApplication

    def serveImage() {
        String filename = params.filename
        if (!filename) {
            response.sendError(404)
            return
        }

        String uploadDir = grailsApplication.config.getProperty('grails.upload.directory', String)
        File imageFile = new File(uploadDir, filename)

        if (!imageFile.exists()) {
            response.sendError(404)
            return
        }

        response.contentType = URLConnection.guessContentTypeFromName(imageFile.name)
        response.outputStream << imageFile.newInputStream()
        response.outputStream.flush()
    }

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)

        def criteria = Student.createCriteria()
        def studentList = criteria.list(params) {
            if (params.name) {
                ilike('name', "%${params.name}%")
            }
            if (params.email) {
                ilike('email', "%${params.email}%")
            }
            if (params.username) {
                user {
                    ilike('username', "%${params.username}%")
                }
            }
        }

        render(view: "index", model: [
                studentList: studentList,
                studentCount: studentList.totalCount
        ])
    }

    def create() {
        render(view: "create", model: [
                student: new Student(),
                user: new User()
        ])
    }

    @Transactional
    def save() {
        if (!params.username || !params.password || !params.name || !params.email) {
            flash.error = "All fields are required"
            render(view: "create", model: [student: new Student(params), user: new User(params)])
            return
        }

        // Validate email format early
        if (!params.email?.endsWith("@gmail.com")) {
            flash.error = "Email must be a Gmail address"
            render(view: "create", model: [student: new Student(params), user: new User(params)])
            return
        }

        // Check for existing username
        if (User.findByUsername(params.username)) {
            flash.error = "Username already exists"
            render(view: "create", model: [student: new Student(params), user: new User(params)])
            return
        }

        // Check for existing email
        if (Student.findByEmail(params.email)) {
            flash.error = "Email already exists"
            render(view: "create", model: [student: new Student(params), user: new User(params)])
            return
        }

        try {
            def user = new User(
                    username: params.username,
                    password: params.password
            ).save(failOnError: true)

            // Assign role to user
            def role = Role.findByAuthority('ROLE_USER') ?: new Role(authority: 'ROLE_USER').save(failOnError: true)
            UserRole.create(user, role, true)

            // Now create Student with the persisted User
            def student = new Student(
                    name: params.name,
                    email: params.email,
                    user: user
            )

            MultipartFile photo = request.getFile('photo')
            if (photo && !photo.empty) {
                if (photo.size > 2000000) {
                    throw new ValidationException("Image size must be less than 2MB", student.errors)
                }

                String uploadDir = grailsApplication.config.getProperty('grails.upload.directory', String)
                File uploadFolder = new File(uploadDir)

                if (!uploadFolder.exists()) {
                    uploadFolder.mkdirs()
                }

                String filename = "${System.currentTimeMillis()}_${photo.originalFilename.replaceAll('[^a-zA-Z0-9.-]', '_')}"
                File destination = new File(uploadFolder, filename)
                photo.transferTo(destination)

                student.photoUrl = filename
            }

            student.save(failOnError: true)

            flash.message = "Student created successfully"
            redirect(action: "show", id: student.id)
        } catch (ValidationException ve) {
            log.error("Validation failed", ve)
            def student = ve.target instanceof Student ? ve.target : new Student(params)
            def user = ve.target instanceof User ? ve.target : new User(params)
            render(view: "create", model: [
                    student: student,
                    user: user,
                    error: ve.message
            ])
        } catch (Exception e) {
            log.error("Error saving student data", e)
            render(view: "create", model: [
                    student: new Student(params),
                    user: new User(params),
                    error: e.message
            ])
        }
    }

    def checkUsername() {
        def username = params.username
        def exists = username ? User.findByUsername(username) != null : false
        render(contentType: "application/json") {
            [exists: exists]
        }
    }

    def show(Long id) {
        def student = studentService.get(id)
        if (!student) {
            notFound()
            return
        }

        def enrollments = Enrollment.findAllByStudent(student)
        render(view: "show", model: [student: student, enrollments: enrollments])
    }

    def edit(Long id) {
        def student = studentService.get(id)
        if (!student) {
            notFound()
            return
        }
        render(view: "edit", model: [student: student])
    }

    @Transactional
    def update(Student student) {
        if (!student) {
            notFound()
            return
        }

        if (student.hasErrors()) {
            respond student.errors, view:'edit'
            return
        }

        MultipartFile photo = request.getFile('photo')
        if (photo && !photo.empty) {
            try {
                if (photo.size > 2000000) {
                    flash.error = "Image size must be less than 2MB"
                    render(view: "edit", model: [student: student])
                    return
                }

                String uploadDir = grailsApplication.config.getProperty('grails.upload.directory', String)
                File uploadFolder = new File(uploadDir)

                if (!uploadFolder.exists()) {
                    uploadFolder.mkdirs()
                }

                String filename = "${System.currentTimeMillis()}_${photo.originalFilename.replaceAll('[^a-zA-Z0-9.-]', '_')}"
                File destination = new File(uploadFolder, filename)
                photo.transferTo(destination)

                if (student.photoUrl) {
                    File oldFile = new File(uploadDir, student.photoUrl)
                    if (oldFile.exists()) {
                        oldFile.delete()
                    }
                }

                student.photoUrl = filename

            } catch (IOException e) {
                log.error("Failed to update image", e)
                flash.error = "Failed to update image. Please try again."
                render(view: "edit", model: [student: student])
                return
            }
        }

        try {
            studentService.save(student)
            flash.message = "Student updated successfully"
            redirect(action: "show", id: student.id)
        } catch (ValidationException e) {
            render(view: "edit", model: [student: student])
        }
    }

    @Transactional
    def delete(Long id) {
        def student = studentService.get(id)
        if (!student) {
            notFound()
            return
        }

        try {
            if (student.photoUrl) {
                String uploadDir = grailsApplication.config.getProperty('grails.upload.directory', String)
                File photoFile = new File(uploadDir, student.photoUrl)
                if (photoFile.exists()) {
                    photoFile.delete()
                }
            }

            User user = student.user

            studentService.delete(id)

            UserRole.removeAll(user)
            user.delete(flush: true)

            flash.message = "Student and associated user deleted successfully"
            redirect(action: "index")
        } catch (Exception e) {
            log.error("Error deleting student", e)
            flash.error = "Error deleting student: ${e.message}"
            redirect(action: "show", id: id)
        }
    }

    protected void notFound() {
        flash.message = "Student not found"
        redirect(action: "index")
    }
}