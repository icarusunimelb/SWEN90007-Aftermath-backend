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

public class InstructorMapper extends DataMapper{

    private static volatile InstructorMapper instance = null;

    public static InstructorMapper getSingletonInstance() {
        if (instance == null) {
            synchronized (InstructorMapper.class) {
                if (instance == null) {
                    instance = new InstructorMapper();
                }
            }
        }
        return instance;
    }

    private static final String findWithIdStatement = "SELECT i.firstName, i.lastName, i.email, i.password FROM oes.instructors i " +
            "WHERE i.instructorID = ?";

    public Instructor findWithID(String id){
        Instructor instructor = new Instructor();
        try{
            PreparedStatement findStatement = DBConnection.prepare(findWithIdStatement);
            findStatement.setString(1, id);
            ResultSet rs = findStatement.executeQuery();
            if(rs.next()){
                String firstName = rs.getString(1);
                String lastName = rs.getString(2);
                String email = rs.getString(3);
                String password = rs.getString(4);
                instructor.setId(id);
                instructor.setEmail(email);
                instructor.setName(new Name(firstName, lastName));
                instructor.setPassword(password);
            }
        }catch (SQLException e) {
            System.out.println(this.getClass()+e.getMessage());
        }
        return instructor;
    }

    private static final String findWithSubjectIDStatement =
            "SELECT s.instructorID, s.firstName, s.lastName, s.email, s.password "
                    + "FROM oes.instructors s "
                    + "LEFT JOIN oes.subjectInstructorMap m ON s.instructorID = m.instructorID "
                    + "WHERE m.subjectID = ?";
    public List<Instructor> findWithSubjectID(String subjectID) {
        List<Instructor> instructors = new ArrayList<>();
        try{
            PreparedStatement findStatement = DBConnection.prepare(findWithSubjectIDStatement);
            findStatement.setString(1, subjectID);
            ResultSet rs = findStatement.executeQuery();
            while(rs.next()){
                Instructor instructor = new Instructor();
                String id = rs.getString(1);
                String firstName = rs.getString(2);
                String lastName = rs.getString(3);
                String email = rs.getString(4);
                String password = rs.getString(5);
                instructor.setId(id);
                instructor.setEmail(email);
                instructor.setName(new Name(firstName, lastName));
                instructor.setPassword(password);
                instructors.add(instructor);
            }
        }catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return instructors;
    }

    private static final String authenticateStatement = "SELECT i.password, i.instructorid FROM oes.instructors i " +
            "WHERE i.email = ? limit 1";
    public String authenticate(String email, String password) {
        try {
            PreparedStatement findStatement = DBConnection.prepare(authenticateStatement);
            findStatement.setString(1, email);
            ResultSet rs = findStatement.executeQuery();
            if (rs.next()) {
                String dbPassword = rs.getString(1);
                String dataID = rs.getString(2);
                if (password.equals(dbPassword)) {
                    return dataID;
                }
            }

        } catch (SQLException e) {
            System.out.println(this.getClass()+e.getMessage());
        }
        return "";
    }

    private static final String insertInstructorStatement =
            "INSERT INTO oes.instructors (instructorID, firstName, lastName, email, password) VALUES (?, ?, ?, ?)";

    @Override
    public void insert(DomainObject object) throws RecordNotExistException{
        Instructor instructorObj = (Instructor) object;
        try{
            PreparedStatement insertStatement = DBConnection.prepare(insertInstructorStatement);
            insertStatement.setString(1, instructorObj.getId());
            insertStatement.setString(2, instructorObj.getName().getFirstName());
            insertStatement.setString(3, instructorObj.getName().getLastName());
            insertStatement.setString(4, instructorObj.getEmail());
            insertStatement.setString(5, instructorObj.getPassword());
            insertStatement.execute();
        } catch (SQLException e) {
            System.out.println(this.getClass()+e.getMessage());
        }
    }

    private static final String updateInstructorStatement =
            "UPDATE oes.instructors i SET i.firstName = ?, i.lastName = ?, i.email = ?, i.password = ? " +
                    "WHERE i.instructorID = ?";
    @Override
    public void update(DomainObject object) throws RecordNotExistException{
        Instructor instructorObj = (Instructor) object;
        try {
            PreparedStatement updateStatement = DBConnection.prepare(updateInstructorStatement);
            updateStatement.setString(1, instructorObj.getName().getFirstName());
            updateStatement.setString(2, instructorObj.getName().getLastName());
            updateStatement.setString(3, instructorObj.getEmail());
            updateStatement.setString(4, instructorObj.getPassword());
            updateStatement.setString(5, instructorObj.getId());
            updateStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(this.getClass()+e.getMessage());
        }
    }

    private static final String deleteInstructorStatement =
            "DELETE FROM oes.instructors i WHERE i.instructorID = ?";
    @Override
    public void delete(DomainObject object) {
        Instructor instructorObj = (Instructor) object;
        try {
            System.out.println("Warning: Instructor cannot be deleted!!!");
            PreparedStatement updateStatement = DBConnection.prepare(deleteInstructorStatement);
            updateStatement.setString(1, instructorObj.getId());
            updateStatement.execute();
        } catch (SQLException e) {
            System.out.println(this.getClass()+e.getMessage());
        }
    }

    public List<Subject> getMarkingSubjects(String instructorId) throws RecordNotExistException {
        List<Subject> allSubjects = SubjectMapper.getSingletonInstance().findWithInstructorID(instructorId);
        for(int i = 0; i < allSubjects.size(); i++){
            List<Exam> examOfSubject = ExamMapper.getSingletonInstance().findWithSubjectCode(allSubjects.get(i).getId());
            List<Exam> validExams = new ArrayList<Exam>();
            for(int j = 0; j < examOfSubject.size(); j++){
                if(examOfSubject.get(j).getStatus().equals("CLOSED") || examOfSubject.get(j).getStatus().equals("MARKED")){
                    List<ExamAnswer> examAnswers = ExamAnswerMapper.getSingletonInstance().
                            findTableViewExamAnswer(examOfSubject.get(j).getId());
                    examOfSubject.get(j).setExamAnswers(examAnswers);
                    validExams.add(examOfSubject.get(j));
                }
            }
            allSubjects.get(i).setExams(validExams);
        }
        return allSubjects;
    }

    public List<Subject> getManagingSubjects(String instructorId) {

        List<Subject> subjects = SubjectMapper.getSingletonInstance().findWithInstructorID(instructorId);
        for(int i = 0; i < subjects.size(); i++){
            List<Exam> examOfSubject = ExamMapper.getSingletonInstance().findWithSubjectCode(subjects.get(i).getId());
            subjects.get(i).setExams(examOfSubject);
        }
        return subjects;
    }

    // TODO confirm whether password is needed
    private static final String allInstructorStatement =
            "SELECT instructorID, firstName, lastName, email FROM oes.instructors";
    public List<Instructor> getAllInstructors(){
        List<Instructor> allInstructors = new ArrayList<Instructor>();

        try{
            PreparedStatement findStatement = DBConnection.prepare(allInstructorStatement);
            ResultSet rs = findStatement.executeQuery();
            while(rs.next()){
                Instructor instructor = new Instructor();
                String instructorID = rs.getString(1);
                String firstName = rs.getString(2);
                String lastName = rs.getString(3);
                String email = rs.getString(4);
                instructor.setId(instructorID);
                instructor.setEmail(email);
                instructor.setName(new Name(firstName, lastName));
                allInstructors.add(instructor);
            }
        }catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return allInstructors;
    }
}
