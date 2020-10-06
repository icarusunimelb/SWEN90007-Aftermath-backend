package domain;

import datamapper.MultipleChoiceQuestionMapper;
import datamapper.ShortAnswerQuestionMapper;

public class ShortAnswerQuestion extends Question{

    public ShortAnswerQuestion(){super();};

    @Override
    public String getExamID() {
        if (super.getExamID() == null) load();
        return super.getExamID();
    }

    @Override
    public int getTotalMark() {
        if (super.getTotalMark() == -100) load();
        return super.getTotalMark();
    }

    @Override
    public String getQuestionBody() {
        if (super.getQuestionBody() == null) load();
        return super.getQuestionBody();
    }

    @Override
    public String getTitle() {
        if (super.getTitle() == null) load();
        return super.getTitle();
    }

    public void load(){
        ShortAnswerQuestion record = ShortAnswerQuestionMapper.getSingletonInstance().findWithID(getId());
        if (super.getTotalMark() == -100) setTotalMark(record.getTotalMark());
        if (super.getExamID() == null) setExamID(record.getExamID());
        if (super.getQuestionBody() == null) setQuestionBody(record.getQuestionBody());
        if (super.getTitle() == null) setTitle(record.getTitle());
    }
}
