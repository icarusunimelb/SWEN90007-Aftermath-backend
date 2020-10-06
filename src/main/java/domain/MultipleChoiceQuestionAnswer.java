package domain;


import datamapper.ChoiceMapper;
import datamapper.MultipleChoiceQuestionAnswerMapper;
import datamapper.MultipleChoiceQuestionMapper;

public class MultipleChoiceQuestionAnswer extends Answer{
    private MultipleChoiceQuestion multipleChoiceQuestion = null;



    private int answerIndex = -1;

    public MultipleChoiceQuestionAnswer(){super();};

    public int getAnswerIndex() {
        return answerIndex;
    }

    public void setAnswerIndex(int answerIndex) {
        this.answerIndex = answerIndex;
    }

    public MultipleChoiceQuestion getMultipleChoiceQuestion() {
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
    public double getMark() {
        if (super.getMark() == 0.01 || super.getMark() == 999.00) load();
        return super.getMark();
    }

    public void load() {
        MultipleChoiceQuestionAnswer record = MultipleChoiceQuestionAnswerMapper.getSingletonInstance().findWithID(getId());
        if (super.getQuestionID() == null) setQuestionID(record.getQuestionID());
        if (super.getExamAnswerID() == null) setExamAnswerID(record.getExamAnswerID());
        if (super.getMark() == 0.01 || super.getMark() == 999.00) setMark(record.getMark());
        if (this.answerIndex == -1)setMark(record.getAnswerIndex());
    }
}
