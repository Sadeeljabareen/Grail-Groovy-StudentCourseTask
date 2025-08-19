package CLI

class Course {

    String title
    String description
    Double credits

    static hasMany = [enrollment: Enrollment]

    static constraints = {
        title blank: false
        description blank: false
        credits validator: { val, obj ->
            if (val > 6) {
                return "course.credits.tooHigh"
            }
            return true
        }
    }

    String toString(){
        title
    }
}
