package datamapper;

import datasource.DBConnection;
import domain.*;
import exceptions.RecordNotExistException;
import utils.IdentityMap;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.List;

public class ExamMapper extends DataMapper{

    private static volatile ExamMapper instance = null;

    public static ExamMapper getSingletonInstance() {
        if (instance == null) {
            synchronized (ExamAnswerMapper.class) {
                if (instance == null) {
                    instance = new ExamMapper();
                }
            }
        }
        return instance;
    }

    private static final String findWithIDStatement = "SELECT e.examName, e.subjectID, e.status "
            + "FROM oes.exams e "
            + "WHERE e.examID = ?";

    public Exam findWithID(String examID){
        Exam exam = new Exam();
        try {
            PreparedStatement findStatement = DBConnection.prepare(findWithIDStatement);
            findStatement.setString(1,examID);
            ResultSet rs = findStatement.executeQuery();
            while (rs.next()) {
                String examName = rs.getString(1);
                String subjectID = rs.getString(2);
                String status = rs.getString(3);
                exam.setId(examID);
                exam.setSubjectID(subjectID);
                exam.setExamName(examName);
                exam.setStatus(status);
            }
        } catch (SQLException e) {
            System.out.println(this.getClass()+e.getMessage());
        }
        return exam;
    }

    private static final String findWithSubjectCodeStatement = "SELECT e.examID, e.examName, e.status "
            + "FROM oes.exams e "
            + "WHERE e.subjectID = ?";

    public List<Exam> findWithSubjectCode(String subjectID) {
        List<Exam> exams = new ArrayList<Exam>();
        try {
            PreparedStatement findStatement = DBConnection.prepare(findWithSubjectCodeStatement);
            findStatement.setString(1,subjectID);
            ResultSet rs = findStatement.executeQuery();
            while (rs.next()) {
                Exam exam = new Exam();
                String examID = rs.getString(1);
                String examName = rs.getString(2);
                String status = rs.getString(3);
                exam.setId(examID);
                exam.setSubjectID(subjectID);
                exam.setExamName(examName);
                exam.setStatus(status);
                exams.add(exam);
            }
        } catch (SQLException e) {
            System.out.println(this.getClass()+e.getMessage());
        }
        return exams;
    }

    private static final String insertExamStatement =
            "INSERT INTO oes.exams (examID, subjectID, examName, status) VALUES (?, ?, ?, ?)";
    @Override
    public void insert(DomainObject object) throws RecordNotExistException{
        Exam examObj = (Exam) object;
        try {
            PreparedStatement insertStatement = DBConnection.prepare(insertExamStatement);
            insertStatement.setString(1,examObj.getId());
            insertStatement.setString(2,examObj.getSubjectID());
            insertStatement.setString(3, examObj.getExamName());
            insertStatement.setString(4, examObj.getStatus());
            insertStatement.execute();
        } catch (SQLException e) {
            System.out.println(this.getClass()+e.getMessage());
        }
    }

    private static final String updateExamStatement =
            "UPDATE oes.exams SET examName = ?, status = ? WHERE examID = ?";
    @Override
    public void update(DomainObject object) throws RecordNotExistException {
        Exam examObj = (Exam) object;
        try {
            PreparedStatement updateStatement = DBConnection.prepare(updateExamStatement);
            updateStatement.setString(1, examObj.getExamName());
            updateStatement.setString(2, examObj.getStatus());
            updateStatement.setString(3, examObj.getId());
            updateStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("sql message" + e.getMessage());
        }
    }

    private static final String deleteExamStatement =
            "DELETE FROM oes.exams e WHERE e.examID = ?";
    @Override
    public void delete(DomainObject object) {
        Exam examObj = (Exam) object;
        try {
            PreparedStatement updateStatement = DBConnection.prepare(deleteExamStatement);
            updateStatement.setString(1, examObj.getId());
            updateStatement.execute();
        } catch (SQLException e) {
            System.out.println(this.getClass()+e.getMessage());
        }
    }
}
