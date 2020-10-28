package datamapper;

import datasource.DBConnection;
import domain.DomainObject;
import domain.Subject;
import exceptions.RecordNotExistException;
import utils.IdentityMap;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SubjectMapper extends DataMapper{
    private static volatile SubjectMapper instance = null;

    public static SubjectMapper getSingletonInstance() {
        if (instance == null) {
            synchronized (SubjectMapper.class) {
                if (instance == null) {
                    instance = new SubjectMapper();
                }
            }
        }
        return instance;
    }

    private static final String findWithIDStatement = "SELECT s.subjectCode, s.subjectName "
            + "FROM oes.subjects s "
            + "WHERE s.subjectID = ?";
    public Subject findWithID(String subjectID) {
        Subject subject = new Subject();
        try{
            PreparedStatement findStatement = DBConnection.prepare(findWithIDStatement);
            findStatement.setString(1, subjectID);
            ResultSet rs = findStatement.executeQuery();
            while (rs.next()) {
                String subjectCode = rs.getString(1);
                String subjectName = rs.getString(2);
                subject.setSubjectCode(subjectCode);
                subject.setSubjectName(subjectName);
            }
        }catch (SQLException e){
            System.out.println(this.getClass()+e.getMessage());
        }
        return subject;
    }

    private static final String findWithInstructorIDStatement =
            "SELECT s.subjectID, s.subjectCode, s.subjectName "
            + "FROM oes.subjects s "
            + "RIGHT JOIN oes.subjectInstructorMap s1 ON s.subjectID = s1.subjectID "
            + "WHERE s1.instructorID = ?";
    public List<Subject> findWithInstructorID(String instructorID) {
        List<Subject> subjects = new ArrayList<>();
        try{
            PreparedStatement findStatement = DBConnection.prepare(findWithInstructorIDStatement);
            findStatement.setString(1, instructorID);
            ResultSet rs = findStatement.executeQuery();
            while (rs.next()) {
                Subject subject = new Subject();
                String subjectID = rs.getString(1);
                String subjectCode = rs.getString(2);
                String subjectName = rs.getString(3);
                subject.setId(subjectID);
                subject.setSubjectCode(subjectCode);
                subject.setSubjectName(subjectName);
                subjects.add(subject);
            }
        }catch (SQLException e){
            System.out.println(this.getClass()+e.getMessage());
        }
        //System.out.println("this is in SubjectMapper. subjects size = "+ subjects.size());
        return subjects;
    }

    private static final String findWithStudentIDStatement =
            "SELECT s.subjectID, s.subjectCode, s.subjectName "
                    + "FROM oes.subjects s "
                    + "RIGHT JOIN oes.subjectStudentMap s1 ON s.subjectID = s1.subjectID "
                    + "WHERE s1.studentID = ?";
    public List<Subject> findWithStudentID(String studentID) {
        List<Subject> subjects = new ArrayList<>();
        try{
            PreparedStatement findStatement = DBConnection.prepare(findWithStudentIDStatement);
            findStatement.setString(1, studentID);
            ResultSet rs = findStatement.executeQuery();
            while (rs.next()) {
                Subject subject = new Subject();
                String subjectID = rs.getString(1);
                String subjectCode = rs.getString(2);
                String subjectName = rs.getString(3);
                subject.setId(subjectID);
                subject.setSubjectCode(subjectCode);
                subject.setSubjectName(subjectName);
                subjects.add(subject);
            }
        }catch (SQLException e){
            System.out.println(this.getClass()+e.getMessage());
        }
        return subjects;
    }

    private static final String insertSubjectStatement =
            "INSERT INTO oes.subjects (subjectID, subjectCode, subjectName) VALUES (?, ?, ?)";
    @Override
    public void insert(DomainObject object) throws RecordNotExistException{
        Subject subjectObj = (Subject) object;
        try {
            PreparedStatement insertStatement = DBConnection.prepare(insertSubjectStatement);
            insertStatement.setString(1, subjectObj.getId());
            insertStatement.setString(2,subjectObj.getSubjectCode());
            insertStatement.setString(3, subjectObj.getSubjectName());
            insertStatement.execute();
        } catch (SQLException e) {
            System.out.println(this.getClass()+e.getMessage());
        }
    }

    private static final String updateSubjectStatement =
            "UPDATE oes.subjects s SET s.subjectCode = ?, s.subjectName = ? " +
                    "WHERE s.subjectID = ?";
    @Override
    public void update(DomainObject object) throws RecordNotExistException {
        Subject subjectObj = (Subject) object;
        try {
            PreparedStatement updateStatement = DBConnection.prepare(updateSubjectStatement);
            updateStatement.setString(1, subjectObj.getSubjectCode());
            updateStatement.setString(2, subjectObj.getSubjectName());
            updateStatement.setString(3, subjectObj.getId());
            updateStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(this.getClass()+e.getMessage());
        }
    }

    private static final String deleteSubjectStatement =
            "DELETE FROM oes.subjects s WHERE s.subjectID = ?";
    @Override
    public void delete(DomainObject object) {
        Subject subjectObj = (Subject) object;
        try {
            PreparedStatement updateStatement = DBConnection.prepare(deleteSubjectStatement);
            updateStatement.setString(1, subjectObj.getId());
            updateStatement.execute();
        } catch (SQLException e) {
            System.out.println(this.getClass()+e.getMessage());
        }
    }

    // TODO confirm whether password is needed
    private static final String allSubjectStatement =
            "SELECT subjectID, subjectCode, subjectName FROM oes.subjects";
    public List<Subject> getAllSubjects(){
        List<Subject> allSubjects = new ArrayList<Subject>();

        try{
            PreparedStatement findStatement = DBConnection.prepare(allSubjectStatement);
            ResultSet rs = findStatement.executeQuery();
            if(rs.next()){
                Subject subject = new Subject();
                String subjectID = rs.getString(1);
                String subjectCode = rs.getString(2);
                String subjectName = rs.getString(3);
                subject.setId(subjectID);
                subject.setSubjectCode(subjectCode);
                subject.setSubjectName(subjectName);
                allSubjects.add(subject);
            }
        }catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return allSubjects;
    }
}
