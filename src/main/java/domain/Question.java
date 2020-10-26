package domain;

import exceptions.RecordNotExistException;

public class Question extends DomainObject{
    private String title = null;
    private int totalMark = -100;
    private String questionBody = null;
    private String examID = null;

    public String getTitle() throws RecordNotExistException {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Question(){super();};

    public String getExamID() throws RecordNotExistException{
        return examID;
    }

    public void setExamID(String examID) {
        this.examID = examID;
    }

    public int getTotalMark() throws RecordNotExistException{
        return totalMark;
    }

    public void setTotalMark(int totalMark) {
        this.totalMark = totalMark;
    }

    public String getQuestionBody() throws RecordNotExistException{
        return questionBody;
    }

    public void setQuestionBody(String questionBody) {
        this.questionBody = questionBody;
    }
}
