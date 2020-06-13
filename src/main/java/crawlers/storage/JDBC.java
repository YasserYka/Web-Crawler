package crawlers.storage;


import java.sql.Connection;
import java.sql.Statement;
import java.util.Optional;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JDBC {

	private static final String INSERT_LINK_QUERY = "INSERT INTO links(id, link) VALUES (?,?)";
	private static final Logger logger = LoggerFactory.getLogger(JDBC.class);
	private static final String JDBC_DRIVER = "org.postgresql.Driver";
	private static final String DATABASE_URL = "jdbc:postgresql://localhost:5432/crawlers";
	//TODO: Find secure way instead of hardcoding file prop maybe?
	private static final String USERNAME = "user";
	private static final String PASSWORD = "pass";
	
	private static Connection connect() throws ClassNotFoundException, SQLException {		
		Class.forName(JDBC_DRIVER);
		return DriverManager.getConnection(DATABASE_URL, USERNAME, PASSWORD);
	}
	
	private static Statement statement(Connection connection) throws SQLException {
		return connection.createStatement();
	}
	
	public static void query(String query) {
		Connection connection = null;
		Statement statement = null;		
		
		try {
				
			connection = connect();
			statement = statement(connection);
			ResultSet resultset = statement.executeQuery(query);
			
			while(resultset.next()) {
				logger.info("Excuted query's result {}", resultset.getString(2));
			}
			
			connection.close();
			statement.close();
		
		} catch (SQLException e) { logger.error("SQLException triggered! {}", e); }
		catch (ClassNotFoundException e) { logger.error("ClassNotFoundException triggered at query method! {}", e); }
		
		_makeSureConnectionAndStatmentAreClosed(connection, statement);
	}
	
	private static void _makeSureConnectionAndStatmentAreClosed(Connection connection, Statement statement) {
		try {
			if(Optional.ofNullable(connection).isPresent())
				connection.close();
			if(Optional.ofNullable(statement).isPresent())
				statement.close();
		} catch (SQLException e) {
			logger.error("SQLException triggered at _makeSureCo.. method! {}", e); }
	}
	
}
