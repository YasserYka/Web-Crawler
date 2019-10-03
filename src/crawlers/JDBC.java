package crawlers;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

public class JDBC {

	private static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	private static final String DATABASE_URL = "jdbc:mysql://localhost/EMP";
	//TODO: Find secure way instead of hardcoding file prop maybe?
	private final String username = null;
	private final String password = null;
	
	public Connection connect() throws ClassNotFoundException, SQLException {
		Connection connection = null;
		
		Class.forName("com.mysql.jdbc.Driver");
		connection = DriverManager.getConnection(DATABASE_URL, username, password);
		
		return connection;
	}
	
	public Statement statement(Connection connection) throws SQLException {
		Statement statement = null;
		
		statement = connection.createStatement();

		return statement;
	}
	public void query(String query) {
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
	}
	
}
