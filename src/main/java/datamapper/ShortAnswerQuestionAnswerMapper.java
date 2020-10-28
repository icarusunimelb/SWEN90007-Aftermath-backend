package datamapper;

import datasource.DBConnection;
import domain.DomainObject;
import domain.MultipleChoiceQuestionAnswer;
import domain.ShortAnswerQuestionAnswer;
import exceptions.RecordNotExistException;
import utils.IdentityMap;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class ShortAnswerQuestionAnswerMapper extends DataMapper{
    private static volatile ShortAnswerQuestionAnswerMapper instance = null;
    private static DecimalFormat df2 = new DecimalFormat("###.##");
    public static ShortAnswerQuestionAnswerMapper getSingletonInstance() {
        if (instance == null) {
            synchronized (ShortAnswerQuestionAnswerMapper.class) {
                if (instance == null) {
                    instance = new ShortAnswerQuestionAnswerMapper();
                }
            }
        }
        return instance;
    }

    private static final String findWithTwoID = "SELECT s.shortAnswerQuestionAnswerID, s.mark, s.answer "
            + "FROM oes.shortAnswerQuestionAnswers s "
            + "WHERE s.examAnswerID = ? AND s.questionID = ?";
    public ShortAnswerQuestionAnswer findWithTwoID(String examAnswerID, String questionID){
        ShortAnswerQuestionAnswer answer = new ShortAnswerQuestionAnswer();
        try {
            PreparedStatement findStatement = DBConnection.prepare(findWithTwoID);
            findStatement.setString(1,examAnswerID);
            findStatement.setString(2,questionID);
            ResultSet rs = findStatement.executeQuery();
            if (rs.next()) {
                String id = rs.getString(1);
                int mark = rs.getInt(2);
                String answerText = rs.getString(3);
                answer.setExamAnswerID(examAnswerID);
                answer.setId(id);
                answer.setAnswer(answerText);
                answer.setMark(mark);
                answer.setQuestionID(questionID);
            }
        } catch (SQLException e) {
            System.out.println(this.getClass()+e.getMessage());
        }
        return answer;
    }

    private static final String findWithID = "SELECT s.questionID, s.examAnswerID, s.mark, s.answer "
            + "FROM oes.shortAnswerQuestionAnswers s "
            + "WHERE s.shortAnswerQuestionAnswerID = ?";
    public ShortAnswerQuestionAnswer findWithID(String id){
        ShortAnswerQuestionAnswer answer = new ShortAnswerQuestionAnswer();
        try {
            PreparedStatement findStatement = DBConnection.prepare(findWithID);
            findStatement.setString(1,id);
            ResultSet rs = findStatement.executeQuery();
            if (rs.next()) {
                String questionID = rs.getString(1);
                String examAnswerID = rs.getString(2);
                int mark = rs.getInt(3);
                String answerText = rs.getString(4);
                answer.setExamAnswerID(examAnswerID);
                answer.setId(id);
                answer.setAnswer(answerText);
                answer.setMark(mark);
                answer.setQuestionID(questionID);
            }
        } catch (SQLException e) {
            System.out.println(this.getClass()+e.getMessage());
        }
        return answer;
    }

    private static final String findAnswerStatement = "SELECT shortAnswerQuestionAnswerID, questionID, mark, "
            + "answer "
            + "FROM oes.shortAnswerQuestionAnswers "
            + "WHERE examAnswerID = ?";
    public List<ShortAnswerQuestionAnswer> findAllAnswers(String examAnswerID) {
        List<ShortAnswerQuestionAnswer> answers = new ArrayList<ShortAnswerQuestionAnswer>();
        try {
            PreparedStatement findStatement = DBConnection.prepare(findAnswerStatement);
            findStatement.setString(1,examAnswerID);
            ResultSet rs = findStatement.executeQuery();

            while (rs.next()) {
                ShortAnswerQuestionAnswer answer = new ShortAnswerQuestionAnswer();
                String shortAnswerQuestionAnswerID = rs.getString(1);
                String questionID = rs.getString(2);
                int mark = rs.getInt(3);
                String answerText = rs.getString(4);
                answer.setExamAnswerID(examAnswerID);
                answer.setId(shortAnswerQuestionAnswerID);
                answer.setMark(mark);
                answer.setAnswer(answerText);
                answer.setQuestionID(questionID);
                answers.add(answer);
            }
        } catch (SQLException e) {
            System.out.println(this.getClass()+e.getMessage());
        }

        return answers;
    }

    private static final String insertAnswerStatement =
            "INSERT INTO oes.shortAnswerQuestionAnswers (shortAnswerQuestionAnswerID, questionID, examAnswerID, mark, answer) " +
                    "VALUES (?, ?, ?, ?, ?)";

    @Override
    public void insert(DomainObject object) throws RecordNotExistException {
        ShortAnswerQuestionAnswer shortAnswerQuestionAnswerObj = (ShortAnswerQuestionAnswer) object;
        try {
            PreparedStatement insertStatement = DBConnection.prepare(insertAnswerStatement);
            insertStatement.setString(1, shortAnswerQuestionAnswerObj.getId());
            insertStatement.setString(2, shortAnswerQuestionAnswerObj.getQuestionID());
            insertStatement.setString(3, shortAnswerQuestionAnswerObj.getExamAnswerID());
            insertStatement.setInt(4, shortAnswerQuestionAnswerObj.getMark());
            insertStatement.setString(5, shortAnswerQuestionAnswerObj.getAnswer());
            insertStatement.execute();
        } catch (SQLException e) {
            System.out.println(this.getClass()+e.getMessage());
        }
    }

    private static final String updateSAQAStatement =
            "UPDATE oes.shortAnswerQuestionAnswers SET answer = ?, mark = ? " +
                    "WHERE shortAnswerQuestionAnswerID = ?";
    @Override
    public void update(DomainObject object) throws RecordNotExistException{
        ShortAnswerQuestionAnswer shortAnswerQuestionAnswerObj = (ShortAnswerQuestionAnswer) object;
        try {
            PreparedStatement updateStatement = DBConnection.prepare(updateSAQAStatement);
            updateStatement.setString(1, shortAnswerQuestionAnswerObj.getAnswer());
            updateStatement.setInt(2, shortAnswerQuestionAnswerObj.getMark());
            updateStatement.setString(3, shortAnswerQuestionAnswerObj.getId());
            updateStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(this.getClass()+e.getMessage());
        }
    }

    private static final String deleteSAQAStatement =
            "DELETE FROM oes.shortAnswerQuestionAnswers s WHERE s.shortAnswerQuestionAnswerID = ?";
    @Override
    public void delete(DomainObject object) {
        ShortAnswerQuestionAnswer shortAnswerQuestionAnswerObj = (ShortAnswerQuestionAnswer) object;
        try {
            PreparedStatement updateStatement = DBConnection.prepare(deleteSAQAStatement);
            updateStatement.setString(1, shortAnswerQuestionAnswerObj.getId());
            updateStatement.execute();
        } catch (SQLException e) {
            System.out.println(this.getClass()+e.getMessage());
        }
    }
}
