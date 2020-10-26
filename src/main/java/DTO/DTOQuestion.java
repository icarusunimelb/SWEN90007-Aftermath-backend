package DTO;

import domain.MultipleChoiceQuestion;
import domain.Question;
import domain.ShortAnswerQuestion;
import exceptions.RecordNotExistException;

import java.util.List;
import java.util.stream.Collectors;

public class DTOQuestion {
    public String dataId;
    public String type;
    public String title;
    public String description;
    public int marks;
    public List<String> choices;

    public DTOQuestion(Question question) {
        try {
            dataId = question.getId();
            type = question instanceof MultipleChoiceQuestion ?
                    "QUESTION_MULTIPLE_CHOICE" : question instanceof ShortAnswerQuestion ? "QUESTION_SHORT_ANSWER" : null;
            title = question.getTitle();
            description = question.getQuestionBody();
            marks = question.getTotalMark();
            if (question instanceof MultipleChoiceQuestion) {
                choices = ((MultipleChoiceQuestion) question).getMultipleChoices().stream().map(c -> {
                    try {
                        return c.getChoice();
                    } catch (RecordNotExistException e) {
                        e.printStackTrace();
                        return null;
                    }
                }).collect(Collectors.toList());
            } else {
                choices = null;
            }
        } catch (RecordNotExistException e) {
            e.printStackTrace();
        }
    }
}
