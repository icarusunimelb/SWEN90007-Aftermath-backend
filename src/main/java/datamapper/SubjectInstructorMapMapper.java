package datamapper;

import datasource.DBConnection;
import domain.*;
import utils.IdentityMap;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SubjectInstructorMapMapper extends DataMapper{

    private static volatile SubjectInstructorMapMapper instance = null;

    public static SubjectInstructorMapMapper getSingletonInstance() {
        if (instance == null) {
            synchronized (SubjectInstructorMapMapper.class) {
                if (instance == null) {
                    instance = new SubjectInstructorMapMapper();
                }
            }
        }
        return instance;
    }

    private static final String findWithIdStatement = "SELECT i.subjectID, i.instructorID FROM oes.subjectInstructorMap i " +
            "WHERE i.mapID = ?";

    public SubjectInstructorMap findWithID(String id){
        SubjectInstructorMap subjectInstructorMap = new SubjectInstructorMap();
        try{
            PreparedStatement findStatement = DBConnection.prepare(findWithIdStatement);
            findStatement.setString(1, id);
            ResultSet rs = findStatement.executeQuery();
            if(rs.next()){
                String subjectID = rs.getString(1);
                String instructorID = rs.getString(2);
                subjectInstructorMap.setId(id);
                subjectInstructorMap.setSubjectID(subjectID);
                subjectInstructorMap.setInstructorID(instructorID);
                IdentityMap.getInstance(subjectInstructorMap).put(id, subjectInstructorMap);
            }
        }catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return subjectInstructorMap;
    }

    private static final String insertSubjectStudentMapStatement =
            "INSERT INTO oes.subjectInstructorMap (subjectID, instructorID) VALUES (?, ?)";

    @Override
    public void insert(DomainObject object) {
        SubjectInstructorMap subjectInstructorMap = (SubjectInstructorMap) object;
        try{
            PreparedStatement insertStatement = DBConnection.prepare(insertSubjectStudentMapStatement);
            insertStatement.setString(1, subjectInstructorMap.getSubjectID());
            insertStatement.setString(2, subjectInstructorMap.getInstructorID());
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
