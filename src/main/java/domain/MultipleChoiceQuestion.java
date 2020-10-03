package domain;

import datamapper.ChoiceMapper;
import datamapper.MultipleChoiceQuestionMapper;

import java.util.List;

public class MultipleChoiceQuestion extends Question{
    private List<Choice> multipleChoices = null;

    public MultipleChoiceQuestion(){super();};


    public void setMultipleChoices(List<Choice> multipleChoices) {
        this.multipleChoices = multipleChoices;
    }

    public List<Choice> getMultipleChoices() {
        if (multipleChoices == null){
            setMultipleChoices(ChoiceMapper.getSingletonInstance().findWithQuestionID(getId()));
        }
        return multipleChoices;
    }

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
        MultipleChoiceQuestion record = MultipleChoiceQuestionMapper.getSingletonInstance().findWithID(getId());
        if (super.getTotalMark() == Integer.MIN_VALUE) setTotalMark(record.getTotalMark());
        if (super.getExamID() == null) setExamID(record.getExamID());
        if (super.getQuestionBody() == null) setQuestionBody(record.getQuestionBody());
    }
}
