package assignmet1;

abstract class Vehicle {

	private String vehicleID;
	private String manuYear;
	private String make;
	private String model;
	private int numOfPassengerSeat;
	private String vehicleType;
	private String vehicleStatus;
	private RentalRecord[] recordList = new RentalRecord[10];

public Vehicle() {
}
	//constructor
	public Vehicle(String vehicleID, String manuYear, String make, String model, int numOfPassengerSeat,
			String vehicleType) {
		this.vehicleID = vehicleID;
		this.manuYear = manuYear;
		this.make = make;
		this.model = model;
		this.numOfPassengerSeat = numOfPassengerSeat;
		this.vehicleType = vehicleType;
		this.recordList = new RentalRecord[10];
	}

	/*
	 * This method for creating a string of vehicle details in requested and return it. 
	 */
	public String toString() {
		String str = vehicleID + ":" + manuYear + ":" + make + ":" + model + ":" + numOfPassengerSeat + ":" + vehicleStatus;
		return str;
	}

	/*
	 * This method for creating a string of property details in requested and return it. 
	 */
	public String getDetails() {
		String str = "Vehicle ID:";
		str += "\t" + vehicleID + "\n";
		str += "Year:";
		str += "\t" + manuYear + "\n";
		str += "Make:";
		str += "\t" + make + "\n";
		str += "Model:";
		str += "\t" + model + "\n";
		str += "Number of seat:";
		str += "\t" + numOfPassengerSeat + "\n";
		str += "Status:";
		str += "\t" + vehicleStatus + "\n";
		if (recordList[0] == null) {
			str += "RENTAL RECORD:" + "\t" + "empty";
			return str;
		}
		str += "RENTAL RECORD:" + "\n";
		for (int i = 0; i < recordList.length; i++) {
			if (recordList[i] == null)
				break;
			str += recordList[i].getDetails();
		}
		return str;
	}

	//The method used to change the status of vehicle
	public boolean changeStatus(String status) {
		if (status.compareTo("Available") == 0 || status.compareTo("Rented") == 0
				|| status.compareTo("Under Maintenance") == 0) {
			this.vehicleStatus = status;
			return true;
		}
		return false;
	}

	//The method used to shift the records one step forward in the record list
	public void shiftingRecord() {
		for (int i = 1; i < recordList.length; i++) {
			recordList[recordList.length - i] = recordList[recordList.length - i - 1];
		}
	}

	//The method used to insert one record in the position 0 after shifting record
	public void insertRecord0(RentalRecord record) {
		recordList[0] = record;
		recordList[0].insertRecordID(this.vehicleID);
	}

	//The method used to change the record in the position 0
	public boolean updateRecord0(DateTime acturalReturnDate, double rentalFee, double lateFee) {
		if (DateTime.diffDays(recordList[0].getRentDate(), acturalReturnDate) <= 0) {
			System.out.println("--------------------------------------");
			recordList[0].insertActualReturnDate(acturalReturnDate);
			recordList[0].insertRentalFee(rentalFee);
			recordList[0].insertLateFee(lateFee);
			return true;
		}
		return false;
	}

	//The method used to return the vehicle ID
	public String getVehicleID() {
		return this.vehicleID;
	}

	//The method used to return the year of a vehicle
	public String getManuYear() {
		return this.manuYear;
	}

	//The method used to return the make of a vehicle
	public String getMake() {
		return this.make;
	}

	//The method used to return the model of a vehicle
	public String getModel() {
		return this.model;
	}

	//The method used to return the number of passenger seat of a vehicle
	public int getNumberOfPassengerSeat() {
		return this.numOfPassengerSeat;
	}

	//The method used to return the type of a vehicle
	public String getVehicleType() {
		return this.vehicleType;
	}

	//The method used to return the status of a vehicle
	public String getVehicleStatus() {
		return this.vehicleStatus;
	}

	//The method used to return one record from the record list
	public RentalRecord getRecord(int index) {
		return this.recordList[index];
	}

	/*
	 * Abstract method for renting a vehicle
	 */
	abstract boolean rent(String customerId, DateTime rentDate, int numOfRentDay);

	/*
	 * Abstract method for return a rented vehicle
	 */
	abstract boolean vehicleReturn(DateTime returnDate);

	/*
	 * Abstract method for maintaining a vehicle
	 */
	abstract boolean performMaintenance();

	/*
	 * Abstract method to complete maintaining a vehicle
	 */
	abstract boolean completeMaintenance(DateTime completionDate);

	//Abstract method for calculating the rent for a vehicle
	abstract double calculateRentalFee();

	//Abstract method for calculating the late rent for a property
	abstract double calculateLateFee();

}
