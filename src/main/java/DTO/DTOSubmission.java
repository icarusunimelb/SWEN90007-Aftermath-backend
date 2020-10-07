package DTO;

import com.google.gson.Gson;
import domain.Answer;
import domain.ExamAnswer;

import java.util.HashMap;

public class DTOSubmission {
    public final String dataId;
    public final int marks;
    public final String studentId;
    public HashMap<String, DTOAnswer> answers = new HashMap<>();

    public DTOSubmission(ExamAnswer examAnswer){
        dataId = examAnswer.getId();
        marks = examAnswer.getFinalMark();
        studentId = examAnswer.getStudentID();
        for (Answer ans: examAnswer.getAnswers()) {
            //System.out.println("ans id: "+ans.getId());
            //System.out.println("DTOAnswer: "+new Gson().toJson(new DTOAnswer(ans)));
            answers.put(ans.getQuestionID(), new DTOAnswer(ans));
        }
    }
}
