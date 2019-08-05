package model.util;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class CreateTable 
{
	public static void main(String[] args) throws SQLException {	
		//final String DB_NAME = "vehicleDB";
		
		
		try (Connection con = JDBCUtil.getConnection();
				Statement stmt = con.createStatement();
		) {	
			
			stmt.executeUpdate("DROP TABLE rental_vehicle");
			stmt.executeUpdate("DROP TABLE car");
			stmt.executeUpdate("DROP TABLE van");
			stmt.executeUpdate("DROP TABLE rental_record");
			
			stmt.executeUpdate("CREATE TABLE rental_vehicle (" + "vehicle_id text NOT NULL," + "manu_year text,"
					+ "make text," + "model text," + "seats_number INTEGER," + "vehicle_type text,"
					+ "vehicle_status text," + "image_name text," + "description text," + "create_time text,"
					+ "PRIMARY KEY (vehicle_id))");
			
			stmt.executeUpdate("CREATE TABLE car (" + "vehicle_id real NOT NULL," + "per_day_fee real,"
					+ "PRIMARY KEY (vehicle_id))");

			stmt.executeUpdate("CREATE TABLE van (" + "vehicle_id real NOT NULL,"
					+ "last_maintenance_date text," + "per_day_fee real," + "PRIMARY KEY (vehicle_id))");

			stmt.executeUpdate("CREATE TABLE rental_record (" + "record_id text NOT NULL," + "vehicle_id text,"
					+ "customer_id text," + "rent_date text," + "estimated_return_date text,"
					+ "actual_return_date text," + "rental_fee real," + "late_fee real," + "PRIMARY KEY (record_id))");
			
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}
}
