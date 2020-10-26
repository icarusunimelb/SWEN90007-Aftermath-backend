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
                IdentityMap.getInstance(instructor).put(id, instructor);
            }
        }catch (SQLException e) {
            System.out.println(this.getClass()+e.getMessage());
        }
        return instructor;
    }

    private static final String checkRegisterStatement = "SELECT EXISTS(SELECT 1 FROM oes.instructors i " +
            "WHERE i.email = ? limit 1)";
    public boolean registerOrNot(String email) {
        boolean isRegisterOrNot = false;
        try {
            PreparedStatement findStatement = DBConnection.prepare(checkRegisterStatement);
            findStatement.setString(1, email);
            ResultSet rs = findStatement.executeQuery();
            if (rs.next()) {
                isRegisterOrNot = rs.getBoolean(1);
            }

        } catch (SQLException e) {
            System.out.println(this.getClass()+e.getMessage());
        }
        return isRegisterOrNot;
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
        //System.out.println("instructorId: "+instructorId);
        List<Subject> allSubjects = SubjectMapper.getSingletonInstance().findWithInstructorID(instructorId);
        //System.out.println("subjectSize: "+allSubjects.size());
        for(int i = 0; i < allSubjects.size(); i++){
            List<Exam> examOfSubject = ExamMapper.getSingletonInstance().findWithSubjectCode(allSubjects.get(i).getId());
            //System.out.println("subjectId: "+allSubjects.get(i).getId());
            List<Exam> validExams = new ArrayList<Exam>();
            for(int j = 0; j < examOfSubject.size(); j++){
                //System.out.println("Status:"+ examOfSubject.get(j).getStatus());
                if(examOfSubject.get(j).getStatus().equals("CLOSED") || examOfSubject.get(j).getStatus().equals("MARKED")){
                    //System.out.println("##########################################################");
                    List<ExamAnswer> examAnswers = ExamAnswerMapper.getSingletonInstance().
                            findTableViewExamAnswer(examOfSubject.get(j).getId());
                    //System.out.println("submissionSize:"+ examAnswers.size());
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
        //System.out.println("this is subject in instructor mapper size = " + subjects.size());
        for(int i = 0; i < subjects.size(); i++){
            List<Exam> examOfSubject = ExamMapper.getSingletonInstance().findWithSubjectCode(subjects.get(i).getId());
            subjects.get(i).setExams(examOfSubject);
            //System.out.println("this is in Instructor mapper !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        }
        //System.out.println("end of getManagingSubjects in Instructor mapper !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        return subjects;
    }
}
