package domain;

import datamapper.*;
import datasource.DBConnection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Exam extends DomainObject{
    private List<Question> questions = null;
    private String subjectID = null;
    private List<ExamAnswer> examAnswers =null;
    private String examName = null;
    private String status = null;

    public Exam(){super();};

    public List<Question> getQuestions() {
        if (questions == null) {
            List<Question> tempQuestions = new ArrayList<>();
            tempQuestions.addAll(MultipleChoiceQuestionMapper.getSingletonInstance().findWithExamID(getId()));
            tempQuestions.addAll(ShortAnswerQuestionMapper.getSingletonInstance().findWithExamID(getId()));
            setQuestions(tempQuestions);
        }
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    public List<ExamAnswer> getExamAnswers() {
        if (examAnswers == null) {
            System.out.println("^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
            setExamAnswers(ExamAnswerMapper.getSingletonInstance().findTableViewExamAnswer(getId()));
        }
        return examAnswers;
    }

    public void setExamAnswers(List<ExamAnswer> examAnswers) {
        this.examAnswers = examAnswers;
    }

    public String getSubjectID() {
        if(subjectID == null) load();
        return subjectID;
    }

    public void setSubjectID(String subjectID) {
        this.subjectID = subjectID;
    }

    public String getExamName() {
        if (examName == null) load();
        return examName;
    }

    public void setExamName(String examName) {
        this.examName = examName;
    }

    public String getStatus() {
        if (status == null) {
            load();
        }
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void load(){
        Exam record = ExamMapper.getSingletonInstance().findWithID(getId());
        if (subjectID == null) subjectID = record.getSubjectID();
        if (examName == null) examName = record.getExamName();
        if (status == null) status = record.getStatus();
    }
}
