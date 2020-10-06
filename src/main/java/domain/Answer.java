package domain;

public class Answer extends DomainObject{
    private int mark = -100;
    private String questionID = null;
    private String examAnswerID = null;

    public Answer() {super();};

    public String getQuestionID() {
        return questionID;
    }

    public void setQuestionID(String questionID) {
        this.questionID = questionID;
    }

    public String getExamAnswerID() {
        return examAnswerID;
    }

    public void setExamAnswerID(String examAnswerID) {
        this.examAnswerID = examAnswerID;
    }

    public int getMark() {
        return mark;
    }

    public void setMark(int mark) {
        this.mark = mark;
    }

}
