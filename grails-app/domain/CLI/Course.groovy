package CLI

class Course {

    String title
    String description
    Double credits

    static hasMany = [enrollment: Enrollment]

    static constraints = {
        title blank: false ,unique: true
        description blank: false
        credits nullable: false, min: 1d
    }

    String toString(){
        title
    }
}
