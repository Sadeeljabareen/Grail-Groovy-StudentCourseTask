package CLI

class Student {
    String name
    String email
    String photoUrl
    User user

    static belongsTo = [user: User]

    static constraints = {
        name blank: false
        email blank: false, email: true, validator: { val, obj ->
            if (!val?.endsWith("@gmail.com")) {
                return ['student.email.invalidDomain', val]
            }
        }
        photoUrl nullable: true
    }


    String toString() {
        name
    }
}