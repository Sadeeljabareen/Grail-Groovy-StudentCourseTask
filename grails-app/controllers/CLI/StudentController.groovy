package CLI

import grails.gorm.transactions.Transactional
import grails.rest.Resource
import grails.validation.ValidationException
import groovy.util.logging.Slf4j
import org.springframework.web.multipart.MultipartFile

@Resource()
@Slf4j
class StudentController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    StudentService studentService
    def springSecurityService

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        render(view: "index", model: [
                studentList: studentService.list(params),
                studentCount: studentService.count()
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

        // التحقق من البيانات المطلوبة
        if (!params.username || !params.password || !params.name || !params.email) {
            flash.error = "جميع الحقول مطلوبة"
            render(view: "create", model: [student: new Student(params), user: new User(params)])
            return
        }

        // التحقق من عدم تكرار اسم المستخدم أو البريد الإلكتروني
        if (User.findByUsername(params.username)) {
            flash.error = "اسم المستخدم موجود مسبقاً"
            render(view: "create", model: [student: new Student(params), user: new User(params)])
            return
        }

        if (Student.findByEmail(params.email)) {
            flash.error = "البريد الإلكتروني موجود مسبقاً"
            render(view: "create", model: [student: new Student(params), user: new User(params)])
            return
        }

        def user = new User(
                username: params.username,
                password: params.password
        )

        def student = new Student(
                name: params.name,
                email: params.email,
                user: user
        )

        // Handle photo upload
        MultipartFile photo = request.getFile('photo')
        if (photo && !photo.empty) {
            try {
                if (photo.size > 2000000) { // 2MB
                    flash.error = "حجم الصورة يجب أن يكون أقل من 2MB"
                    render(view: "create", model: [
                            student: student,
                            user: user
                    ])
                    return
                }

                // مسار الحفظ المطلق
                String uploadDir = "C:\\Users\\Dell\\CLI\\webapps\\uploads"
                File uploadFolder = new File(uploadDir)

                // إنشاء المجلد إذا لم يكن موجوداً
                if (!uploadFolder.exists()) {
                    uploadFolder.mkdirs()
                    log.info("تم إنشاء مجلد الرفع في: ${uploadFolder.absolutePath}")
                }

                // إنشاء اسم فريد للملف
                String filename = "${System.currentTimeMillis()}_${photo.originalFilename.replaceAll('[^a-zA-Z0-9.-]', '_')}"
                File destination = new File(uploadFolder, filename)

                // حفظ الملف
                photo.transferTo(destination)

                // تخزين المسار النسبي في قاعدة البيانات (نسبي للرابط)
                student.photoUrl = "/uploads/${filename}"

            } catch (IOException e) {
                log.error("فشل في حفظ الصورة", e)
                flash.error = "فشل في حفظ الصورة. يرجى المحاولة مرة أخرى."
                render(view: "create", model: [
                        student: student,
                        user: user
                ])
                return
            }
        }

        try {
            // حفظ المستخدم أولاً
            user.save(failOnError: true)

            // تعيين دور المستخدم
            def role = Role.findByAuthority('ROLE_USER')
            if (!role) {
                throw new Exception("دور المستخدم غير موجود في قاعدة البيانات")
            }
            UserRole.create(user, role, true)

            // حفظ بيانات الطالب
            student.save(failOnError: true)

            flash.message = "تم إنشاء الطالب بنجاح"
            redirect(action: "show", id: student.id)
        } catch (Exception e) {
            log.error("خطأ في حفظ بيانات الطالب", e)
            render(view: "create", model: [
                    student: student,
                    user: user,
                    error: e.message
            ])
        }
    }

    def checkEmail() {
        def email = params.email
        def exists = email ? Student.findByEmail(email) != null : false
        render(contentType: "application/json") {
            [exists: exists]
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

    def update(Student student) {
        if (!student) {
            notFound()
            return
        }

        // Handle photo upload
        MultipartFile photo = request.getFile('photo')
        if (photo && !photo.empty) {
            try {
                if (photo.size > 2000000) { // 2MB
                    flash.error = "حجم الصورة يجب أن يكون أقل من 2MB"
                    render(view: "edit", model: [student: student])
                    return
                }

                // مسار الحفظ المطلق
                String uploadDir = "C:\\Users\\Dell\\CLI\\webapps\\uploads"
                File uploadFolder = new File(uploadDir)

                if (!uploadFolder.exists()) {
                    uploadFolder.mkdirs()
                    log.info("تم إنشاء مجلد الرفع في: ${uploadFolder.absolutePath}")
                }

                String filename = "${System.currentTimeMillis()}_${photo.originalFilename.replaceAll('[^a-zA-Z0-9.-]', '_')}"
                File destination = new File(uploadFolder, filename)
                photo.transferTo(destination)

                // حذف الصورة القديمة إذا كانت موجودة
                if (student.photoUrl) {
                    String oldFilePath = "${uploadDir}\\${student.photoUrl.replace('/uploads/', '')}"
                    File oldFile = new File(oldFilePath)
                    if (oldFile.exists()) {
                        oldFile.delete()
                    }
                }

                student.photoUrl = "/uploads/${filename}"

            } catch (IOException e) {
                log.error("فشل في تحديث الصورة", e)
                flash.error = "فشل في تحديث الصورة. يرجى المحاولة مرة أخرى."
                render(view: "edit", model: [student: student])
                return
            }
        }

        try {
            studentService.save(student)
            flash.message = "تم تحديث بيانات الطالب بنجاح"
            redirect(action: "show", id: student.id)
        } catch (ValidationException e) {
            render(view: "edit", model: [student: student])
        }
    }


    def delete(Long id) {
        def student = studentService.get(id)
        if (!student) {
            notFound()
            return
        }

        try {
            // حذف الصورة إذا كانت موجودة
            if (student.photoUrl) {
                String uploadDir = "webapps/uploads"
                String filePath = "${uploadDir}/${student.photoUrl.replace('/uploads/', '')}"
                File photoFile = new File(filePath)
                if (photoFile.exists()) {
                    photoFile.delete()
                }
            }

            // حذف حساب المستخدم أولاً
            User user = student.user
            studentService.delete(id)
            user.delete()

            flash.message = "تم حذف الطالب بنجاح."
            redirect(action: "index")
        } catch (Exception e) {
            log.error("خطأ في حذف الطالب", e)
            flash.error = "خطأ في حذف الطالب: ${e.message}"
            redirect(action: "show", id: id)
        }
    }

    protected void notFound() {
        flash.message = "الطالب غير موجود"
        redirect(action: "index")
    }
}