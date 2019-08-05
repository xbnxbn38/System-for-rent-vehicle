package assignmet1;

class Car extends Vehicle {

	private double rateFor4_seat = 78.0;
	private double rateFor7_seat = 113.0;
	private double lateRate = 1.25;

	public Car() {
		
	}

	//constructor
	public Car(String vehicleID, String manuYear, String make, String model, int numOfPassengerSeat,
			String vehicleType) {
		super(vehicleID, manuYear, make, model, numOfPassengerSeat, vehicleType);
		super.changeStatus("Available");
	}

	//Method for renting a vehicle
	@Override
	boolean rent(String customerId, DateTime rentDate, int numOfRentDay) {
		if (super.getVehicleStatus() == "Available" && super.getVehicleType() == "Car") {//Determine whether the vehicle is available for rent
			if (rentDate.getNameOfDay() != "Friday" && rentDate.getNameOfDay() != "Saturday") {//Determine if the rent length is legal
				if (numOfRentDay < 2 || numOfRentDay > 14)
					return false;
			} else if (rentDate.getNameOfDay() == "Friday" || rentDate.getNameOfDay() == "Saturday") {//Determine if the rent length is legal
				if (numOfRentDay < 3 || numOfRentDay > 14)
					return false;
			}
			//Update rental records
			super.shiftingRecord();
			DateTime estimatedReturnDate = new DateTime(rentDate, numOfRentDay);
			RentalRecord newRecord = new RentalRecord(customerId, rentDate, estimatedReturnDate);
			super.insertRecord0(newRecord);
			//Set vehicle status
			super.changeStatus("Rented");
			return true;
		}
		return false;
	}

	//Method for return a vehicle
	@Override
	boolean vehicleReturn(DateTime returnDate) {
		if (DateTime.diffDays(returnDate, super.getRecord(0).getRentDate()) <= 0
				|| super.getVehicleStatus() != "Rented") {
			return false;
		}
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
		if (super.getVehicleStatus().compareTo("Rented") == 0
				|| super.getVehicleStatus().compareTo("Under Maintenance") == 0) {
			return false;
		}
		super.changeStatus("Under Maintenance");
		System.out.println("Car " + super.getVehicleID() + " is now under maintenance!");
		return true;
	}

	//Method to modify the vehicle status to complete vehicle maintenance
	@Override
	boolean completeMaintenance(DateTime completionDate) {
		if (super.getVehicleStatus() != "Under Maintenance")
			return false;
		super.changeStatus("Available");
		System.out.println(
				"Maintenance for Car " + super.getVehicleID() + "has all maintenance completed and ready for rent.");
		return true;
	}

	//The method used to calculate rent for Car
	@Override
	double calculateRentalFee() {
		if (super.getNumberOfPassengerSeat() == 4) {
			if (DateTime.diffDays(super.getRecord(0).getActualReturnDate(),
					super.getRecord(0).getEstimatedReturnDate()) <= 0)
				return (DateTime.diffDays(super.getRecord(0).getActualReturnDate(), super.getRecord(0).getRentDate())
						* this.rateFor4_seat);
			else
				return (DateTime.diffDays(super.getRecord(0).getEstimatedReturnDate(), super.getRecord(0).getRentDate())
						* this.rateFor4_seat);
		} else if (super.getNumberOfPassengerSeat() == 7) {
			if (DateTime.diffDays(super.getRecord(0).getActualReturnDate(),
					super.getRecord(0).getEstimatedReturnDate()) <= 0)
				return (DateTime.diffDays(super.getRecord(0).getActualReturnDate(), super.getRecord(0).getRentDate())
						* this.rateFor7_seat);
			else
				return (DateTime.diffDays(super.getRecord(0).getEstimatedReturnDate(), super.getRecord(0).getRentDate())
						* this.rateFor7_seat);
		} 
		return 0;
	}

	//The method used to calculate late rent for Car
	@Override
	double calculateLateFee() {
		if (super.getNumberOfPassengerSeat() == 4) {
			if (DateTime.diffDays(super.getRecord(0).getActualReturnDate(),
					super.getRecord(0).getEstimatedReturnDate()) > 0)
				return (DateTime.diffDays(super.getRecord(0).getActualReturnDate(),
						super.getRecord(0).getEstimatedReturnDate()) * this.rateFor4_seat * this.lateRate);
			else
				return (0.0);
		} else if (super.getNumberOfPassengerSeat() == 7) {
			if (DateTime.diffDays(super.getRecord(0).getActualReturnDate(),
					super.getRecord(0).getEstimatedReturnDate()) > 0)
				return (DateTime.diffDays(super.getRecord(0).getActualReturnDate(),
						super.getRecord(0).getEstimatedReturnDate()) * this.rateFor7_seat * this.lateRate);
			else
				return (0.0);
		} 
		return 0.0;
	}

	//The method used to return 4-seat car rate
	public double getRateFor4_seat() {
		return this.rateFor4_seat;
	}

	//The method used to return 7-seat car rate
	public double getRateFor7_seat() {
		return this.rateFor7_seat;
	}

	

	//The method used to return car late rate
	public double getLateRate() {
		return this.lateRate;
	}
}
