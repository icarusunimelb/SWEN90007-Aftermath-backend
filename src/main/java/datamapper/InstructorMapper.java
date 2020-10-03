package datamapper;

import datasource.DBConnection;
import domain.DomainObject;
import domain.ExamAnswer;
import domain.Instructor;
import domain.Name;
import utils.IdentityMap;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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
            System.out.println(e.getMessage());
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
            System.out.println(e.getMessage());
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
            System.out.println(e.getMessage());
        }
        return "";
    }

    private static final String insertInstructorStatement =
            "INSERT INTO oes.instructors (instructorID, firstName, lastName, email, password) VALUES (?, ?, ?, ?)";

    @Override
    public void insert(DomainObject object) {
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
            System.out.println(e.getMessage());
        }
    }

    private static final String updateInstructorStatement =
            "UPDATE oes.instructors i SET i.firstName = ?, i.lastName = ?, i.email = ?, i.password = ? " +
                    "WHERE i.instructorID = ?";
    @Override
    public void update(DomainObject object) {
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
            System.out.println(e.getMessage());
        }
    }

    private static final String deleteInstructorStatement =
            "DELETE FROM oes.instructors i WHERE i.instructorID = ?";
    @Override
    public void delete(DomainObject object) {
        Instructor instructorObj = (Instructor) object;
        try {
            PreparedStatement updateStatement = DBConnection.prepare(deleteInstructorStatement);
            updateStatement.setString(1, instructorObj.getId());
            updateStatement.execute();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
