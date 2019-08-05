package assignmet1;

import java.text.SimpleDateFormat;

class Van extends Vehicle {

	private double vanRate = 235.0;
	private double vanLateRate = 299.0;
	private DateTime maintenanceDate;

	public Van() {	
	}
	//constructor
	public Van(String vehicleID, String manuYear, String make, String model, String vehicleType,
			DateTime maintenanceDate) {
		super(vehicleID, manuYear, make, model, 15, vehicleType);
		this.maintenanceDate = maintenanceDate;
		DateTime current = new DateTime();
		if (DateTime.diffDays(current, maintenanceDate) > 12) {
			super.changeStatus("Under Maintenance");
		} else {
			super.changeStatus("Available");
		}
	}

	//Method for creating Van information in requested format
	@Override
	public String toString() {
		String str = super.getVehicleID();
		str += ":" + super.getManuYear() + ":" + super.getMake() + ":" + super.getModel() + ":"
				+ super.getVehicleType() + ":" + super.getNumberOfPassengerSeat() + ":" + super.getVehicleStatus() + ":"
				+ getFormattedMaintenanceDate(this.maintenanceDate);
		return str;
	}
     
	//Method for creating maintenance date in requested format
	public String getFormattedMaintenanceDate(DateTime date) {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		return sdf.format(date);
	}

	//Method for renting a vehicle
	@Override
	boolean rent(String customerId, DateTime rentDate, int numOfRentDay) {
		if (super.getVehicleType() == "Available" && super.getVehicleType() == "Van") {
			if (numOfRentDay < 1 ) {
				return false;
			} 
			super.shiftingRecord();
			DateTime estimatedReturnDate = new DateTime(rentDate, numOfRentDay);
			RentalRecord newRecord = new RentalRecord(customerId, rentDate, estimatedReturnDate);
			super.insertRecord0(newRecord);
			super.changeStatus("Rented");
			return true;
		}
		return false;
	}

	//Method for return a vehicle
	@Override
	boolean vehicleReturn(DateTime returnDate) {
		if (DateTime.diffDays(returnDate, super.getRecord(0).getRentDate()) <= 0
				|| super.getVehicleStatus() != "Rented")
			return false;
		super.getRecord(0).insertActualReturnDate(returnDate);
		double rentalFee = this.calculateRentalFee();
		double lateFee = this.calculateLateFee();
		super.updateRecord0(returnDate, rentalFee, lateFee);
		super.changeStatus("Available");
		return true;
	}

	//Method to modify the vehicle status to make vehicle under maintenance
	@Override
	boolean performMaintenance() {
		if (super.getVehicleStatus() != "Available")
			return false;
		super.changeStatus("Under Maintenance");
		System.out.println(super.getVehicleType() + " " + super.getVehicleID() + " is now under maintenance.");
		return true;
	}

	//Method to modify the Vehicle status to complete Vehicle maintenance
	@Override
	boolean completeMaintenance(DateTime completionDate) {
		if (super.getVehicleStatus() != "Under Maintenance")
			return false;
		if (DateTime.diffDays(maintenanceDate, completionDate) > 0)
			return false;
		super.changeStatus("Available");
		this.maintenanceDate = completionDate;
		System.out.println(super.getVehicleType() + " " + super.getVehicleID() + " has all maintenance completed and ready for rent.");
		return true;
	}

	//The method used to calculate rent for Van
	@Override
	double calculateRentalFee() {
		if (DateTime.diffDays(super.getRecord(0).getActualReturnDate(),
				super.getRecord(0).getEstimatedReturnDate()) <= 0)
			return (DateTime.diffDays(super.getRecord(0).getActualReturnDate(), super.getRecord(0).getRentDate())
					* this.vanRate);
		else
			return (DateTime.diffDays(super.getRecord(0).getEstimatedReturnDate(), super.getRecord(0).getRentDate())
					* this.vanRate);
	}

	//The method used to calculate late rent for Van
	@Override
	double calculateLateFee() {
		if (DateTime.diffDays(super.getRecord(0).getActualReturnDate(),
				super.getRecord(0).getEstimatedReturnDate()) > 0)
			return (DateTime.diffDays(super.getRecord(0).getActualReturnDate(),
					super.getRecord(0).getEstimatedReturnDate()) * this.vanLateRate);
		else
			return (0.0);
	}

	//The method used to return Van Rate
	public double vanRate() {
		return this.vanRate;
	}

	//The method used to return Van late Rate
	public double getVanLateRate() {
		return this.vanLateRate;
	}

	//The method used to return the Maintenance Date van
	public DateTime getMaintenanceDate() {
		return this.maintenanceDate;
	}
}

