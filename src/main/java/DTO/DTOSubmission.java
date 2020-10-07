package DTO;

import domain.Answer;
import domain.ExamAnswer;

import java.util.HashMap;

public class DTOSubmission {
    public final String dataId;
    public final int marks;
    public final String studentId;
    public HashMap<String, DTOAnswer> answers;

    public DTOSubmission(ExamAnswer examAnswer){
        dataId = examAnswer.getId();
        marks = examAnswer.getFinalMark();
        studentId = examAnswer.getStudentID();
        for (Answer ans: examAnswer.getAnswers()) {
            System.out.println("1");
            answers.put(ans.getId(), new DTOAnswer(ans));
        }
    }
}
