package DTO;

import domain.Instructor;
import domain.Student;
import exceptions.RecordNotExistException;

public class DTOStudent {
    public final String dataId;
    public final String studentName;

    public DTOStudent(Student student) throws RecordNotExistException {
        dataId = student.getId();
        studentName = student.getName().toString();
    }

}
