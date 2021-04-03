package crawlers.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import lombok.SneakyThrows;

public class JDBCBean {
    
	private static final String DATABASE_URL = "jdbc:postgresql://localhost:5432/crawlers";
	private static final String USERNAME = "user";
	private static final String PASSWORD = "pass";

	// for connection pooling
	private final List<Connection> connectionPool;
	
    private static final int POOL_SIZE = 10;

	// pre-warming database connections
	public JDBCBean() {
		connectionPool = new ArrayList<Connection>();

		IntStream.range(0, POOL_SIZE).forEach(i -> addConnection());
	}
 
	@SneakyThrows(SQLException.class)
	public void addConnection(){

		connectionPool.add(DriverManager.getConnection(DATABASE_URL, USERNAME, PASSWORD));
	}

	public Connection getConnection(){

		if (connectionPool.isEmpty())
			throw new RuntimeException("Reached maximum database connection pool size!");

		return connectionPool.remove(0);
	}

	public void realseConnection(Connection connection){

		connectionPool.add(connection);
	}

}