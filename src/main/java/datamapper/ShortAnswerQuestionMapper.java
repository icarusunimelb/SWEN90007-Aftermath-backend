package datamapper;

import datasource.DBConnection;
import domain.*;
import exceptions.RecordNotExistException;
import utils.IdentityMap;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ShortAnswerQuestionMapper extends DataMapper{

    private static volatile ShortAnswerQuestionMapper instance = null;

    public static ShortAnswerQuestionMapper getSingletonInstance() {
        if (instance == null) {
            synchronized (ShortAnswerQuestionMapper.class) {
                if (instance == null) {
                    instance = new ShortAnswerQuestionMapper();
                }
            }
        }
        return instance;
    }

    private static final String findWithIDStatement = "SELECT s.title, s.examID, s.totalMark, s.questionBody "
            + "FROM oes.shortAnswerQuestions s "
            + "WHERE s.questionID = ?";

    public ShortAnswerQuestion findWithID(String questionID) {
        ShortAnswerQuestion question = new ShortAnswerQuestion();
        try {
            PreparedStatement findStatement = DBConnection.prepare(findWithIDStatement);
            findStatement.setString(1, questionID);
            ResultSet rs = findStatement.executeQuery();
            if (rs.next()) {
                String title = rs.getString(1);
                String examID = rs.getString(2);
                int totalMark = rs.getInt(3);
                String questionBody = rs.getString(4);
                question.setId(questionID);
                question.setTitle(title);
                question.setExamID(examID);
                question.setTotalMark(totalMark);
                question.setQuestionBody(questionBody);

            }
        } catch (SQLException e) {
            System.out.println(this.getClass()+e.getMessage());
        }
        return question;
    }

    private static final String findWithExamIDStatement = "SELECT s.title, s.questionID, s.totalMark, "
            + "s.questionBody "
            + "FROM oes.shortAnswerQuestions s "
            + "WHERE s.examID = ?";

    public List<ShortAnswerQuestion> findWithExamID (String examID) {
        List<ShortAnswerQuestion> questions = new ArrayList<ShortAnswerQuestion>();
        try {
            PreparedStatement findStatement = DBConnection.prepare(findWithExamIDStatement);
            findStatement.setString(1,examID);
            ResultSet rs = findStatement.executeQuery();
            while (rs.next()) {
                ShortAnswerQuestion question = new ShortAnswerQuestion();
                String title = rs.getString(1);
                String questionID = rs.getString(2);
                int totalMark = rs.getInt(3);
                String questionBody = rs.getString(4);
                question.setId(questionID);
                question.setTitle(title);
                question.setExamID(examID);
                question.setQuestionBody(questionBody);
                question.setTotalMark(totalMark);
                questions.add(question);
            }
        } catch (SQLException e) {
            System.out.println(this.getClass()+e.getMessage());
        }

        return questions;
    }

    private static final String insertShortAnswerQuestionStatement =
            "INSERT INTO oes.shortanswerquestions (questionID, examID, totalMark, questionBody, title) VALUES (?, ?, ?, ?, ?)";
    @Override
    public void insert(DomainObject object) throws RecordNotExistException{
        ShortAnswerQuestion shortAnswerQuestionObj = (ShortAnswerQuestion) object;
        try {
            PreparedStatement insertStatement = DBConnection.prepare(insertShortAnswerQuestionStatement);
            insertStatement.setString(1, shortAnswerQuestionObj.getId());
            insertStatement.setString(2, shortAnswerQuestionObj.getExamID());
            insertStatement.setInt(3, shortAnswerQuestionObj.getTotalMark());
            insertStatement.setString(4, shortAnswerQuestionObj.getQuestionBody());
            insertStatement.setString(5, shortAnswerQuestionObj.getTitle());
            insertStatement.execute();
        } catch (SQLException e) {
            System.out.println(this.getClass()+e.getMessage());
        }
    }

    private static final String updateSAQStatement =
            "UPDATE oes.shortAnswerQuestions SET title = ?, questionBody = ?, totalMark = ? " +
                    "WHERE questionID = ?";
    @Override
    public void update(DomainObject object) throws RecordNotExistException {
        ShortAnswerQuestion shortAnswerQuestionObj = (ShortAnswerQuestion) object;
        try {
            PreparedStatement updateStatement = DBConnection.prepare(updateSAQStatement);
            updateStatement.setString(1, shortAnswerQuestionObj.getTitle());
            updateStatement.setString(2, shortAnswerQuestionObj.getQuestionBody());
            updateStatement.setInt(3, shortAnswerQuestionObj.getTotalMark());
            updateStatement.setString(4, shortAnswerQuestionObj.getId());
            updateStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(this.getClass()+e.getMessage());
        }
    }

    private static final String deleteSAQStatement =
            "DELETE FROM oes.shortAnswerQuestions WHERE questionID = ?";
    @Override
    public void delete(DomainObject object) {
        ShortAnswerQuestion shortAnswerQuestionObj = (ShortAnswerQuestion) object;
        try {
            PreparedStatement updateStatement = DBConnection.prepare(deleteSAQStatement);
            updateStatement.setString(1, shortAnswerQuestionObj.getId());
            updateStatement.execute();
        } catch (SQLException e) {
            System.out.println(this.getClass()+e.getMessage());
        }
    }
}
