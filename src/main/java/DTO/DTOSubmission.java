package DTO;

import com.google.gson.Gson;
import domain.Answer;
import domain.ExamAnswer;
import exceptions.RecordNotExistException;

import java.util.HashMap;

public class DTOSubmission {
    public String dataId;
    public int marks;
    public String studentId;
    public HashMap<String, DTOAnswer> answers = new HashMap<>();

    public DTOSubmission(ExamAnswer examAnswer) {
        try {
            dataId = examAnswer.getId();
            marks = examAnswer.getFinalMark();
            studentId = examAnswer.getStudentID();
            for (Answer ans : examAnswer.getAnswers()) {
                //System.out.println("ans id: "+ans.getQuestionID());
                //System.out.println("DTOAnswer: "+new Gson().toJson(new DTOAnswer(ans)));
                answers.put(ans.getQuestionID(), new DTOAnswer(ans));
            }
        } catch (RecordNotExistException e) {
            e.printStackTrace();
        }
    }
}
