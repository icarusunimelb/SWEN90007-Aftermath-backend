package DTO;

import domain.Instructor;
import domain.Student;

public class DTOStudent {
    public final String dataId;
    public final String studentName;

    public DTOStudent(Student student) {
        dataId = student.getId();
        studentName = student.getName().toString();
    }

}
