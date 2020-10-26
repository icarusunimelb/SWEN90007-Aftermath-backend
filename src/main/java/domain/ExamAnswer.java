package domain;

import com.google.gson.Gson;
import datamapper.ChoiceMapper;
import datamapper.ExamAnswerMapper;
import datamapper.MultipleChoiceQuestionAnswerMapper;
import datamapper.ShortAnswerQuestionAnswerMapper;
import exceptions.RecordNotExistException;

import java.util.ArrayList;
import java.util.List;

public class ExamAnswer extends DomainObject{
    private String examID = null;
    private String studentID = null;
    private List<Answer> answers = null;
    private int finalMark = -100;

    public ExamAnswer(){super();}

    public int getFinalMark() throws RecordNotExistException{
        if (finalMark == -100 ) load();
        return finalMark;
    }

    public void setFinalMark(int finalMark) {
        this.finalMark = finalMark;
    }

    public String getExamID() throws RecordNotExistException{
        if(examID == null) load();
        return examID;
    }

    public void setExamID(String examID) {
        this.examID = examID;
    }

    public String getStudentID() throws RecordNotExistException{
        if(studentID == null) load();
        return studentID;
    }

    public void setStudentID(String studentID) {
        this.studentID = studentID;
    }

    public List<Answer> getAnswers() {
        if (answers == null) {
            List<Answer> tempAnswers = new ArrayList<>();
            tempAnswers.addAll(MultipleChoiceQuestionAnswerMapper.getSingletonInstance().findAllAnswers(getId()));
            tempAnswers.addAll(ShortAnswerQuestionAnswerMapper.getSingletonInstance().findAllAnswers(getId()));
            //System.out.println("EXAM-ANSWER-ITEMS: "+new Gson().toJson(tempAnswers));
            setAnswers(tempAnswers);
        }
        return answers;
    }

    public void setAnswers(List<Answer> answers) {
        this.answers = answers;
    }

    public void load() throws RecordNotExistException{
        ExamAnswer record = ExamAnswerMapper.getSingletonInstance().findWithID(getId());
        if (record.getId() == null) {
            throw new RecordNotExistException(getClass()+" "+ getId() + " has no record in the database");
        }
        if (examID == null) this.examID = record.getExamID();
        if (studentID == null) this.studentID = record.getStudentID();
        if (finalMark == -100 ) this.finalMark = record.getFinalMark();
    }
}
