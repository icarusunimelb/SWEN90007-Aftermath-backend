package datamapper;

import datasource.DBConnection;
import domain.*;
import utils.IdentityMap;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AdminMapper extends DataMapper{
    private static volatile AdminMapper instance = null;

    public static AdminMapper getSingletonInstance() {
        if (instance == null) {
            synchronized (AdminMapper.class) {
                if (instance == null) {
                    instance = new AdminMapper();
                }
            }
        }
        return instance;
    }

    private static final String authenticateStatement = "SELECT s.password, s.adminID FROM oes.admin s " +
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


    @Override
    public void insert(DomainObject object) {

    }

    @Override
    public void update(DomainObject object) {

    }

    @Override
    public void delete(DomainObject object) {

    }
}
