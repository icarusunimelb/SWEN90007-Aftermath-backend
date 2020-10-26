package domain;

import datamapper.MultipleChoiceQuestionAnswerMapper;
import datamapper.ShortAnswerQuestionAnswerMapper;
import datamapper.ShortAnswerQuestionMapper;
import exceptions.RecordNotExistException;

public class ShortAnswerQuestionAnswer extends Answer{
    private String answer = null;
    private ShortAnswerQuestion shortAnswerQuestion = null;

    public ShortAnswerQuestionAnswer(){super();};

    public ShortAnswerQuestion getShortAnswerQuestion() throws RecordNotExistException{
        if (shortAnswerQuestion == null){
            setShortAnswerQuestion(ShortAnswerQuestionMapper.getSingletonInstance().findWithID(getQuestionID()));
        }
        return shortAnswerQuestion;
    }

    public void setShortAnswerQuestion(ShortAnswerQuestion shortAnswerQuestion) {
        this.shortAnswerQuestion = shortAnswerQuestion;
    }

    public String getAnswer() throws RecordNotExistException{
        if (answer == null) load();
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    @Override
    public String getQuestionID() throws RecordNotExistException{
        if (super.getQuestionID() == null) load();
        return super.getQuestionID();
    }

    @Override
    public String getExamAnswerID() throws RecordNotExistException{
        if (super.getExamAnswerID() == null) load();
        return super.getExamAnswerID();
    }

    @Override
    public int getMark() throws RecordNotExistException{
        if (super.getMark() == -100) load();
        return super.getMark();
    }

    public void load() throws RecordNotExistException{
        ShortAnswerQuestionAnswer record = ShortAnswerQuestionAnswerMapper.getSingletonInstance().findWithID(getId());
        if (record.getId() == null) {
            throw new RecordNotExistException(getClass()+" "+ getId() + " has no record in the database");
        }
        if (super.getQuestionID() == null) setQuestionID(record.getQuestionID());
        if (super.getExamAnswerID() == null) setExamAnswerID(record.getExamAnswerID());
        if (super.getMark() == -100) setMark(record.getMark());
        if (answer == null) answer = record.getAnswer();
    }
}
