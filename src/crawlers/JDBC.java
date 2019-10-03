package crawlers;

import java.sql.Connection;
import java.sql.Statement;
import java.util.Optional;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

public class JDBC {

	private static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	private static final String DATABASE_URL = "jdbc:mysql://localhost/EMP";
	//TODO: Find secure way instead of hardcoding file prop maybe?
	private static final String username = null;
	private static final String password = null;
	
	private static Connection connect() throws ClassNotFoundException, SQLException {
		Connection connection = null;
		
		Class.forName("com.mysql.jdbc.Driver");
		connection = DriverManager.getConnection(DATABASE_URL, username, password);
		
		return connection;
	}
	
	private static Statement statement(Connection connection) throws SQLException {
		Statement statement = null;
		
		statement = connection.createStatement();

		return statement;
	}
	public static void query(String query) {
		Connection connection = null;
		Statement statement = null;		
		
		try {
			
		connection = connect();
		statement = statement(connection);
		ResultSet resultset = statement.executeQuery(query);
		
		while(resultset.next()) {
			//Retrieve and return from resultset
		}
		connection.close();
		statement.close();
		}catch (SQLException e) {
			//TODO: handle/log exception
		}catch (ClassNotFoundException e) {
			//TODO: handle/log exception
		}
		_makeSureConnectionAndStatmentAreClosed(connection, statement);
	}
	private static void _makeSureConnectionAndStatmentAreClosed(Connection connection, Statement statement) {
		try {
			if(Optional.ofNullable(connection).isPresent())
				connection.close();
			if(Optional.ofNullable(statement).isPresent())
				statement.close();
			} catch (SQLException e) {
				//TODO: handle/log exception
			}
	}
	
}
