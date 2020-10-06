package domain;

import datamapper.MultipleChoiceQuestionAnswerMapper;
import datamapper.ShortAnswerQuestionAnswerMapper;
import datamapper.ShortAnswerQuestionMapper;

public class ShortAnswerQuestionAnswer extends Answer{
    private String answer = null;
    private ShortAnswerQuestion shortAnswerQuestion = null;

    public ShortAnswerQuestionAnswer(){super();};

    public ShortAnswerQuestion getShortAnswerQuestion() {
        if (shortAnswerQuestion == null){
            setShortAnswerQuestion(ShortAnswerQuestionMapper.getSingletonInstance().findWithID(getQuestionID()));
        }
        return shortAnswerQuestion;
    }

    public void setShortAnswerQuestion(ShortAnswerQuestion shortAnswerQuestion) {
        this.shortAnswerQuestion = shortAnswerQuestion;
    }

    public String getAnswer() {
        if (answer == null) load();
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    @Override
    public String getQuestionID() {
        if (super.getQuestionID() == null) load();
        return super.getQuestionID();
    }

    @Override
    public String getExamAnswerID() {
        if (super.getExamAnswerID() == null) load();
        return super.getExamAnswerID();
    }

    @Override
    public int getMark() {
        if (super.getMark() == -100) load();
        return super.getMark();
    }

    public void load() {
        ShortAnswerQuestionAnswer record = ShortAnswerQuestionAnswerMapper.getSingletonInstance().findWithID(getId());
        if (super.getQuestionID() == null) setQuestionID(record.getQuestionID());
        if (super.getExamAnswerID() == null) setExamAnswerID(record.getExamAnswerID());
        if (super.getMark() == -100) setMark(record.getMark());
        if (answer == null) answer = record.getAnswer();
    }
}
