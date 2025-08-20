package api

import CLI.Student
import CLI.User
import CLI.Role
import CLI.UserRole

import grails.rest.RestfulController

class StudentController extends RestfulController<Student> {
    static namespace = 'api'
    static responseFormats = ['json']
    static allowedMethods = [
            index: 'GET', show: 'GET',
            save: 'POST', update: 'PUT', delete: 'DELETE'
    ]

    StudentController() { super(Student) }

    def index(Integer max, Integer offset) {
        params.max = Math.min(max ?: 20, 100)
        params.offset = offset ?: 0
        respond Student.list(params), [status: 200]
    }

    // ===== Helper: استخراج userId بأمان من JSON =====
    private Long extractUserId(def json) {
        def v = null
        if (json?.user instanceof Map && json.user.containsKey('id')) {
            v = json.user.id
        } else if (json?.containsKey('userId')) {
            v = json.userId
        }
        if (v == null) return null
        if (v instanceof Number) return ((Number)v).longValue()
        def s = v.toString().trim()
        return (s.isNumber()) ? s.toLong() : null
    }
    // داخل StudentController
    @Override
    def save() {
        // 1) تأكيد نوع المحتوى
        if (!(request.contentType?.toLowerCase()?.contains('application/json'))) {
            respond([message: 'Content-Type يجب أن يكون application/json'], [status: 415]); return
        }

        // 2) قراءة الـ JSON
        def json = request.JSON
        if (!json) { respond([message: 'Body فارغ'], [status: 400]); return }

        // 3) تحقق حقول لازمة
        if (!json.name || !json.email) {
            respond([message: 'name و email مطلوبان'], [status: 422]); return
        }
        if (!json.username || !json.password) {
            respond([message: 'username و password مطلوبان'], [status: 422]); return
        }

        // 4) منع التكرار قبل الحفظ
        if (User.findByUsername(json.username)) {
            respond([message: "username '${json.username}' مستخدم بالفعل"], [status: 409]); return
        }

        // دالة صغيرة لتحويل أخطاء الفاليديشن لصيغة واضحة
        def toFieldErrors = { errs ->
            if (!errs) return []
            (errs.fieldErrors ?: []).collect { fe ->
                [field: fe.field, rejectedValue: fe.rejectedValue, code: fe.code, message: fe.defaultMessage]
            }
        }

        // 5) الترانزاكشن: أنشئ User -> أضف ROLE_USER -> أنشئ Student
        User.withTransaction { tx ->
            try {
                // إنشاء المستخدم + قيم افتراضية شائعة إن كانت موجودة في الدومين
                def user = new User(
                        username: json.username,
                        password: json.password // يُشفَّر عبر الـ Listener إن كان مفعّل
                )
                if (user.metaClass.hasProperty(user, 'enabled') && user.enabled == null) user.enabled = true
                if (user.metaClass.hasProperty(user, 'accountExpired'))  user.accountExpired  = false
                if (user.metaClass.hasProperty(user, 'accountLocked'))   user.accountLocked   = false
                if (user.metaClass.hasProperty(user, 'passwordExpired')) user.passwordExpired = false

                if (!user.validate()) {
                    tx.setRollbackOnly()
                    respond([message: 'Validation failed (user)', fieldErrors: toFieldErrors(user.errors)], [status: 422]); return
                }
                user.save(flush: true)

                // إضافة ROLE_USER (مطلوبة ليمرّ قيد Student.user validator)
                def roleUser = Role.findByAuthority('ROLE_USER') ?: new Role(authority: 'ROLE_USER').save()
                if (!UserRole.findByUserAndRole(user, roleUser)) {
                    new UserRole(user: user, role: roleUser).save()
                }

                // إنشاء الطالب (تذكير: email لازم ينتهي بـ @gmail.com حسب القيود)
                def student = new Student(
                        name    : json.name,
                        email   : json.email,
                        photoUrl: (json.photoUrl ?: null),
                        user    : user
                )

                if (!student.validate()) {
                    tx.setRollbackOnly()
                    respond([message: 'Validation failed (student)', fieldErrors: toFieldErrors(student.errors)], [status: 422]); return
                }

                student.save(flush: true)

                // (اختياري) Location header
                response.setHeader('Location', g.createLink(controller:'student', namespace:'api', action:'show', id:student.id, absolute:true))

                respond student, [status: 201]

            } catch (Exception e) {
                tx.setRollbackOnly()
                respond([message: 'حدث خطأ غير متوقع', error: e.message], [status: 500])
            }
        }
    }



    @Override
    def update() {
        if (!(request.contentType ?: '').toLowerCase().contains('application/json')) {
            respond([message: 'Content-Type لازم يكون application/json'], [status: 415])
            return
        }

        def id = params.long('id')
        def student = Student.get(id)
        if (!student) {
            render status: 404; return
        }

        def json
        try {
            json = request.JSON
        } catch (ignored) {
            respond([message: 'Body ليس JSON صالحاً'], [status: 400])
            return
        }

        Student.withTransaction { status ->
            if (json.containsKey('name')) student.name = json.name
            if (json.containsKey('email')) student.email = json.email
            if (json.containsKey('photoUrl')) student.photoUrl = json.photoUrl
            if (json.containsKey('profilePhotoFilename')) student.profilePhotoFilename = json.profilePhotoFilename

            // ✅ التعامل مع user
            if (json.user?.id) {
                def newUser = User.get(json.user.id)
                if (!newUser) {
                    respond([message: "User ${json.user.id} غير موجود"], [status: 404]); return
                }

                def other = Student.findByUser(newUser)
                if (other && other.id != student.id) {
                    respond([message: "User ${json.user.id} مربوط لطالب آخر id=${other.id}"], [status: 409])
                    return
                }

                student.user = newUser
            }

            if (!student.validate()) {
                status.setRollbackOnly()
                respond([
                        message    : 'Validation failed',
                        fieldErrors: student.errors.fieldErrors.collect { fe ->
                            [field: fe.field, rejectedValue: fe.rejectedValue, code: fe.code, message: fe.defaultMessage]
                        }
                ], [status: 422])
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