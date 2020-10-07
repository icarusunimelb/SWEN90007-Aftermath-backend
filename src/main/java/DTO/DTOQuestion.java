package DTO;

import domain.MultipleChoiceQuestion;
import domain.Question;
import domain.ShortAnswerQuestion;

import java.util.List;
import java.util.stream.Collectors;

public class DTOQuestion {
    public final String dataId;
    public final String type;
    public final String title;
    public final String description;
    public final int marks;
    public final List<String> choices;

    public DTOQuestion(Question question) {
        dataId = question.getId();
        type = question instanceof MultipleChoiceQuestion ?
                "QUESTION_MULTIPLE_CHOICE": question instanceof ShortAnswerQuestion ? "QUESTION_SHORT_ANSWER" : null;
        title = question.getTitle();
        description = question.getQuestionBody();
        marks = question.getTotalMark();
        if (question instanceof MultipleChoiceQuestion) {
            choices = ((MultipleChoiceQuestion) question).getMultipleChoices().stream().map(c -> c.getChoice()).collect(Collectors.toList());
        } else {
            choices = null;
        }
    }
}
