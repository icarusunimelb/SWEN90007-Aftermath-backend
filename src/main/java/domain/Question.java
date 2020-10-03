package domain;

public class Question extends DomainObject{
    private double totalMark = Integer.MIN_VALUE;
    private String questionBody = null;
    private String examID = null;

    public Question(){super();};

    public String getExamID() {
        return examID;
    }

    public void setExamID(String examID) {
        this.examID = examID;
    }

    public double getTotalMark() {
        return totalMark;
    }

    public void setTotalMark(double totalMark) {
        this.totalMark = totalMark;
    }

    public String getQuestionBody() {
        return questionBody;
    }

    public void setQuestionBody(String questionBody) {
        this.questionBody = questionBody;
    }
}
