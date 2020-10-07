package DTO;

import domain.Exam;
import domain.Subject;

import java.util.List;

public class DTOSubjectSubmission {
    public final String dataId;
    public final String subjectId;
    public final String subjectName;
    public List<DTOExamSubmission> exams;

    public DTOSubjectSubmission(Subject subject) {
        dataId = subject.getId();
        subjectId = subject.getSubjectCode();
        subjectName = subject.getSubjectName();
        for (Exam exam: subject.getExams()){
            exams.add(new DTOExamSubmission(exam));
        }
    }
}
