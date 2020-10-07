package datamapper;

import datasource.DBConnection;
import domain.*;
import utils.IdentityMap;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class StudentMapper extends DataMapper{
    private static volatile StudentMapper instance = null;

    public static StudentMapper getSingletonInstance() {
        if (instance == null) {
            synchronized (StudentMapper.class) {
                if (instance == null) {
                    instance = new StudentMapper();
                }
            }
        }
        return instance;
    }

    private static final String findWithIdStatement = "SELECT i.firstName, i.lastName, i.email, i.password FROM oes.students i " +
            "WHERE i.studentID = ?";

    public Student findWithID(String id){
        Student student = new Student();
        try{
            PreparedStatement findStatement = DBConnection.prepare(findWithIdStatement);
            findStatement.setString(1, id);
            ResultSet rs = findStatement.executeQuery();
            if(rs.next()){
                String firstName = rs.getString(1);
                String lastName = rs.getString(2);
                String email = rs.getString(3);
                String password = rs.getString(4);
                student.setId(id);
                student.setEmail(email);
                student.setName(new Name(firstName, lastName));
                student.setPassword(password);
                IdentityMap.getInstance(student).put(id, student);
            }
        }catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return student;
    }

    private static final String checkRegisterStatement = "SELECT EXISTS(SELECT 1 FROM oes.students s " +
            "WHERE s.email = ? limit 1)";
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
            System.out.println(e.getMessage());
        }
        return isRegisterOrNot;
    }

    private static final String authenticateStatement = "SELECT s.password, s.studentid FROM oes.students s " +
            "WHERE s.email = ? limit 1";
    public String authenticate(String email, String password) {
        boolean match = false;
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
            System.out.println(e.getMessage());
        }

        return "";
    }

    private static final String insertStudentStatement =
            "INSERT INTO oes.students (studentID, firstName, lastName, email, password) VALUES (?, ?, ?, ?, ?)";
    @Override
    public void insert(DomainObject object) {
        Student studentObj = (Student) object;
        try {
            PreparedStatement insertStatement = DBConnection.prepare(insertStudentStatement);
            insertStatement.setString(1, studentObj.getId());
            insertStatement.setString(2, studentObj.getName().getFirstName());
            insertStatement.setString(3, studentObj.getName().getLastName());
            insertStatement.setString(4, studentObj.getEmail());
            insertStatement.setString(5, studentObj.getPassword());
            insertStatement.execute();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private static final String updateStudentStatement =
            "UPDATE oes.students s SET s.firstName = ?, s.lastName = ?, s.email = ?, s.password = ? " +
                    "WHERE s.studentID = ?";
    @Override
    public void update(DomainObject object) {
        Student studentObj = (Student) object;
        try {
            PreparedStatement updateStatement = DBConnection.prepare(updateStudentStatement);
            updateStatement.setString(1, studentObj.getName().getFirstName());
            updateStatement.setString(2, studentObj.getName().getLastName());
            updateStatement.setString(3, studentObj.getEmail());
            updateStatement.setString(4, studentObj.getPassword());
            updateStatement.setString(5, studentObj.getId());
            updateStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private static final String deleteStudentStatement =
            "DELETE FROM oes.students s WHERE s.studentID = ?";
    @Override
    public void delete(DomainObject object) {
        Student studentObj = (Student) object;
        try {
            PreparedStatement updateStatement = DBConnection.prepare(deleteStudentStatement);
            updateStatement.setString(1, studentObj.getId());
            updateStatement.execute();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private static final String findWithSubjectIDStatement =
            "SELECT s.studentID, s.firstName, s.lastName, s.email, s.password "
                    + "FROM oes.students s "
                    + "LEFT JOIN oes.subjectStudentMap m ON s.studentID = m.studentID "
                    + "WHERE m.subjectID = ?";
    public List<Student> findWithSubjectID(String subjectID) {
        List<Student> students = new ArrayList<>();
        try{
            PreparedStatement findStatement = DBConnection.prepare(findWithSubjectIDStatement);
            findStatement.setString(1, subjectID);
            ResultSet rs = findStatement.executeQuery();
            if(rs.next()){
                Student student = new Student();
                String id = rs.getString(1);
                String firstName = rs.getString(2);
                String lastName = rs.getString(3);
                String email = rs.getString(4);
                String password = rs.getString(5);
                student.setId(id);
                student.setEmail(email);
                student.setName(new Name(firstName, lastName));
                student.setPassword(password);
                students.add(student);
            }
        }catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return students;
    }


    public List<Subject> getTakingSubjects(String studentId){
        List<Subject> subjects = SubjectMapper.getSingletonInstance().findWithStudentID(studentId);

        for(int i = 0; i < subjects.size(); i++){
            List<Exam> examOfSubject = ExamMapper.getSingletonInstance().findWithSubjectCode(subjects.get(i).getId());

            List<Exam> validExams = new ArrayList<Exam>();
            for(int j = 0; j < examOfSubject.size(); j++){
                if(examOfSubject.get(j).getStatus().equals("PUBLISHED") || examOfSubject.get(j).getStatus().equals("ONGOING")){
                    if (ExamAnswerMapper.getSingletonInstance().findWithStudentID(studentId)==null) {
                        validExams.add(examOfSubject.get(j));
                    }
                }
            }
            subjects.get(i).setExams(validExams);
        }
        return subjects;
    }

    public List<Student> getNotSubmittedStudents(List<Student> students, String examId){
        List<Student> notSubmittedStudents = new ArrayList<Student>();
        for(Student student: students){
            if(!ExamAnswerMapper.getSingletonInstance().checkIfStudentAnswer(student.getId(),examId)){
                notSubmittedStudents.add(student);
            }
        }
        return notSubmittedStudents;
    }
}
