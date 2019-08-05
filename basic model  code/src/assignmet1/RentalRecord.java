package assignmet1;

public class RentalRecord {

		private String recordID;
		private String customerID;
		private DateTime rentDate;
		private DateTime estimatedReturnDate;
		private DateTime actualReturnDate;
		private double rentalFee;
		private double lateFee;

		//constructor
		public RentalRecord(String customerID, DateTime rentDate, DateTime estimatedReturnDate) {
			this.customerID = customerID;
			this.rentDate = rentDate;
			this.estimatedReturnDate = estimatedReturnDate;
		}

		/*
		 *  To print the details of a rental record in the requested format
		 */
		 
		public String toString() {
			String str = recordID + ":" + rentDate + ":" + estimatedReturnDate + ":";
			if (actualReturnDate != null)
				str += actualReturnDate + ":" + String.format("%.2f", rentalFee) + ":" + String.format("%.2f", lateFee);
			else
				str += "none:none:none";
			return str;
		}

		/*
		 *  To print the details of a rental record in the requested format
		 */
		 
		public String getDetails() {
			String details = "";
			details = "Record ID:";
			details += "\t\t" + recordID + "\n";
			details += "Rent Date:";
			details += "\t\t" + rentDate + "\n";
			details += "Estimated Return Date:";
			details += "\t" + estimatedReturnDate + "\n";
			if (actualReturnDate != null) {
				details += "Actual Return Date:";
				details += "\t" + estimatedReturnDate + "\n";
				details += "Rental Fee:";
				details += "\t\t" + String.format("%.2f", rentalFee) + "\n";
				details += "Late Fee:";
				details += "\t\t" + String.format("%.2f", lateFee) + "\n";
			}
			details += "-------------------------------------\n";
			return details;
		}
		
		//To set up record ID
		public void insertRecordID(String VehicleID)
		{
			recordID = VehicleID;
			recordID += "_" + customerID;
			recordID += "_" + rentDate.toString().replaceAll("/", "");
		}
		
		//To set up customer ID
		public void insertCustomerID(String ID)
		{
			customerID = ID;
		}
		
		//To set up or insert the date of rental start
		public void insertRentDate(DateTime rentDate)
		{
			DateTime startRent = new DateTime(rentDate, 0);
			this.rentDate = startRent;
		}
		
		//To set up or insert the estimated return date
		public boolean insertEstimatedRetureDate(int numOfRentDay)
		{
			if(numOfRentDay<0) return false;
			DateTime endRent = new DateTime(this.rentDate, numOfRentDay);
			this.estimatedReturnDate = endRent;
			return true;
		}
		
		//To set up or insert the the actual return date
		public boolean insertActualReturnDate(DateTime returnDate)
		{
			if(DateTime.diffDays(returnDate, rentDate)<0) return false;
			this.actualReturnDate = returnDate;
			return true;
		}
		
		//To set up or insert the rent fee
		public void insertRentalFee(double rentalFee)
		{
			this.rentalFee = rentalFee;
		}
		
		//To set up or insert the late rent fee
		public void insertLateFee(double lateFee)
		{
			this.lateFee = lateFee;
		}

		//The method used to return record ID
		public String getRecordID() {
			return this.recordID;
		}

		//The method used to return customer ID
		public String getCustomerID() {
			return this.customerID;
		}

		//The method used to return the date of rental start
		public DateTime getRentDate() {
			return this.rentDate;
		}

		//The method used to return the estimated return date
		public DateTime getEstimatedReturnDate() {
			return this.estimatedReturnDate;
		}

		//The method used to return the actual return date
		public DateTime getActualReturnDate() {
			return this.actualReturnDate;
		}

		//The method used to return the rent fee
		public double getRentalFee() {
			return this.rentalFee;
		}

		//The method used to return the late rent fee
		public double getLateFee() {
			return this.lateFee;
		}
	}
