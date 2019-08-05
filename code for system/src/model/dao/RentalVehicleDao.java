package model.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import model.constant.VehicleType;
import model.entity.Car;
import model.entity.Van;
import model.entity.RentalVehicle;
import model.exception.PerformMaintenanceException;
import model.util.DateTime;
import model.util.JDBCUtil;

public class RentalVehicleDao {
	private static Connection connection = JDBCUtil.getConnection();

	public static List<RentalVehicle> getAllRentalVehicle() {
		ArrayList<RentalVehicle> rentalVehicles = new ArrayList<>();
		Statement statement = null;

		try {
			statement = connection.createStatement();
			String sql = "SELECT * FROM rental_vehicle order by create_time desc";

			ResultSet resultSet = statement.executeQuery(sql);
			while (resultSet.next()) {
				RentalVehicle rentalVehicle = null;
				if (resultSet.getString(6).equals(VehicleType.CAR)) {
					rentalVehicle = new Car();
					rentalVehicle.setVehicleId(resultSet.getString(1));
					rentalVehicle.setManuY(resultSet.getString(2));
					rentalVehicle.setMake(resultSet.getString(3));
					rentalVehicle.setModel(resultSet.getString(4));
					rentalVehicle.setSeatsNum(resultSet.getInt(5));
					rentalVehicle.setVehicleType(VehicleType.CAR);
					rentalVehicle.setStatus(resultSet.getString(7));
					rentalVehicle.setImageName(resultSet.getString(8));
					rentalVehicle.setDescription(resultSet.getString(9));

					((Car) rentalVehicle)
							.setPerDayFee(CarDao.findPerDayFeeByVehicleId(rentalVehicle.getVehicleId()));
				} else if (resultSet.getString(6).equals(VehicleType.VAN)) {
					rentalVehicle = new Van();
					rentalVehicle.setVehicleId(resultSet.getString(1));
					rentalVehicle.setManuY(resultSet.getString(2));
					rentalVehicle.setMake(resultSet.getString(3));
					rentalVehicle.setModel(resultSet.getString(4));
					rentalVehicle.setSeatsNum(resultSet.getInt(5));
					rentalVehicle.setVehicleType(VehicleType.VAN);
					rentalVehicle.setStatus(resultSet.getString(7));
					rentalVehicle.setImageName(resultSet.getString(8));
					rentalVehicle.setDescription(resultSet.getString(9));

					((Van) rentalVehicle)
							.setPerDayFee(VanDao.findPerDayFeeByVehicleId(rentalVehicle.getVehicleId()));
					((Van) rentalVehicle).setLastMaintenance(
							VanDao.findLastMaintenanceByVehicleId(rentalVehicle.getVehicleId()));
				}
				rentalVehicles.add(rentalVehicle);
			}

			resultSet.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return rentalVehicles;
	}

	public static List<Car> getAllCar() {
		ArrayList<Car> cars = new ArrayList<>();
		Statement statement = null;
		try {
			statement = connection.createStatement();
			String sql = "SELECT " + "rental_vehicle.vehicle_id,manu_year,make,model,vehicle_status,"
					+ "seats_number,vehicle_type,image_name,description,per_day_fee "
					+ "FROM rental_vehicle,car " + "where rental_vehicle.vehicle_id=car.vehicle_id";
			ResultSet resultSet = statement.executeQuery(sql);

			while (resultSet.next()) {
				Car car = new Car();
				car.setVehicleId(resultSet.getString("vehicle_id"));
				car.setManuY(resultSet.getString("manu_year"));
				car.setMake(resultSet.getString("make"));
				car.setModel(resultSet.getString("model"));
				car.setVehicleType(resultSet.getString("vehicle_type"));
				car.setStatus(resultSet.getString("vehicle_status"));
				car.setSeatsNum(resultSet.getInt("seats_number"));
				car.setImageName(resultSet.getString("image_name"));
				car.setDescription(resultSet.getString("description"));
				car.setPerDayFee(resultSet.getDouble("per_day_fee"));

				cars.add(car);
			}
			resultSet.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (statement != null) {
				try {
					statement.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}

		return cars;
	}

	public static List<Van> getAllVan() {
		ArrayList<Van> vans = new ArrayList<>();
		Statement statement = null;
		try {
			statement = connection.createStatement();
			String sql = "SELECT " + "rental_vehicle.vehicle_id,manu_year,make,model,vehicle_status,"
					+ "seats_number,vehicle_type,image_name,description,per_day_fee,last_maintenance_date "
					+ "FROM rental_vehicle,van "
					+ "where rental_vehicle.vehicle_id=van.vehicle_id";
			ResultSet resultSet = statement.executeQuery(sql);

			while (resultSet.next()) {
				Van van = new Van();

				van.setVehicleId(resultSet.getString("vehicle_id"));
				van.setManuY(resultSet.getString("manu_year"));
				van.setMake(resultSet.getString("make"));
				van.setModel(resultSet.getString("model"));
				van.setVehicleType(resultSet.getString("vehicle_type"));
				van.setStatus(resultSet.getString("vehicle_status"));
				van.setSeatsNum(resultSet.getInt("seats_number"));
				van.setImageName(resultSet.getString("image_name"));
				van.setDescription(resultSet.getString("description"));
				van.setPerDayFee(resultSet.getDouble("per_day_fee"));
				van.setLastMaintenance(new DateTime(resultSet.getString("last_maintenance_date")));

				vans.add(van);
			}
			resultSet.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (statement != null) {
				try {
					statement.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}

		return vans;
	}

	public static RentalVehicle findById(String vehicleId) {
		Statement statement = null;
		RentalVehicle rentalVehicle = null;
		try {
			statement = connection.createStatement();
			String sql = "SELECT * FROM rental_vehicle where vehicle_id='" + vehicleId + "'";
			ResultSet resultSet = statement.executeQuery(sql);
			while (resultSet.next()) {
				if (resultSet.getString(6).equals(VehicleType.CAR)) {
					rentalVehicle = new Car();
					rentalVehicle.setVehicleId(resultSet.getString(1));
					rentalVehicle.setManuY(resultSet.getString(2));
					rentalVehicle.setMake(resultSet.getString(3));
					rentalVehicle.setModel(resultSet.getString(4));
					rentalVehicle.setSeatsNum(resultSet.getInt(5));
					rentalVehicle.setVehicleType(VehicleType.CAR);
					rentalVehicle.setStatus(resultSet.getString(7));
					rentalVehicle.setImageName(resultSet.getString(8));
					rentalVehicle.setDescription(resultSet.getString(9));

					((Car) rentalVehicle)
							.setPerDayFee(CarDao.findPerDayFeeByVehicleId(rentalVehicle.getVehicleId()));
				} else if (resultSet.getString(6).equals(VehicleType.VAN)) {
					rentalVehicle = new Van();
					rentalVehicle.setVehicleId(resultSet.getString(1));
					rentalVehicle.setManuY(resultSet.getString(2));
					rentalVehicle.setMake(resultSet.getString(3));
					rentalVehicle.setModel(resultSet.getString(4));
					rentalVehicle.setSeatsNum(resultSet.getInt(5));
					rentalVehicle.setVehicleType(VehicleType.VAN);
					rentalVehicle.setStatus(resultSet.getString(7));
					rentalVehicle.setImageName(resultSet.getString(8));
					rentalVehicle.setDescription(resultSet.getString(9));

					((Van) rentalVehicle)
							.setPerDayFee(VanDao.findPerDayFeeByVehicleId(rentalVehicle.getVehicleId()));
				}
			}
			resultSet.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (statement != null) {
				try {
					statement.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return rentalVehicle;
	}

	public static Boolean findByAddress(int manuYear, String make, String model) {
		Statement statement = null;
		
		try {
			statement = connection.createStatement();
			String sql = "SELECT * FROM rental_vehicle where manu_year='" + manuYear + "' and make='"+make+"' and model='"+model+"'";
			ResultSet resultSet = statement.executeQuery(sql);
			if (resultSet.next()) {
				resultSet.close();
				return true;
			}
			else {
				resultSet.close();
				return false;
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (statement != null) {
				try {
					statement.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return false;
	}
	
	public static void updateStatus(RentalVehicle rentalVehicle) throws PerformMaintenanceException {
		Statement statement = null;
		try {
			statement = connection.createStatement();
			String sql = "UPDATE rental_vehicle SET " + "vehicle_status='" + rentalVehicle.getStatus() + "' "
					+ "WHERE vehicle_id='" + rentalVehicle.getVehicleId() + "'";
			if (statement.executeUpdate(sql) != 1) {
				throw new SQLException("updating rent record into table rental_record failed;");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new PerformMaintenanceException("sql error:" + e.getMessage());
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

	public static List<RentalVehicle> getAllRentalVehicleForExport() {
		ArrayList<RentalVehicle> rentalVehicles = new ArrayList<>();
		Statement statement = null;

		try {
			statement = connection.createStatement();
			String sql = "SELECT * FROM rental_vehicle";

			ResultSet resultSet = statement.executeQuery(sql);
			while (resultSet.next()) {
				RentalVehicle rentalVehicle = null;
				if (resultSet.getString(6).equals(VehicleType.CAR)) {
					rentalVehicle = new Car();
					rentalVehicle.setVehicleId(resultSet.getString(1));
					rentalVehicle.setManuY(resultSet.getString(2));
					rentalVehicle.setMake(resultSet.getString(3));
					rentalVehicle.setModel(resultSet.getString(4));
					rentalVehicle.setSeatsNum(resultSet.getInt(5));
					rentalVehicle.setVehicleType(VehicleType.CAR);
					rentalVehicle.setStatus(resultSet.getString(7));
					rentalVehicle.setImageName(resultSet.getString(8));
					rentalVehicle.setDescription(resultSet.getString(9));
					rentalVehicle.setCreateTime(resultSet.getString(10));
				} else if (resultSet.getString(6).equals(VehicleType.VAN)) {
					rentalVehicle = new Van();
					rentalVehicle.setVehicleId(resultSet.getString(1));
					rentalVehicle.setManuY(resultSet.getString(2));
					rentalVehicle.setMake(resultSet.getString(3));
					rentalVehicle.setModel(resultSet.getString(4));
					rentalVehicle.setSeatsNum(resultSet.getInt(5));
					rentalVehicle.setVehicleType(VehicleType.VAN);
					rentalVehicle.setStatus(resultSet.getString(7));
					rentalVehicle.setImageName(resultSet.getString(8));
					rentalVehicle.setDescription(resultSet.getString(9));
					rentalVehicle.setCreateTime(resultSet.getString(10));
				}
				rentalVehicles.add(rentalVehicle);
			}

			resultSet.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return rentalVehicles;
	}

	public static void saveAll(List<RentalVehicle> rentalVehicles) throws Exception {
		Statement statement = null;
		try {
			statement = connection.createStatement();
			String sql = "INSERT INTO rental_vehicle VALUES ";
			StringBuffer temp = new StringBuffer();
			for (RentalVehicle rentalVehicle : rentalVehicles) {
				temp.append("(");
				temp.append(String.format("'%s',", rentalVehicle.getVehicleId()));
				temp.append(String.format("'%s',", rentalVehicle.getManuY()));
				temp.append(String.format("'%s',", rentalVehicle.getMake()));
				temp.append(String.format("'%s',", rentalVehicle.getModel()));
				temp.append(String.format("'%d',", rentalVehicle.getSeatsNum()));
				temp.append(String.format("'%s',", rentalVehicle.getVehicleType()));
				temp.append(String.format("'%s',", rentalVehicle.getStatus()));
				temp.append(String.format("'%s',", rentalVehicle.getImageName()));
				temp.append(String.format("'%s',", rentalVehicle.getDescription()));
				temp.append(String.format("'%s'", rentalVehicle.getCreateTime()));
				temp.append("),");
			}
			temp.delete(temp.length() - 1, temp.length());
			sql = sql + temp.toString();
			statement.execute(sql);
		} catch (Exception e) {
			throw e;
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

	public static void save(RentalVehicle rentalVehicle) throws Exception {
		Statement statement = null;
		try {
			statement = connection.createStatement();
			String sql = "INSERT INTO rental_vehicle VALUES ";
			StringBuffer temp = new StringBuffer();
				temp.append("(");
				temp.append(String.format("'%s',", rentalVehicle.getVehicleId()));
				temp.append(String.format("'%s',", rentalVehicle.getManuY()));
				temp.append(String.format("'%s',", rentalVehicle.getMake()));
				temp.append(String.format("'%s',", rentalVehicle.getModel()));
				temp.append(String.format("'%d',", rentalVehicle.getSeatsNum()));
				temp.append(String.format("'%s',", rentalVehicle.getVehicleType()));
				temp.append(String.format("'%s',", rentalVehicle.getStatus()));
				temp.append(String.format("'%s',", rentalVehicle.getImageName()));
				temp.append(String.format("'%s',", rentalVehicle.getDescription()));
				temp.append(String.format("'%s'", rentalVehicle.getCreateTime()));
				temp.append("),");
			
			temp.delete(temp.length() - 1, temp.length());
			sql = sql + temp.toString();
			statement.execute(sql);
		} catch (Exception e) {
			throw e;
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

	
	public static List<RentalVehicle> getAllRentalVehicleWithConditon(String condition) {
		ArrayList<RentalVehicle> rentalVehicles = new ArrayList<>();
		Statement statement = null;

		try {
			statement = connection.createStatement();
			String sql = "SELECT * FROM rental_vehicle WHERE " + condition + " ORDER BY create_time DESC";

			ResultSet resultSet = statement.executeQuery(sql);
			while (resultSet.next()) {
				RentalVehicle rentalVehicle = null;
				if (resultSet.getString(6).equals(VehicleType.CAR)) {
					rentalVehicle = new Car();
					rentalVehicle.setVehicleId(resultSet.getString(1));
					rentalVehicle.setManuY(resultSet.getString(2));
					rentalVehicle.setMake(resultSet.getString(3));
					rentalVehicle.setModel(resultSet.getString(4));
					rentalVehicle.setSeatsNum(resultSet.getInt(5));
					rentalVehicle.setVehicleType(VehicleType.CAR);
					rentalVehicle.setStatus(resultSet.getString(7));
					rentalVehicle.setImageName(resultSet.getString(8));
					rentalVehicle.setDescription(resultSet.getString(9));

					((Car) rentalVehicle)
							.setPerDayFee(CarDao.findPerDayFeeByVehicleId(rentalVehicle.getVehicleId()));
				} else if (resultSet.getString(6).equals(VehicleType.VAN)) {
					rentalVehicle = new Van();
					rentalVehicle.setVehicleId(resultSet.getString(1));
					rentalVehicle.setManuY(resultSet.getString(2));
					rentalVehicle.setMake(resultSet.getString(3));
					rentalVehicle.setModel(resultSet.getString(4));
					rentalVehicle.setSeatsNum(resultSet.getInt(5));
					rentalVehicle.setVehicleType(VehicleType.VAN);
					rentalVehicle.setStatus(resultSet.getString(7));
					rentalVehicle.setImageName(resultSet.getString(8));
					rentalVehicle.setDescription(resultSet.getString(9));

					((Van) rentalVehicle)
							.setPerDayFee(VanDao.findPerDayFeeByVehicleId(rentalVehicle.getVehicleId()));
					((Van) rentalVehicle).setLastMaintenance(
							VanDao.findLastMaintenanceByVehicleId(rentalVehicle.getVehicleId()));
				}
				rentalVehicles.add(rentalVehicle);
			}

			resultSet.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return rentalVehicles;
	}
}
