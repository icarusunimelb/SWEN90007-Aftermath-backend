package datamapper;

import datasource.DBConnection;
import domain.*;
import exceptions.RecordNotExistException;
import utils.IdentityMap;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MultipleChoiceQuestionMapper extends DataMapper {

    private static volatile MultipleChoiceQuestionMapper instance = null;

    public static MultipleChoiceQuestionMapper getSingletonInstance() {
        if (instance == null) {
            synchronized (MultipleChoiceQuestionMapper.class) {
                if (instance == null) {
                    instance = new MultipleChoiceQuestionMapper();
                }
            }
        }
        return instance;
    }

    private static final String findWithIDStatement = "SELECT m.title, m.examID, m.totalMark, m.questionBody "
            + "FROM oes.multipleChoiceQuestion m "
            + "WHERE m.questionID = ?";

    public MultipleChoiceQuestion findWithID(String questionID) {
        MultipleChoiceQuestion question = new MultipleChoiceQuestion();
        try {
            PreparedStatement findStatement = DBConnection.prepare(findWithIDStatement);
            findStatement.setString(1, questionID);
            ResultSet rs = findStatement.executeQuery();
            if (rs.next()) {
                String title = rs.getString(1);
                String examID = rs.getString(2);
                int totalMark = rs.getInt(3);
                String questionBody = rs.getString(4);
                question.setTitle(title);
                question.setId(questionID);
                question.setExamID(examID);
                question.setTotalMark(totalMark);
                question.setQuestionBody(questionBody);

            }
        } catch (SQLException e) {
            System.out.println(this.getClass()+e.getMessage());
        }
        return question;
    }

    private static final String findWithExamIDStatement = "SELECT m.title, m.questionID, m.totalMark, "
            + "m.questionBody "
            + "FROM oes.multipleChoiceQuestion m "
            + "WHERE m.examID = ?";

    public List<MultipleChoiceQuestion> findWithExamID(String examID) {
        List<MultipleChoiceQuestion> questions = new ArrayList<MultipleChoiceQuestion>();
        try {
            PreparedStatement findStatement = DBConnection.prepare(findWithExamIDStatement);
            findStatement.setString(1, examID);
            ResultSet rs = findStatement.executeQuery();
            while (rs.next()) {
                MultipleChoiceQuestion question = new MultipleChoiceQuestion();
                String title = rs.getString(1);
                String questionID = rs.getString(2);
                int totalMark = rs.getInt(3);
                String questionBody = rs.getString(4);
                question.setId(questionID);
                question.setTitle(title);
                question.setTotalMark(totalMark);
                question.setExamID(examID);
                question.setQuestionBody(questionBody);

                List<Choice> choices = ChoiceMapper.getSingletonInstance().findWithQuestionID(questionID);
                question.setMultipleChoices(choices);
                IdentityMap.getInstance(question).put(questionID, question);
                questions.add(question);
            }
        } catch (SQLException e) {
            System.out.println(this.getClass()+e.getMessage());
        }

        return questions;
    }

    private static final String insertMultipleChoiceQuestionStatement =
            "INSERT INTO oes.multipleChoiceQuestion (questionID, examID, totalMark, questionBody, title) VALUES (?, ?, ?, ?, ?)";
    @Override
    public void insert(DomainObject object) throws RecordNotExistException{
        MultipleChoiceQuestion multipleChoiceQuestionObj = (MultipleChoiceQuestion) object;
        try {
            PreparedStatement insertStatement = DBConnection.prepare(insertMultipleChoiceQuestionStatement);
            insertStatement.setString(1, multipleChoiceQuestionObj.getId());
            insertStatement.setString(2, multipleChoiceQuestionObj.getExamID());
            insertStatement.setInt(3, multipleChoiceQuestionObj.getTotalMark());
            insertStatement.setString(4, multipleChoiceQuestionObj.getQuestionBody());
            insertStatement.setString(5, multipleChoiceQuestionObj.getTitle());
            insertStatement.execute();
        } catch (SQLException e) {
            System.out.println(this.getClass()+e.getMessage());
        }
    }

    private static final String updateMCQStatement =
            "UPDATE oes.multipleChoiceQuestion SET title = ?, questionBody = ?, totalMark = ? " +
                    "WHERE questionID = ?";
    @Override
    public void update(DomainObject object) throws RecordNotExistException {
        MultipleChoiceQuestion multipleChoiceQuestionObj = (MultipleChoiceQuestion) object;
        try {
            PreparedStatement updateStatement = DBConnection.prepare(updateMCQStatement);
            updateStatement.setString(1, multipleChoiceQuestionObj.getTitle());
            updateStatement.setString(2, multipleChoiceQuestionObj.getQuestionBody());
            updateStatement.setInt(3, multipleChoiceQuestionObj.getTotalMark());
            updateStatement.setString(4, multipleChoiceQuestionObj.getId());
            updateStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(this.getClass()+e.getMessage());
        }
    }

    private static final String deleteMCQStatement =
            "DELETE FROM oes.multipleChoiceQuestion WHERE questionID = ?";
    @Override
    public void delete(DomainObject object) {
        MultipleChoiceQuestion multipleChoiceQuestionObj = (MultipleChoiceQuestion) object;
        try {
            PreparedStatement updateStatement = DBConnection.prepare(deleteMCQStatement);
            updateStatement.setString(1, multipleChoiceQuestionObj.getId());
            updateStatement.execute();
        } catch (SQLException e) {
            System.out.println(this.getClass()+e.getMessage());
        }
    }

}
