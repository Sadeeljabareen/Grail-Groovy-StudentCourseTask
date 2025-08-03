package CLI

class Student {

    String name
    String email

    static hasMany = [enrollment: Enrollment]

    static constraints = {
        name blank: false
        email email: true, blank: false ,unique: true
    }

    String toString(){
        name
    }
}
