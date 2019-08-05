package model.entity;

import model.constant.VehicleStatus;
import model.dao.VanDao;
import model.dao.RentalVehicleDao;
import model.dao.RentalRecordDao;
import model.exception.CompleteMaintenanceException;
import model.exception.PerformMaintenanceException;
import model.exception.RentException;
import model.exception.ReturnException;
import model.util.DateTime;

public class Van extends RentalVehicle {
	private DateTime lastMaintenance;
	private double perDayFee;
    
	public Van() {
		
	}

	public Van(String vehicleId, String manuY, String make, String model, String vehicleType,
			DateTime lastMaintenance, String des, String img) {
		this.vehicleId = vehicleId;
		this.manuY = manuY;
		this.make = make;
		this.model = model;
		this.vehicleType = vehicleType;
		this.lastMaintenance = lastMaintenance;
		this.vehicleId = "V_" + manuY + make + model;
		super.setDescription(des);
		super.setImageName(img);
		status = VehicleStatus.AVAILABLE;
		perDayFee = 235;
	}	
	
	public DateTime getLastMaintenance() {
		return lastMaintenance;
	}

	public void setLastMaintenance(DateTime lastMaintenance) {
		this.lastMaintenance = lastMaintenance;
	}

	public double getPerDayFee() {
		return perDayFee;
	}

	public void setPerDayFee(double perDayFee) {
		this.perDayFee = perDayFee;
	}

	public void rent(String customerId, DateTime rentDate, DateTime estimatedReturnDate) throws RentException, PerformMaintenanceException {
		if (status.equals(VehicleStatus.VanStatus.BEING_RENTED) || status.equals(VehicleStatus.VanStatus.UNDER_MAINTENANCE)) {
			throw new RentException(3);
		}
		if (rentDate.getTime()>=new DateTime().getTime()&&status.equals(VehicleStatus.AVAILABLE)
				&& DateTime.diffDays(estimatedReturnDate, rentDate) < DateTime.diffDays((new DateTime(lastMaintenance, 10)), lastMaintenance)) {
			status = VehicleStatus.VanStatus.BEING_RENTED;
			RentalRecord record=new RentalRecord(this, customerId, rentDate,estimatedReturnDate);
			
			RentalRecordDao.save(record);
			RentalVehicleDao.updateStatus(this);
		}else {
			throw new RentException(0);
		}

	}

	public void returnVehicle(DateTime returnDate) throws ReturnException, PerformMaintenanceException {
		RentalRecord record=RentalRecordDao.findLatestRecordByVehicle(this);
		if (returnDate.getTime() < record.getRentDate().getTime()) {
			throw new ReturnException();
		} else {
			status = VehicleStatus.AVAILABLE;
			record.setActualReturnDateAndFees(returnDate);
			
			RentalRecordDao.update(record);
			RentalVehicleDao.updateStatus(this);
		}
	}

	public void performMaintenance() throws PerformMaintenanceException {
		if (status.equals(VehicleStatus.VanStatus.BEING_RENTED)) {
			throw new PerformMaintenanceException();
		} else {
			status = VehicleStatus.VanStatus.UNDER_MAINTENANCE;
			RentalVehicleDao.updateStatus(this);
		}
	}

	public void completeMaintenance(DateTime completeDate) throws CompleteMaintenanceException, PerformMaintenanceException {
		if (status.equals(VehicleStatus.VanStatus.UNDER_MAINTENANCE)) {
			status = VehicleStatus.AVAILABLE;
			lastMaintenance = completeDate;
			VanDao.updateLastMaintenance(this);
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
