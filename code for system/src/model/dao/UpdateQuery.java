package model.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import model.util.JDBCUtil;

public class UpdateQuery 
{
	private static Connection connection = JDBCUtil.getConnection();

	public static void main(String[] args) {
		Statement statement = null;

		try {
			statement = connection.createStatement();
			
			String Query = "delete from apartment where vehicle_id LIKE 'C_2010ToyotaColla'";

			
			  statement.executeUpdate(Query);

			//resultSet.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	

	
}
