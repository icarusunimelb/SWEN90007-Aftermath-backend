package DTO;

import datamapper.ExamMapper;
import datamapper.InstructorMapper;
import datamapper.StudentMapper;
import domain.Exam;
import domain.Instructor;
import domain.Student;
import domain.Subject;

import java.util.ArrayList;
import java.util.List;

public class DTOSubject {
    public final String dataId;
    public final String subjectId;
    public final String subjectName;
    public final List<DTOInstructor> lecturers = new ArrayList<>();
    public final List<DTOStudent> students = new ArrayList<>();
    public final List<DTOExam> exams = new ArrayList<>();

    public DTOSubject(Subject subject){
        dataId = subject.getId();
        subjectId = subject.getSubjectCode();
        subjectName = subject.getSubjectName();


        for (Exam exam : ExamMapper.getSingletonInstance().findWithSubjectCode(subject.getId())){
            exams.add(new DTOExam(exam));
        }
        for (Instructor instructor : InstructorMapper.getSingletonInstance().findWithSubjectID(subject.getId())){
            lecturers.add(new DTOInstructor(instructor));
        }
        for (Student student : StudentMapper.getSingletonInstance().findWithSubjectID(subject.getId())){
            students.add(new DTOStudent(student));
        }
    }

}
