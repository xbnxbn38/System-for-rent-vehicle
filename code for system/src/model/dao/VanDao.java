package model.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import model.entity.Van;
import model.exception.CompleteMaintenanceException;
import model.util.DateTime;
import model.util.JDBCUtil;

public class VanDao {
	private static Connection connection = JDBCUtil.getConnection();
	
	public static double findPerDayFeeByVehicleId(String vehicleId) {
		Statement statement = null;
		double perDayFee=0;
		try {
			statement=connection.createStatement();
			String sql="SELECT per_day_fee FROM van WHERE vehicle_id='"+vehicleId+"'";
			ResultSet resultSet=statement.executeQuery(sql);
			while (resultSet.next()) {
				perDayFee=resultSet.getDouble(1);
			}
			resultSet.close();
		} catch (SQLException e) {
			e.printStackTrace();
			return perDayFee;
		}finally {
			if(statement!=null) {
				try {
					statement.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return perDayFee;
	}

	public static void updateLastMaintenance(Van van) throws CompleteMaintenanceException {
		Statement statement = null;
		try {
			statement = connection.createStatement();
			String sql = "UPDATE van SET "
					+ "last_maintenance_date='"+van.getLastMaintenance().getEightDigitDate()+"' "
					+ "WHERE vehicle_id='"+van.getVehicleId()+"'";
			if (statement.executeUpdate(sql) != 1) {
				throw new SQLException("updating rent record into table rental_record failed;");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new CompleteMaintenanceException("sql error:"+e.getMessage());
		} finally {
			if (statement != null) {
				try {
					statement.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}		
	}

	public static DateTime findLastMaintenanceByVehicleId(String vehicleId) {
		Statement statement = null;
		DateTime dateTime=null;
		try {
			statement=connection.createStatement();
			String sql="SELECT last_maintenance_date FROM van WHERE vehicle_id='"+vehicleId+"'";
			ResultSet resultSet=statement.executeQuery(sql);
			while (resultSet.next()) {
				dateTime=new DateTime(resultSet.getString(1));
			}
			resultSet.close();
		} catch (SQLException e) {
			e.printStackTrace();
			return dateTime;
		}finally {
			if(statement!=null) {
				try {
					statement.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return dateTime;
	}
	
	public static List<Van> getAllVan(){
		ArrayList<Van> vans=new ArrayList<>();
		Statement statement = null;
		try {
			statement=connection.createStatement();
			String sql="SELECT * FROM van";
			ResultSet resultSet=statement.executeQuery(sql);
			while (resultSet.next()) {
				Van van=new Van();
				van.setVehicleId(resultSet.getString(1));
				van.setLastMaintenance(new DateTime(resultSet.getString(2)));
				van.setPerDayFee(resultSet.getDouble(3));
				vans.add(van);
			}
			resultSet.close();
		} catch (SQLException e) {
			e.printStackTrace();
			return vans;
		}finally {
			if(statement!=null) {
				try {
					statement.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return vans;
	}

	public static void saveAll(List<Van> vans) throws Exception {
		Statement statement=null;
		try {
			statement=connection.createStatement();
			String sql="INSERT INTO van VALUES ";
			StringBuffer temp=new StringBuffer();
			for (Van van : vans) {
				temp.append("('"+van.getVehicleId()+"','"+van.getLastMaintenance().getEightDigitDate()+"','"+van.getPerDayFee()+"'),");
			}
			temp.delete(temp.length()-1, temp.length());
			sql=sql+temp.toString();
			statement.execute(sql);
		} catch (Exception e) {
			throw e;
		}finally {
			if(statement!=null) {
				try {
					statement.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}
		
		public static void save(Van van) throws Exception {
			Statement statement=null;
			try {
				statement=connection.createStatement();
				String sql="INSERT INTO van VALUES ";
				StringBuffer temp=new StringBuffer();
			    temp.append("('"+van.getVehicleId()+"','"+van.getLastMaintenance().getEightDigitDate()+"','"+van.getPerDayFee()+"'),");
				
				temp.delete(temp.length()-1, temp.length());
				sql=sql+temp.toString();
				statement.execute(sql);
			} catch (Exception e) {
				throw e;
			}finally {
				if(statement!=null) {
					try {
						statement.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			}
	}
}
