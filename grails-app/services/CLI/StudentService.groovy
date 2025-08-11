package CLI

import grails.gorm.services.Service

@Service(Student)
interface StudentService {

    Student save(Student student)

    Student get(Serializable id)

    List<Student> list(Map args)

    Long count()

    void delete(Serializable id)
}