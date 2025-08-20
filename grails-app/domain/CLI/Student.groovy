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
       // user nullable: false, unique: true, validator: { user ->
        //    // check if user has ROLE_USER
        //    def roles = user?.authorities*.authority
        //    roles && roles.contains('ROLE_USER') && !roles.contains('ROLE_ADMIN')
       // }
    }


    String toString() {
        name
    }
}