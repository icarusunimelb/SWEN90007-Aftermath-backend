package datasource;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DBConnection {

	private static final String DB_CONNECTION = "jdbc:postgresql://localhost:5432/myDB";
	private static final String DB_USER = "postgres";
	private static final String DB_PASSWORD = "123456";


	public static PreparedStatement prepare(String stm) throws SQLException {

		PreparedStatement preparedStatement = null;
		try {

			Connection dbConnection = getDBConnection();

			preparedStatement = dbConnection.prepareStatement(stm);

		} catch (SQLException e) {

			System.out.println(e.getMessage());

		}

		return preparedStatement;
	}
	private static Connection getDBConnection() {



		try {
			DriverManager.registerDriver(new org.postgresql.Driver());

			Connection dbConnection = DriverManager.getConnection(
					DB_CONNECTION, DB_USER,DB_PASSWORD);
			return dbConnection;

		} catch (SQLException e) {

			System.out.println(e.getMessage());

		}
		System.out.println("Connection problem");
		return null;

	}


}
