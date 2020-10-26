package domain;


import datamapper.ChoiceMapper;
import datamapper.MultipleChoiceQuestionAnswerMapper;
import datamapper.MultipleChoiceQuestionMapper;
import exceptions.RecordNotExistException;

public class MultipleChoiceQuestionAnswer extends Answer{
    private MultipleChoiceQuestion multipleChoiceQuestion = null;



    private int answerIndex = -1;

    public MultipleChoiceQuestionAnswer(){super();};

    public int getAnswerIndex() throws RecordNotExistException{
        if (this.answerIndex == -1) load();
        return answerIndex;
    }

    public void setAnswerIndex(int answerIndex) {
        this.answerIndex = answerIndex;
    }

    public MultipleChoiceQuestion getMultipleChoiceQuestion() throws RecordNotExistException{
        if (multipleChoiceQuestion == null){
            MultipleChoiceQuestion record = MultipleChoiceQuestionMapper.getSingletonInstance().findWithID(getQuestionID());
            setMultipleChoiceQuestion(record);
        }
        return multipleChoiceQuestion;
    }

    public void setMultipleChoiceQuestion(MultipleChoiceQuestion multipleChoiceQuestion) {
        this.multipleChoiceQuestion = multipleChoiceQuestion;
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
        MultipleChoiceQuestionAnswer record = MultipleChoiceQuestionAnswerMapper.getSingletonInstance().findWithID(getId());
        if (record.getId() == null) {
            throw new RecordNotExistException(getClass()+" "+ getId() + " has no record in the database");
        }
        if (super.getQuestionID() == null) setQuestionID(record.getQuestionID());
        if (super.getExamAnswerID() == null) setExamAnswerID(record.getExamAnswerID());
        if (super.getMark() == -100) setMark(record.getMark());
        if (this.answerIndex == -1) setMark(record.getAnswerIndex());
    }
}
