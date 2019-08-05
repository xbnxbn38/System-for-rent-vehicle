package model.entity;

import model.constant.VehicleStatus;
import model.constant.VehicleType;
import model.dao.RentalVehicleDao;
import model.dao.RentalRecordDao;
import model.exception.CompleteMaintenanceException;
import model.exception.PerformMaintenanceException;
import model.exception.RentException;
import model.exception.ReturnException;
import model.util.DateTime;

public class Car extends RentalVehicle {

	private double perDayFee;
	private double latePerDayFee;
	
	public Car() {
	}

	public Car(String vehicleId, String manuY, String make, String model, String vehicleType,
			int seatNum, String des, String img) {
		this.vehicleId = "Car".charAt(0) + "_" + manuY + make + model;
		this.manuY = manuY;
		this.make = make;
		this.model = model;
		this.vehicleType = VehicleType.CAR;
		this.seatsNum = seatNum;
		status = VehicleStatus.CarStatus.AVAILABLE;
		super.setDescription(des);
		super.setImageName(img);
	}

	public double getPerDayFee() {
		return perDayFee;
	}

	public void setPerDayFee(double perDayFee) {
		this.perDayFee = perDayFee;
		this.latePerDayFee=1.25*perDayFee;
	}

	public double getLatePerDayFee() {
		return latePerDayFee;
	}

	public void setLatePerDayFee(double latePerDayFee) {
		this.latePerDayFee = latePerDayFee;
	}

	public void rent(String customerId, DateTime rentDate, DateTime estimatedReturnDate) throws RentException, PerformMaintenanceException {
		if (rentDate.getTime() >= new DateTime().getTime() && status.equals(VehicleStatus.CarStatus.AVAILABLE)) {
			int diffDays=DateTime.diffDays(estimatedReturnDate, rentDate);
			if (rentDate.DayOfWeek().equals("Friday") || rentDate.DayOfWeek().equals("Saturday")) {
				if (diffDays >= 3 && diffDays <= 14) {
					status = VehicleStatus.CarStatus.BEING_RENTED;
					RentalRecord latestRecord=new RentalRecord(this,customerId,rentDate,estimatedReturnDate);
					
					RentalRecordDao.save(latestRecord);
					RentalVehicleDao.updateStatus(this);
				} else {
					throw new RentException(1);
				}
			} else {
				if (diffDays >= 2 && diffDays <= 14) {
					status = VehicleStatus.CarStatus.BEING_RENTED;
					RentalRecord latestRecord=new RentalRecord(this, customerId, rentDate,estimatedReturnDate);
					
					RentalRecordDao.save(latestRecord);
					RentalVehicleDao.updateStatus(this);
				} else {
					throw new RentException(2);
				}
			}
		} else {
			throw new RentException(3);
		}
	}

	public void returnVehicle(DateTime returnDate) throws ReturnException, PerformMaintenanceException {
		RentalRecord latestRecord=RentalRecordDao.findLatestRecordByVehicle(this);
		if (returnDate.getTime() > latestRecord.getRentDate().getTime() && status.equals(VehicleStatus.CarStatus.BEING_RENTED)) {
			latestRecord.setActualReturnDateAndFees(returnDate);
			status = VehicleStatus.CarStatus.AVAILABLE;
			
			RentalRecordDao.update(latestRecord);
			RentalVehicleDao.updateStatus(this);
		} else {
			throw new ReturnException();
		}
	}

	public void performMaintenance() throws PerformMaintenanceException {
		if (status.equals(VehicleStatus.CarStatus.BEING_RENTED)) {
			throw new PerformMaintenanceException();
		} else {
			status = VehicleStatus.CarStatus.UNDER_MAINTENANCE;
			RentalVehicleDao.updateStatus(this);
		}
	}

	public void completeMaintenance(DateTime completeDate) throws CompleteMaintenanceException, PerformMaintenanceException {
		if (status.equals(VehicleStatus.CarStatus.UNDER_MAINTENANCE)) {
			status = VehicleStatus.AVAILABLE;
			RentalVehicleDao.updateStatus(this);
		} else {
			throw new CompleteMaintenanceException("this Vehicle isn't under maintenance.");
		}
	}

	public String getDetails() {
		return this.getVehicleId()+":"+this.getManuY()+":"+this.getMake()+":"+this.getModel()+":"+this.getSeatsNum()+":"
				+this.getVehicleType()+":"+this.getStatus()+":"+this.getImageName()+":"+this.getDescription();
	}

}
