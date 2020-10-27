package DTO;

import java.util.List;

public class DTOUsers {
    public List<DTOInstructor> lecturers = null;
    public List<DTOStudent> students = null;

    public DTOUsers(List<DTOInstructor> dtoInstructors, List<DTOStudent> dtoStudents){
        lecturers = dtoInstructors;
        students = dtoStudents;
    }
}
