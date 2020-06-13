package crawlers.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class JDBCBean {
    
	private static final String JDBC_DRIVER = "org.postgresql.Driver";
	private static final String DATABASE_URL = "jdbc:postgresql://localhost:5432/crawlers";
	//TODO: Find secure way instead of hardcoding file prop maybe?
	private static final String USERNAME = "user";
	private static final String PASSWORD = "pass";
    	
	public static Connection getConnection() throws ClassNotFoundException, SQLException {		
		Class.forName(JDBC_DRIVER);
		return DriverManager.getConnection(DATABASE_URL, USERNAME, PASSWORD);
    }
}