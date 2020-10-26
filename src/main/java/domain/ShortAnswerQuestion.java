package domain;

import datamapper.MultipleChoiceQuestionMapper;
import datamapper.ShortAnswerQuestionMapper;
import exceptions.RecordNotExistException;

public class ShortAnswerQuestion extends Question{

    public ShortAnswerQuestion(){super();};
    private String type ="QUESTION_SHORT_ANSWER";

    public String getType() {
        return type;
    }

    @Override
    public String getExamID() throws RecordNotExistException{
        if (super.getExamID() == null) load();
        return super.getExamID();
    }

    @Override
    public int getTotalMark() throws RecordNotExistException{
        if (super.getTotalMark() == -100) load();
        return super.getTotalMark();
    }

    @Override
    public String getQuestionBody() throws RecordNotExistException{
        if (super.getQuestionBody() == null) load();
        return super.getQuestionBody();
    }

    @Override
    public String getTitle() throws RecordNotExistException{
        if (super.getTitle() == null) load();
        return super.getTitle();
    }

    public void load() throws RecordNotExistException{
        ShortAnswerQuestion record = ShortAnswerQuestionMapper.getSingletonInstance().findWithID(getId());
        if (record.getId() == null) {
            throw new RecordNotExistException(getClass()+" "+ getId() + " has no record in the database");
        }
        if (super.getTotalMark() == -100) setTotalMark(record.getTotalMark());
        if (super.getExamID() == null) setExamID(record.getExamID());
        if (super.getQuestionBody() == null) setQuestionBody(record.getQuestionBody());
        if (super.getTitle() == null) setTitle(record.getTitle());
    }
}
