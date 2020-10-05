package datamapper;

import datasource.DBConnection;
import domain.*;
import utils.IdentityMap;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ExamAnswerMapper extends  DataMapper{

    private static volatile ExamAnswerMapper instance = null;

    public static ExamAnswerMapper getSingletonInstance() {
        if (instance == null) {
            synchronized (ExamAnswerMapper.class) {
                if (instance == null) {
                    instance = new ExamAnswerMapper();
                }
            }
        }
        return instance;
    }

    private static final String findWithIDStatement = "SELECT e.examID, e.studentID, e.finalMark "
            + "FROM oes.examAnswer e "
            + "WHERE e.examAnswerID = ?";

    public ExamAnswer findWithID(String examAnswerID) {
        ExamAnswer examAnswer = new ExamAnswer();
        try {
            PreparedStatement stmt = DBConnection.prepare(findWithIDStatement);
            stmt.setString(1, examAnswerID);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String examID = rs.getString(1);
                String studentID = rs.getString(2);
                double finalMark = rs.getDouble(3);
                examAnswer.setExamID(examID);
                examAnswer.setId(examAnswerID);
                examAnswer.setStudentID(studentID);
                examAnswer.setFinalMark(finalMark);
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return examAnswer;
    }

    private static final String findTableViewExamAnswerStatement = "SELECT e.examAnswerID, e.studentID, e.finalMark "
            + "FROM oes.examAnswer e "
            + "WHERE e.examID = ?";

    public List<ExamAnswer> findTableViewExamAnswer(String examID) {
        List<ExamAnswer> result = new ArrayList<>();
        try {
            PreparedStatement stmt = DBConnection.prepare(findTableViewExamAnswerStatement);
            stmt.setString(1, examID);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                ExamAnswer examAnswer = new ExamAnswer();
                String examAnswerID = rs.getString(1);
                String studentID = rs.getString(2);
                double finalMark = rs.getDouble(3);
                examAnswer.setExamID(examID);
                examAnswer.setId(examAnswerID);
                examAnswer.setStudentID(studentID);
                examAnswer.setFinalMark(finalMark);
                result.add(examAnswer);
            }
            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!"+result.size());

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return result;
    }

    private static final String insertExamAnswerStatement =
            "INSERT INTO oes.examAnswer (examAnswerID, examID, studentID, finalMark) VALUES (?, ?, ?, ?)";

    @Override
    public void insert(DomainObject object) {
        ExamAnswer examAnswerObj = (ExamAnswer) object;
        try{
            PreparedStatement insertStatement = DBConnection.prepare(insertExamAnswerStatement);
            insertStatement.setString(1, examAnswerObj.getId());
            insertStatement.setString(2, examAnswerObj.getExamID());
            insertStatement.setString(3, examAnswerObj.getStudentID());
            insertStatement.setDouble(4, examAnswerObj.getFinalMark());
            insertStatement.execute();
        }catch (SQLException e){
            System.out.println(e);
        }
    }

    private static final String checkStatement =
            "SELECT EXISTS(SELECT 1 FROM oes.examAnswer e " +
                    "WHERE e.examID = ? AND e.studentID = ? limit 1)";

    public boolean checkIfStudentAnswer(String examID, String studentID){
        try {
            PreparedStatement stmt = DBConnection.prepare(checkStatement);
            stmt.setString(1, examID);
            stmt.setString(2, studentID);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getBoolean(1);
            }
        }catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    private static final String updateExamAnswerStatement =
            "UPDATE oes.examAnswer SET finalMark = ? WHERE examAnswerID = ?";
    @Override
    public void update(DomainObject object) {
        ExamAnswer examAnswerObj = (ExamAnswer) object;
        try {
            PreparedStatement updateStatement = DBConnection.prepare(updateExamAnswerStatement);
            updateStatement.setDouble(1, examAnswerObj.getFinalMark());
            updateStatement.setString(2, examAnswerObj.getId());
            updateStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private static final String deleteExamAnswerStatement =
            "DELETE FROM oes.examAnswer e WHERE e.examAnswerID = ?";
    @Override
    public void delete(DomainObject object) {
        ExamAnswer examAnswerObj = (ExamAnswer) object;
        try {
            PreparedStatement updateStatement = DBConnection.prepare(deleteExamAnswerStatement);
            updateStatement.setString(1, examAnswerObj.getId());
            updateStatement.execute();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
