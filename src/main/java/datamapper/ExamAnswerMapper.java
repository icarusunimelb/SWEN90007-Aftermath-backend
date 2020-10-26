package datamapper;

import datasource.DBConnection;
import domain.*;
import exceptions.RecordNotExistException;
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
                int finalMark = rs.getInt(3);
                examAnswer.setExamID(examID);
                examAnswer.setId(examAnswerID);
                examAnswer.setStudentID(studentID);
                examAnswer.setFinalMark(finalMark);
            }

        } catch (SQLException e) {
            System.out.println(this.getClass()+e.getMessage());
        }

        return examAnswer;
    }

    private static final String findWithStudentIDStatement = "SELECT e.examAnswerID, e.finalMark "
            + "FROM oes.examAnswer e "
            + "WHERE e.studentID = ? AND e.examID = ?";

    public ExamAnswer findWithStudentID(String examID, String studentID) {
        ExamAnswer examAnswer = null;
        try {
            PreparedStatement stmt = DBConnection.prepare(findWithStudentIDStatement);
            stmt.setString(1, studentID);
            stmt.setString(2, examID);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                examAnswer = new ExamAnswer();
                String examAnswerID = rs.getString(1);
                int finalMark = rs.getInt(2);
                examAnswer.setExamID(examID);
                examAnswer.setId(examAnswerID);
                examAnswer.setStudentID(studentID);
                examAnswer.setFinalMark(finalMark);
            }

        } catch (SQLException e) {
            System.out.println(this.getClass()+e.getMessage());
        }

        return examAnswer;
    }

    private static final String findTableViewExamAnswerStatement = "SELECT e.examAnswerID, e.studentID, e.finalMark "
            + "FROM oes.examAnswer e "
            + "WHERE e.examID = ?";

    public List<ExamAnswer> findTableViewExamAnswer(String examID) {
        System.out.println("examID: "+examID);
        List<ExamAnswer> result = new ArrayList<>();
        try {
            PreparedStatement stmt = DBConnection.prepare(findTableViewExamAnswerStatement);
            stmt.setString(1, examID);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                //System.out.println("insideloop: "+result.size());
                ExamAnswer examAnswer = new ExamAnswer();
                String examAnswerID = rs.getString(1);
                String studentID = rs.getString(2);
                int finalMark = rs.getInt(3);
                examAnswer.setExamID(examID);
                examAnswer.setId(examAnswerID);
                examAnswer.setStudentID(studentID);
                examAnswer.setFinalMark(finalMark);
                result.add(examAnswer);
            }
            //System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!"+result.size());

        } catch (SQLException e) {
            System.out.println(this.getClass()+e.getMessage());
        }

        return result;
    }

    private static final String insertExamAnswerStatement =
            "INSERT INTO oes.examAnswer (examAnswerID, examID, studentID, finalMark) VALUES (?, ?, ?, ?)";

    @Override
    public void insert(DomainObject object) throws RecordNotExistException{
        ExamAnswer examAnswerObj = (ExamAnswer) object;
        try{
            PreparedStatement insertStatement = DBConnection.prepare(insertExamAnswerStatement);
            insertStatement.setString(1, examAnswerObj.getId());
            insertStatement.setString(2, examAnswerObj.getExamID());
            insertStatement.setString(3, examAnswerObj.getStudentID());
            insertStatement.setInt(4, examAnswerObj.getFinalMark());
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
            System.out.println("Here: "+e.getMessage());
        }
        return false;
    }

    private static final String updateExamAnswerStatement =
            "UPDATE oes.examAnswer SET finalMark = ? WHERE examAnswerID = ?";
    @Override
    public void update(DomainObject object) throws RecordNotExistException {
        ExamAnswer examAnswerObj = (ExamAnswer) object;
        try {
            PreparedStatement updateStatement = DBConnection.prepare(updateExamAnswerStatement);
            updateStatement.setInt(1, examAnswerObj.getFinalMark());
            updateStatement.setString(2, examAnswerObj.getId());
            updateStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(this.getClass()+e.getMessage());
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
            System.out.println(this.getClass()+e.getMessage());
        }
    }
}
