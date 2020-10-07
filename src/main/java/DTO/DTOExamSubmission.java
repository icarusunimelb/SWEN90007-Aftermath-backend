package DTO;

import domain.Exam;
import domain.ExamAnswer;

import java.util.List;

public class DTOExamSubmission {
    public final String dataId;
    public final String examName;
    public final String status;
    public List<DTOSubmission> submissions;

    public DTOExamSubmission(Exam exam) {
        dataId = exam.getId();
        examName = exam.getExamName();
        status = exam.getStatus();
        for (ExamAnswer examAnswer: exam.getExamAnswers()){
            submissions.add(new DTOSubmission(examAnswer));
        }
    }
}
