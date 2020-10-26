package DTO;

import domain.Exam;
import domain.Subject;
import exceptions.RecordNotExistException;

import java.util.ArrayList;
import java.util.List;

public class DTOSubjectSubmission {
    public String dataId;
    public String subjectCode;
    public String subjectName;
    public List<DTOExamSubmission> exams = new ArrayList<>();

    public DTOSubjectSubmission(Subject subject) {
        try {
            dataId = subject.getId();
            subjectCode = subject.getSubjectCode();
            subjectName = subject.getSubjectName();
            for (Exam exam : subject.getExams()) {
                exams.add(new DTOExamSubmission(exam));
            }
        } catch (RecordNotExistException e) {
            e.printStackTrace();
        }
    }
}
