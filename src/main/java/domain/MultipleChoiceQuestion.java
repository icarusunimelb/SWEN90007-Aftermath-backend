package domain;

import datamapper.ChoiceMapper;
import datamapper.MultipleChoiceQuestionMapper;
import exceptions.RecordNotExistException;

import java.util.List;

public class MultipleChoiceQuestion extends Question{
    private List<Choice> multipleChoices = null;
    private String type = "QUESTION_MULTIPLE_CHOICE";

    public String getType() {
        return type;
    }

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
        MultipleChoiceQuestion record = MultipleChoiceQuestionMapper.getSingletonInstance().findWithID(getId());
        if (record.getId() == null) {
            throw new RecordNotExistException(getClass()+" "+ getId() + " has no record in the database");
        }
        if (super.getTotalMark() == -100) setTotalMark(record.getTotalMark());
        if (super.getExamID() == null) setExamID(record.getExamID());
        if (super.getQuestionBody() == null) setQuestionBody(record.getQuestionBody());
        if (super.getTitle() == null) setTitle(record.getTitle());
    }
}
