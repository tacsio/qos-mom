package database;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Logger;

public class SqliteDatabase {

	private Connection connection;

	@SuppressWarnings("unused")
	private static Logger logger = Logger.getLogger(SqliteDatabase.class
			.getName());

	public void insertQosData(String metric, String source, double value, long timestamp) {
		
		this.connection = ConnectionFactory.getConnection();

		String query = "INSERT INTO qos(metric, source, value, timestamp) values (?, ?, ?, ?)";

		PreparedStatement stmt = null;
		int  i = 0;

		try {
			stmt = this.connection.prepareStatement(query);
			stmt.setString(++i, metric);
			stmt.setString(++i, source);
			stmt.setDouble(++i, value);
			stmt.setDate(++i, new Date(timestamp));
			stmt.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				this.connection.close();
				stmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
	}
}
