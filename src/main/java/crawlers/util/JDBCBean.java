package crawlers.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.stream.IntStream;

public class JDBCBean {
    
	private static final String DATABASE_URL = "jdbc:postgresql://localhost:5432/crawlers";
	private static final String USERNAME = "user";
	private static final String PASSWORD = "pass";

	// to achieve connection pooling <Connection, IsUsed>
	private static final HashMap<Connection, Boolean> CONNECTION_POOL;
    private static final int POOL_SIZE = 10;

	// pre-warming database connections 
    static {
    
		CONNECTION_POOL = new HashMap<Connection, Boolean>();

		IntStream.range(1, POOL_SIZE).forEach(i -> {
			try { CONNECTION_POOL.put(DriverManager.getConnection(DATABASE_URL, USERNAME, PASSWORD), false); }
				catch (SQLException e) { e.printStackTrace(); }
		});
	}

}