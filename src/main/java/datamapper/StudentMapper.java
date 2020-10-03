package datamapper;

import datasource.DBConnection;
import domain.DomainObject;
import domain.Instructor;
import domain.Name;
import domain.Student;
import utils.IdentityMap;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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

    private static final String authenticateStatement = "SELECT s.password FROM oes.students s " +
            "WHERE s.email = ? limit 1";
    public boolean authenticate(String email, String password) {
        boolean match = false;
        try {
            PreparedStatement findStatement = DBConnection.prepare(authenticateStatement);
            findStatement.setString(1, email);
            ResultSet rs = findStatement.executeQuery();
            if (rs.next()) {
                String dbPassword = rs.getString(1);
                if (password.equals(dbPassword)) {
                    match = true;
                }
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return match;
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
}
