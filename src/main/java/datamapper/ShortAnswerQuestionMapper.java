package datamapper;

import datasource.DBConnection;
import domain.*;
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

    private static final String findWithIDStatement = "SELECT s.examID, s.totalMark, s.questionBody "
            + "FROM oes.shortAnswerQuestions s "
            + "WHERE s.questionID = ?";

    public ShortAnswerQuestion findWithID(String questionID) {
        ShortAnswerQuestion question = new ShortAnswerQuestion();
        try {
            PreparedStatement findStatement = DBConnection.prepare(findWithIDStatement);
            findStatement.setString(1, questionID);
            ResultSet rs = findStatement.executeQuery();
            if (rs.next()) {
                String examID = rs.getString(1);
                double totalMark = rs.getDouble(2);
                String questionBody = rs.getString(3);
                question.setId(questionID);
                question.setExamID(examID);
                question.setTotalMark(totalMark);
                question.setQuestionBody(questionBody);

                IdentityMap.getInstance(question).put(questionID, question);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return question;
    }

    private static final String findWithExamIDStatement = "SELECT s.questionID, s.totalMark, "
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
                String questionID = rs.getString(1);
                double totalMark = rs.getDouble(2);
                String questionBody = rs.getString(3);
                question.setId(questionID);
                question.setExamID(examID);
                question.setQuestionBody(questionBody);
                question.setTotalMark(totalMark);
                IdentityMap.getInstance(question).put(questionID, question);
                questions.add(question);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return questions;
    }

    private static final String insertShortAnswerQuestionStatement =
            "INSERT INTO oes.shortanswerquestions (questionID, examID, totalMark, questionBody) VALUES (?, ?, ?, ?)";
    @Override
    public void insert(DomainObject object) {
        ShortAnswerQuestion shortAnswerQuestionObj = (ShortAnswerQuestion) object;
        try {
            PreparedStatement insertStatement = DBConnection.prepare(insertShortAnswerQuestionStatement);
            insertStatement.setString(1, shortAnswerQuestionObj.getId());
            insertStatement.setString(2, shortAnswerQuestionObj.getExamID());
            insertStatement.setDouble(3, shortAnswerQuestionObj.getTotalMark());
            insertStatement.setString(4, shortAnswerQuestionObj.getQuestionBody());
            insertStatement.execute();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private static final String updateSAQStatement =
            "UPDATE oes.shortAnswerQuestion s SET s.questionBody = ?, s.totalMark = ? " +
                    "WHERE s.questionID = ?";
    @Override
    public void update(DomainObject object) {
        ShortAnswerQuestion shortAnswerQuestionObj = (ShortAnswerQuestion) object;
        try {
            PreparedStatement updateStatement = DBConnection.prepare(updateSAQStatement);
            updateStatement.setString(1, shortAnswerQuestionObj.getQuestionBody());
            updateStatement.setDouble(2, shortAnswerQuestionObj.getTotalMark());
            updateStatement.setString(3, shortAnswerQuestionObj.getId());
            updateStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private static final String deleteSAQStatement =
            "DELETE FROM oes.shortAnswerQuestion s WHERE s.questionID = ?";
    @Override
    public void delete(DomainObject object) {
        ShortAnswerQuestion shortAnswerQuestionObj = (ShortAnswerQuestion) object;
        try {
            PreparedStatement updateStatement = DBConnection.prepare(deleteSAQStatement);
            updateStatement.setString(1, shortAnswerQuestionObj.getId());
            updateStatement.execute();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
