package DTO;

import domain.Answer;
import domain.MultipleChoiceQuestionAnswer;
import domain.ShortAnswerQuestionAnswer;
import exceptions.RecordNotExistException;

import java.util.HashMap;

public class DTOAnswer {
    public String answer;
    public int mark;

    public DTOAnswer(Answer ans){
        try {
            answer = ans instanceof MultipleChoiceQuestionAnswer ? String.valueOf(((MultipleChoiceQuestionAnswer) ans).getAnswerIndex()) :
                    ans instanceof ShortAnswerQuestionAnswer ? ((ShortAnswerQuestionAnswer) ans).getAnswer() : null;
            mark = ans.getMark();
        } catch (RecordNotExistException e) {
            e.printStackTrace();
        }
    }
}
