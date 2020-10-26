package domain;

import exceptions.RecordNotExistException;

public class Answer extends DomainObject{
    private int mark = -100;
    private String questionID = null;
    private String examAnswerID = null;

    public Answer() {super();};

    public String getQuestionID() throws RecordNotExistException {
        return questionID;
    }

    public void setQuestionID(String questionID) {
        this.questionID = questionID;
    }

    public String getExamAnswerID() throws RecordNotExistException{
        return examAnswerID;
    }

    public void setExamAnswerID(String examAnswerID) {
        this.examAnswerID = examAnswerID;
    }

    public int getMark() throws RecordNotExistException{
        return mark;
    }

    public void setMark(int mark) {
        this.mark = mark;
    }

}
