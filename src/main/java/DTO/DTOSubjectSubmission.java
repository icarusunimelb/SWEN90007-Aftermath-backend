package DTO;

import domain.Exam;
import domain.Subject;

import java.util.ArrayList;
import java.util.List;

public class DTOSubjectSubmission {
    public final String dataId;
    public final String subjectCode;
    public final String subjectName;
    public List<DTOExamSubmission> exams = new ArrayList<>();

    public DTOSubjectSubmission(Subject subject) {
        dataId = subject.getId();
        subjectCode = subject.getSubjectCode();
        subjectName = subject.getSubjectName();
        for (Exam exam: subject.getExams()){
            exams.add(new DTOExamSubmission(exam));
        }
    }
}
