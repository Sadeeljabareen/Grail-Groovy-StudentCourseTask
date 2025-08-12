package CLI

class Student {
    String name
    String email
    String photoUrl
    User user

    static constraints = {
        name blank: false
        email email: true, blank: false, unique: true
        photoUrl nullable: true
        user nullable: false, unique: true, validator: { user ->
            // التأكد أن المستخدم له دور ROLE_USER فقط
            def roles = user?.authorities*.authority
            roles && roles.contains('ROLE_USER') && !roles.contains('ROLE_ADMIN')
        }
    }


    String toString() {
        name
    }
}