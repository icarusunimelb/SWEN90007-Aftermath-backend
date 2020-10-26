package DTO;

import domain.Exam;
import domain.ExamAnswer;
import exceptions.RecordNotExistException;

import java.util.ArrayList;
import java.util.List;

public class DTOExamSubmission {
    public String dataId;
    public String examName;
    public String status;
    public List<DTOSubmission> submissions = new ArrayList<>();

    public DTOExamSubmission(Exam exam) {
        try {
            dataId = exam.getId();
                examName = exam.getExamName();
            status = exam.getStatus();
            for (ExamAnswer examAnswer: exam.getExamAnswers()){
                submissions.add(new DTOSubmission(examAnswer));
            }
        } catch (RecordNotExistException e) {
            e.printStackTrace();
        }
    }
}
