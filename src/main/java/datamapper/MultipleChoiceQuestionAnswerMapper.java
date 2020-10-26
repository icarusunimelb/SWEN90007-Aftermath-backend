package datamapper;

import datasource.DBConnection;
import domain.*;
import exceptions.RecordNotExistException;
import utils.IdentityMap;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class MultipleChoiceQuestionAnswerMapper extends DataMapper{

    private static volatile MultipleChoiceQuestionAnswerMapper instance = null;
    private static DecimalFormat df2 = new DecimalFormat("###.##");
    public static MultipleChoiceQuestionAnswerMapper getSingletonInstance() {
        if (instance == null) {
            synchronized (MultipleChoiceQuestionAnswerMapper.class) {
                if (instance == null) {
                    instance = new MultipleChoiceQuestionAnswerMapper();
                }
            }
        }
        return instance;
    }

    private static final String findWithTwoID = "SELECT m.multipleChoiceQuestionAnswerID, m.mark, m.answerIndex "
            + "FROM oes.multipleChoiceQuestionAnswers m "
            + "WHERE m.examAnswerID = ? AND m.questionID = ?";
    public MultipleChoiceQuestionAnswer findWithTwoID(String examAnswerID, String questionID){
        MultipleChoiceQuestionAnswer answer = new MultipleChoiceQuestionAnswer();
        try {
            PreparedStatement findStatement = DBConnection.prepare(findWithTwoID);
            findStatement.setString(1,examAnswerID);
            findStatement.setString(2,questionID);
            ResultSet rs = findStatement.executeQuery();
            if (rs.next()) {
                String id = rs.getString(1);
                int mark = rs.getInt(2);
                int answerIndex = rs.getInt(3);
                answer.setExamAnswerID(examAnswerID);
                answer.setId(id);
                answer.setAnswerIndex(answerIndex);
                answer.setMark(mark);
                answer.setQuestionID(questionID);
            }
        } catch (SQLException e) {
            System.out.println(this.getClass()+e.getMessage());
        }
        return answer;
    }

    private static final String findWithID = "SELECT m.questionID, m.examAnswerID, m.mark, m.answerIndex "
            + "FROM oes.multipleChoiceQuestionAnswers m "
            + "WHERE m.multipleChoiceQuestionAnswerID = ?";
    public MultipleChoiceQuestionAnswer findWithID(String id){
        MultipleChoiceQuestionAnswer answer = new MultipleChoiceQuestionAnswer();
        try {
            PreparedStatement findStatement = DBConnection.prepare(findAnswerStatement);
            findStatement.setString(1,id);
            ResultSet rs = findStatement.executeQuery();
            if (rs.next()) {
                String questionID = rs.getString(1);
                String examAnswerID = rs.getString(2);
                int mark = rs.getInt(3);
                int answerIndex = rs.getInt(4);
                answer.setExamAnswerID(examAnswerID);
                answer.setId(id);
                answer.setAnswerIndex(answerIndex);
                answer.setMark(mark);
                answer.setQuestionID(questionID);
            }
        } catch (SQLException e) {
            System.out.println(this.getClass()+e.getMessage());
        }
        return answer;
    }


    private static final String findAnswerStatement = "SELECT m.multipleChoiceQuestionAnswerID, " +
            "m.questionID, m.mark, m.answerIndex "
            + "FROM oes.multipleChoiceQuestionAnswers m "
            + "WHERE m.examAnswerID = ?";
    public List<MultipleChoiceQuestionAnswer> findAllAnswers(String examAnswerID) {
        List<MultipleChoiceQuestionAnswer> answers = new ArrayList<MultipleChoiceQuestionAnswer>();
        try {
            PreparedStatement findStatement = DBConnection.prepare(findAnswerStatement);
            findStatement.setString(1,examAnswerID);
            ResultSet rs = findStatement.executeQuery();
            while (rs.next()) {
                MultipleChoiceQuestionAnswer answer = new MultipleChoiceQuestionAnswer();
                String multipleChoiceQuestionAnswerID = rs.getString(1);
                String questionID = rs.getString(2);
                int mark = rs.getInt(3);
                int answerIndex = rs.getInt(4);
                answer.setExamAnswerID(examAnswerID);
                answer.setId(multipleChoiceQuestionAnswerID);
                answer.setAnswerIndex(answerIndex);
                answer.setMark(mark);
                answer.setQuestionID(questionID);
                answers.add(answer);
            }
        } catch (SQLException e) {
            System.out.println(this.getClass()+e.getMessage());
        }

        return answers;
    }

    private static final String insertAnswerStatement =
            "INSERT INTO oes.multipleChoiceQuestionAnswers (multipleChoiceQuestionAnswerID, questionID, examAnswerId, mark, answerIndex) " +
                    "VALUES (?, ?, ?, ?, ?)";

    @Override
    public void insert(DomainObject object) throws RecordNotExistException {
        MultipleChoiceQuestionAnswer multipleChoiceQuestionAnswerObj = (MultipleChoiceQuestionAnswer) object;
        try {
            PreparedStatement insertStatement = DBConnection.prepare(insertAnswerStatement);
            insertStatement.setString(1, multipleChoiceQuestionAnswerObj.getId());
            insertStatement.setString(2, multipleChoiceQuestionAnswerObj.getQuestionID());
            insertStatement.setString(3, multipleChoiceQuestionAnswerObj.getExamAnswerID());
            insertStatement.setInt(4, multipleChoiceQuestionAnswerObj.getMark());
            insertStatement.setInt(5, multipleChoiceQuestionAnswerObj.getAnswerIndex());
            insertStatement.execute();
        } catch (SQLException e) {
            System.out.println(this.getClass()+e.getMessage());
        }
    }

    private static final String updateMCQAStatement =
            "UPDATE oes.multipleChoiceQuestionAnswers SET answerIndex = ?, mark = ? " +
                    "WHERE multipleChoiceQuestionAnswerID = ?";
    @Override
    public void update(DomainObject object) throws RecordNotExistException{
        MultipleChoiceQuestionAnswer multipleChoiceQuestionAnswerObj = (MultipleChoiceQuestionAnswer) object;
        try {
            PreparedStatement updateStatement = DBConnection.prepare(updateMCQAStatement);
            updateStatement.setInt(1, multipleChoiceQuestionAnswerObj.getAnswerIndex());
            // System.out.println("!!!!!!!!!!!!!!! this is your mark in Mapper" + multipleChoiceQuestionAnswerObj.getId());
            updateStatement.setInt(2, multipleChoiceQuestionAnswerObj.getMark());
            updateStatement.setString(3, multipleChoiceQuestionAnswerObj.getId());
            updateStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(this.getClass()+e.getMessage());
        }
    }

    private static final String deleteMCQAStatement =
            "DELETE FROM oes.multipleChoiceQuestionAnswers m WHERE m.multipleChoiceQuestionAnswerID = ?";
    @Override
    public void delete(DomainObject object) {
        MultipleChoiceQuestionAnswer multipleChoiceQuestionAnswerObj = (MultipleChoiceQuestionAnswer) object;
        try {
            PreparedStatement updateStatement = DBConnection.prepare(deleteMCQAStatement);
            updateStatement.setString(1, multipleChoiceQuestionAnswerObj.getId());
            updateStatement.execute();
        } catch (SQLException e) {
            System.out.println(this.getClass()+e.getMessage());
        }
    }
}
