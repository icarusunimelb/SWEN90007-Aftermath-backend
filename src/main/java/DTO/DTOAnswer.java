package DTO;

import domain.Answer;
import domain.MultipleChoiceQuestionAnswer;
import domain.ShortAnswerQuestionAnswer;

import java.util.HashMap;

public class DTOAnswer {
    public final String answer;
    public final int mark;

    public DTOAnswer(Answer ans){
        answer = ans instanceof MultipleChoiceQuestionAnswer ? String.valueOf(((MultipleChoiceQuestionAnswer) ans).getAnswerIndex()) :
                ans instanceof ShortAnswerQuestionAnswer ? ((ShortAnswerQuestionAnswer) ans).getAnswer() : null;
        mark = ans.getMark();
    }
}
