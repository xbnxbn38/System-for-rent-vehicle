package model.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import model.entity.Car;
import model.util.JDBCUtil;

public class CarDao {
	private static Connection connection = JDBCUtil.getConnection();

	public static double findPerDayFeeByVehicleId(String vehicleId) {
		Statement statement = null;
		double perDayFee=0;
		try {
			statement=connection.createStatement();
			String sql="SELECT per_day_fee FROM car WHERE vehicle_id='"+vehicleId+"'";
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
	
	public static List<Car> getAllCar(){
		ArrayList<Car> cars=new ArrayList<>();
		Statement statement = null;
		try {
			statement=connection.createStatement();
			String sql="SELECT * FROM car";
			ResultSet resultSet=statement.executeQuery(sql);
			while (resultSet.next()) {
				Car car=new Car();
				car.setVehicleId(resultSet.getString(1));
				car.setPerDayFee(resultSet.getDouble(2));
				cars.add(car);
			}
			resultSet.close();
		} catch (SQLException e) {
			e.printStackTrace();
			return cars;
		}finally {
			if(statement!=null) {
				try {
					statement.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return cars;
	}
	
	public static void saveAll(List<Car> cars) throws Exception {
		Statement statement=null;
		try {
			statement=connection.createStatement();
			String sql="INSERT INTO car VALUES ";
			StringBuffer temp=new StringBuffer();
			for (Car car : cars) {
				temp.append("('"+car.getVehicleId()+"','"+car.getPerDayFee()+"'),");
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
	
	public static void save(Car car) throws Exception {
		Statement statement=null;
		try {
			statement=connection.createStatement();
			String sql="INSERT INTO car VALUES ";
			StringBuffer temp=new StringBuffer();
	
				temp.append("('"+car.getVehicleId()+"','"+car.getPerDayFee()+"'),");
			
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
