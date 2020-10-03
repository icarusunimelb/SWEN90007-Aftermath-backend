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
    public double getTotalMark() {
        if (super.getTotalMark() == Integer.MIN_VALUE) load();
        return super.getTotalMark();
    }

    @Override
    public String getQuestionBody() {
        if (super.getQuestionBody() == null) load();
        return super.getQuestionBody();
    }

    public void load(){
        ShortAnswerQuestion record = ShortAnswerQuestionMapper.getSingletonInstance().findWithID(getId());
        if (super.getTotalMark() == Integer.MIN_VALUE) setTotalMark(record.getTotalMark());
        if (super.getExamID() == null) setExamID(record.getExamID());
        if (super.getQuestionBody() == null) setQuestionBody(record.getQuestionBody());
    }
}
