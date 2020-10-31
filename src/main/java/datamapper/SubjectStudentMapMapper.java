package datamapper;

import datasource.DBConnection;
import domain.*;
import utils.IdentityMap;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SubjectStudentMapMapper extends DataMapper{

    private static volatile SubjectStudentMapMapper instance = null;

    public static SubjectStudentMapMapper getSingletonInstance() {
        if (instance == null) {
            synchronized (SubjectStudentMapMapper.class) {
                if (instance == null) {
                    instance = new SubjectStudentMapMapper();
                }
            }
        }
        return instance;
    }

    private static final String findWithIdStatement = "SELECT i.subjectID, i.studentID FROM oes.subjectStudentMap i " +
            "WHERE i.mapID = ?";

    public SubjectStudentMap findWithID(String id){
        SubjectStudentMap subjectStudentMap = new SubjectStudentMap();
        try{
            PreparedStatement findStatement = DBConnection.prepare(findWithIdStatement);
            findStatement.setString(1, id);
            ResultSet rs = findStatement.executeQuery();
            if(rs.next()){
                String subjectID = rs.getString(1);
                String studentID = rs.getString(2);
                subjectStudentMap.setId(id);
                subjectStudentMap.setSubjectID(subjectID);
                subjectStudentMap.setStudentID(studentID);
            }
        }catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return subjectStudentMap;
    }

    private static final String insertSubjectStudentMapStatement =
            "INSERT INTO oes.subjectStudentMap (subjectID, studentID) VALUES (?, ?)";

    @Override
    public void insert(DomainObject object) {
        SubjectStudentMap subjectStudentMap = (SubjectStudentMap) object;
        try{
            PreparedStatement insertStatement = DBConnection.prepare(insertSubjectStudentMapStatement);
            insertStatement.setString(1, subjectStudentMap.getSubjectID());
            insertStatement.setString(2, subjectStudentMap.getStudentID());
            insertStatement.execute();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void update(DomainObject object) {

    }

    @Override
    public void delete(DomainObject object) {

    }

}
