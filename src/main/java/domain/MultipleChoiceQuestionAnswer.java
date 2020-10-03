package domain;


import datamapper.ChoiceMapper;
import datamapper.MultipleChoiceQuestionAnswerMapper;
import datamapper.MultipleChoiceQuestionMapper;

public class MultipleChoiceQuestionAnswer extends Answer{
    private MultipleChoiceQuestion multipleChoiceQuestion = null;
    private Choice chosenAnswer = null;
    private String chosenAnswerID = null;

    public MultipleChoiceQuestionAnswer(){super();};

    public String getChosenAnswerID() {
        if (chosenAnswerID == null) load();
        return chosenAnswerID;
    }

    public void setChosenAnswerID(String chosenAnswerID) {
        this.chosenAnswerID = chosenAnswerID;
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

    public Choice getChosenAnswer() {
        if (chosenAnswer == null) {
            Choice record = ChoiceMapper.getSingletonInstance().findWithChoiceID(getChosenAnswerID());
            setChosenAnswer(record);
        }
        return chosenAnswer;
    }

    public void setChosenAnswer(Choice chosenAnswer) {
        this.chosenAnswer = chosenAnswer;
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
        if (super.getMark() == Integer.MIN_VALUE) load();
        return super.getMark();
    }

    public void load() {
        MultipleChoiceQuestionAnswer record = MultipleChoiceQuestionAnswerMapper.getSingletonInstance().findWithID(getId());
        if (super.getQuestionID() == null) setQuestionID(record.getQuestionID());
        if (super.getExamAnswerID() == null) setExamAnswerID(record.getExamAnswerID());
        if (super.getMark() == Integer.MIN_VALUE) setMark(record.getMark());
        if (chosenAnswerID == null) chosenAnswerID = record.getChosenAnswerID();
    }
}
