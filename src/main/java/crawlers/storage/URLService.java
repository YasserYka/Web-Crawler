package crawlers.storage;

import java.sql.Connection;
import java.sql.Date;
import java.util.Optional;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import crawlers.models.URL;
import crawlers.util.JDBCBean;
import lombok.SneakyThrows;

public class URLService {

	private static final String INSERT_URL_QUERY = "INSERT INTO URLs(link, createdAt) VALUES (?,?)";
	private static final String SELECT_ALL_URLS_QUERY = "SELECT * FROM URLs";
	private static final Logger logger = LoggerFactory.getLogger(URLService.class);
	private Connection connection;

	public URLService() throws ClassNotFoundException, SQLException {
		connection = JDBCBean.getConnection();
	}

	@SneakyThrows(SQLException.class)
	public void add(URL url) {
		PreparedStatement preparedStatement = connection.prepareStatement(INSERT_URL_QUERY);
		preparedStatement.setString(1, url.getUrl());
		preparedStatement.setDate(2, new Date(System.currentTimeMillis()));
		preparedStatement.executeUpdate();
		preparedStatement.close();
	}
	
	@SneakyThrows(SQLException.class)
	public void getAll(String query) {		
		PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_URLS_QUERY);
		
		ResultSet resultset = preparedStatement.executeQuery(query);
		
		while(resultset.next())
			logger.info("Excuted query's result {} {} {}", resultset.getInt(1), resultset.getString(2), resultset.getDate(3));
		
		preparedStatement.close();
	}
	
	@SneakyThrows(SQLException.class)
	public void closeConnection() {
		if(Optional.ofNullable(connection).isPresent())
			connection.close();
	}
	
}
