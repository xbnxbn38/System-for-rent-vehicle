package assignmet1;

import java.text.SimpleDateFormat;
import java.util.*;



import java.util.Scanner; //check after



class ThriftyRentSystem {

	private Vehicle[] vehicleList = new Vehicle[50];
	private Vehicle newVehicle;
	private String vehicleID = null;
	private String customerID = null;
	private String type = null;
	private String manuYear; 
	private String make, model;
	private int day, month, year;
	private int numOfPassengerSeat = 0;
	private String rentalDay;
	private int numOfRentDay;
	private DateTime maintenanceDate = null;
	private DateTime completeMaintenanceDate = null;
	private int index = -1;
	private Scanner scan = new Scanner(System.in);
	private int menuOption;
	private String input;

	// Method that displays a menu and receives input from options
	
	void displayMenu() {
		Menu: for (;;) {
			System.out.print("**** ThriftyRent SYSTEM MENU ****\n" + "Add vehicle:                1\n"
					+ "Rent vehicle:               2\n" + "Return vehicle:             3\n"
					+ "Vehicle Maintenance:        4\n" + "Complete Maintenance:       5\n"
					+ "Display All Vehicles:       6\n" + "Exit Program:               7\n" + "Enter your choice:");
			menuOption = scan.nextInt();
			if (invalidMenuOption(menuOption)) {
				System.out.println("Please enter the number between 1 and 7.");
				continue Menu;
			} 
			switch (menuOption) {
			case 1:
				addVehicle();// Choose menuOption 1 for adding vehicle
				continue Menu;
			case 2:
				rentVehicle();// Choose menuOption 2 for renting vehicle
				continue Menu;
			case 3:
				returnVehicle();// Choose menuOption 3 for returning vehicle
				continue Menu;
			case 4:
				vehicleMaintenance();// Choose menuOption 4 for doing maintenance for a vehicle
				continue Menu;
			case 5:
				completeMaintenance();// Choose menuOption 5 for completing maintenance for a vehicle
				continue Menu;
			case 6:
				displayAllVehicle();// Choose menuOption 6 for displaying all vehicles in vehicle list
				continue Menu;
			case 7:
				systemExit();// Choose menuOption 7 for shutting down the ThriftyRent System
			default:
				continue Menu;
			}
		}
	}

	// To complete the function of adding vehicle
	
	private void addVehicle() {
		if (fullList())
			return;
		type = selectVehicleType(); // Input the type of vehicle by selecting from offered menu
		vehicleID = inputVehicleID();// Input the ID of vehicle base on the type of car
		manuYear = inputYear(); // Input the manufactured year 
		make = inputMake();
		model = inputModel();
		if (newVehicle instanceof Car) { // If the vehicle id a Car, need to input number of passenger seat
			numOfPassengerSeat = enterNumberOfPassengerSeat();
		}
		if (newVehicle instanceof Van) {// Input the last maintenance date for Van type
			do {
				System.out.print("Please enter the last maintenance date of Van as format DD/MM/YYYY: ");
				input = scan.next();
				if (!this.validMaintenanceDate(input)) {
					System.out.println("Invalid input! Please follow the requested format!");
				}
			} while (!this.validMaintenanceDate(input));
			day = stringToInt(input, 0, 1);
			month = stringToInt(input, 3, 4);
			year = stringToInt(input, 6, 9);
			maintenanceDate = new DateTime(day, month, year); // Generate a maintenance date
		}
		this.generateNewVehicle(); // Complete adding Vehicle by calling generateNewVehicle()
	}

	// To complete the function of renting a vehicle
	
	private void rentVehicle() {
		System.out.print("Please enter the vehicle ID:");
		input = scan.next();
		if (!isExistedVehicle(input)) {
			System.out.println("The vehicle ID is not existed. Return to Menu.");// check if the Vehicle is existed
			return;}
		if (!isAvailableVehicle(input, index)) {
			System.out.println("The vehicle ID is not available for rent. Return to Menu.");// check if the chosen vehicle is available for rent
			return;}
		do {
			System.out.print("Please enter Customer ID(start with CUS):");
			input = scan.next();
			isValidCustomerID(input); // check the validation of entered customer ID
		} while (!isValidCustomerID(input));
		System.out.print("Please enter the rent date(dd/mm/yyyy):");
		rentalDay = scan.next();
		if (!isValidRentalDate(rentalDay)) // check the validation of entered rental day
			return;
		do {
			System.out.print("How many days for rental?:");
			input = scan.next();
			if (!isNumber(input))
				System.out.println("Invalid input! The days of rental should be a number!");
		} while (!isNumber(input));
		numOfRentDay = Integer.parseInt(input);
		if (vehicleList[index].getVehicleType() == "Van") {
			input = calculateActualRentDays(input, rentalDay);
		}
		if (!isAllowedRentalDuration(input, index, rentalDay)) {
			return;
		}
		
		if (vehicleList[index] instanceof Van) {
			/*if (!vehicleList[index].rent(customerID, calculateDate(rentalDay), numOfRentDay))// check if the Van could be rented for maximum 30 days
				System.out.println("Sorry! The length of duration should be less than "
						+ (30 - DateTime.diffDays(calculateDate(rentalDay),
								((Van) vehicleList[index]).getMaintenanceDate()))
						+ " days\n" + "based on the rent day");*/
			vehicleList[index].rent(customerID, calculateDate(rentalDay), numOfRentDay);
		}
		System.out.println(vehicleList[index].getVehicleType() + " " + vehicleList[index].getVehicleID()
				+ " is now rented by customer " + vehicleList[index].getRecord(0).getCustomerID());
	}

	// To complete the function of returning a vehicle
	
	private void returnVehicle() {
		System.out.print("Please enter the Vehicle ID:");
		input = scan.next();
		if (!isExistedVehicle(input)){
			System.out.println("The vehicle ID is not existed. Return to Menu.");// check if the Vehicle is existed
			return;}
		if (!checkValidationForReturn(index)){
			System.out.println("The vehicle ID is not available for return. Return to Menu.");// check if the chosen vehicle is available for return
			return;}
		System.out.print("Please enter the return date(dd/mm/yyyy):");
		input = scan.next();
		if (!checkValidationForReturnDate(input, index))
			return;
		day = stringToInt(input, 0, 1);
		month = stringToInt(input, 3, 4);
		year = stringToInt(input, 6, 9);
		DateTime returnDate = new DateTime(day, month, year);
		vehicleList[index].vehicleReturn(returnDate);
		System.out.println(vehicleList[index].getVehicleType() + " " + vehicleList[index].getVehicleID()
				+ " has been returned by customer " + vehicleList[index].getRecord(0).getCustomerID());
		System.out.println(vehicleList[index].getDetails());
	}

	// To complete the function of maintaining a vehicle
	
	private void vehicleMaintenance() {
		System.out.print("Please enter the vehicle ID:");
		input = scan.next();
		if (!isExistedVehicle(input)) {
			System.out.print("The vehicle ID is not existed. Return to Menu." + "\n");// check if the entered Vehicle is existed
			return;}
		if (!checkValidationForMaintenance(index)) {
			System.out.print("The vehicle is not valid for maintenance. Return to Menu." + "\n");// check if the entered vehicle is valid for maintenance
			return;}
		vehicleList[index].performMaintenance();
	}

	// To complete the function of completing maintaining a vehicle
	
	private void completeMaintenance() {
		System.out.print("Please enter the vehicle ID:");
		input = scan.next();
		if (!isExistedVehicle(input)) { 
			System.out.print("The vehicle ID is not existed. Return to Menu." + "\n");// check if the entered vehicle is existed
			return;}
		if (!checkValidationForCompleteMaintenance(index)) {
			System.out.print("The vehicle is not valid for completing maintenance. Return to Menu." + "\n");// check if the entered vehicle is valid for completing maintenance
			return;}
		if (vehicleList[index] instanceof Car) {
			vehicleList[index].completeMaintenance(completeMaintenanceDate);
			return;
		}
		System.out.print("Please enter the date of maintenance completion(dd/mm/yyyy): ");
		input = scan.next();
		if (!checkValidationForCompleteMaintenanceDate(input, index))
			return;
		day = stringToInt(input, 0, 1);
		month = stringToInt(input, 3, 4);
		year = stringToInt(input, 6, 9);
		completeMaintenanceDate = new DateTime(day, month, year);
		vehicleList[index].completeMaintenance(completeMaintenanceDate);
	}

	// To complete the function of displaying all vehicles in vehicle list
	
	private void displayAllVehicle() {
		int empty = 0;
		for (int i = 0; i < vehicleList.length; i++) {
			if (vehicleList[i] != null) {
				System.out.println(vehicleList[i].getDetails());
			} else {
				empty++;
			}
			if (empty == vehicleList.length - 0) {
				System.out.println("The vehicle List is empty!");
			}
		}
	}

	// To complete the function of checking the validation of entered menu option
	
	private boolean invalidMenuOption(int option) {
		boolean invalid = true;
		if (option > 0 && option < 8)
			invalid = false;
		return invalid;
	}

	// To complete the function of checking the vehicle list full or not
	
	private boolean fullList() {
		boolean full = true;
		for (int i = 0; i < vehicleList.length; i++) {
			if (vehicleList[i] == null) {
				return full = false;
			}
			if (i == vehicleList.length - 1) {
				System.out.println("Sorry! Vehicle List is full! Cannot add new vehicle!");
			}
		}
		return full;
	}

	
	// To complete the function of selecting the vehicle type
	
	private String selectVehicleType() {
		String t = null;
		do {
			System.out.println("Please select the type of new vehicle:\n 1. Car\n 2. Van");
			menuOption = scan.nextInt();
			if (menuOption == 1) {
				t = "Car";
				newVehicle = new Car();
			} else if (menuOption == 2) {
				t = "Van";
				newVehicle = new Van();
			} else
				System.out.println("Invalid selection! Please select one option from 1 and 2!");
		} while (menuOption != 1 && menuOption != 2);
		return t;
	}

	
	// To complete the function of selecting the vehicle ID
		private String inputVehicleID() {
			String iD = null;
			do {
					System.out.print("Please enter the Vehicle ID after title:");
					String d = null;
					d = scan.next(); 
					if (type == "Car")
						iD = "C_" + d;// If it is an Car, the ID should start with C_
				   else if (type == "Van")
						iD = "V_" + d;// If it is a Van, the ID should start with V_
				if (isExistedVehicle(iD)) 
					System.out.println("Invalid input! The Vehicle ID is already exists!");
			} while (isExistedVehicle(iD));
			return iD;
		}
		
		
		
		
	// To complete the function of entering the manufactured year of a vehicle
	
	private String inputYear() {
		String y = null;
		do {
			System.out.print("Please enter the manufactured year of new Vehicle:");
			y = scan.next();
			if (!isNumber(y))
				System.out.println("Invalid input! The manufactured year should be a number!");// Complete the checking for validation of entered year
		} while (!isNumber(y));
		return y;
	}

	// To complete the function of checking the input is a number or not
	
	private boolean isNumber(String str) {
		for (int i = 0; i < str.length(); i++) {
			if (!Character.isDigit(str.charAt(i))) {
				return false;
			}
		}
		return true;
	}
	
    // To complete the function of entering the make of a vehicle
	private String inputMake() {
		String ma = null;
		System.out.print("Please enter the make of new vehicle:");
		ma =scan.next();
		return ma;
	}
	
	
	
	// To complete the function of entering the m of a vehicle
	
	private String inputModel() {
		String mo = null;
		System.out.print("Please enter the mode name of new vehicle:");
		mo =scan.next();
		return mo;
	}
	
	
	
	// To complete the function of entering the number of passenger seat of a vehicle 
	
	private int enterNumberOfPassengerSeat() {
		do {
			do {
				System.out.println("Please enter the number of passenger seat for the new Car from 4 or 7:");
				input = scan.next(); // Choose the number of passenger seat from options
				if (!isNumber(input)) // Check the input is a number
					System.out.println("Invalid input! The number of passenger seat should be numeric!");
			} while (!isNumber(input));
			menuOption = stringToInt(input, 0, 0);
			if (menuOption !=4 && menuOption !=7) 
				System.out.println("Invalid input! The number of passenger seat should be 4 or 7!");
		} while (menuOption != 4 && menuOption != 7);
		return menuOption;
	}

	// To complete the function of checking the validation of entered maintenance date
	
	private boolean validMaintenanceDate(String dateEntered) {
		if (dateEntered.length() != 10)
			return false;
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
		try {
			Date time = (Date) format.parse(dateEntered);
			Date currentTime = new Date();
			if (time.after(currentTime))
				return false;
			return (dateEntered.equals(format.format(time)));
		} catch (Exception e) {
			return false;
		}
	}

	// To complete the function of generating one new vehicle and adding it to vehicle list
	
	private void generateNewVehicle() {
		if (newVehicle instanceof Car) {
			newVehicle = new Car(vehicleID, manuYear, make, model, numOfPassengerSeat, type);
		} else if (newVehicle instanceof Van) {
			newVehicle = new Van(vehicleID, manuYear, make, model, type, maintenanceDate);
		}
		for (int i = 0; i < vehicleList.length; i++) {
			if (vehicleList[i] == null) {
				vehicleList[i] = newVehicle;
				System.out.println("Congratulations! New vehicle "+ vehicleID +" has been added to Vehicle List!\n");
				break;
			}
		}
	}

	// To complete the function of translating a string into a integer
	
	private int stringToInt(String input2, int start, int end) {
		int num = 0;
		int times = 1;
		for (int i = end; i >= start; i--) {
			num += (input.charAt(i) - '0') * times;
			times *= 10;
		}
		return num;
	}

	// To complete the function of checking the vehicle with entered ID is existed or not
	
	private boolean isExistedVehicle(String enteredID) {
		boolean e = false;
		for (int i = 0; i < this.vehicleList.length; i++) {
			if (vehicleList[i] != null && vehicleList[i].getVehicleID().compareTo(enteredID) != 0) {
				if (i == vehicleList.length - 1) {
					System.out.println("vehicle is not existed! Please check the vehicle ID.");
				}
				continue;
			} else if (vehicleList[i] != null && vehicleList[i].getVehicleID().compareTo(enteredID) == 0) {
				index = i;
				e = true;
			}
		}
		return e;
	}

	// To complete the function of checking the validation of a vehicle available for rent or not
	
	private boolean isAvailableVehicle(String enteredID, int index) {
		if (vehicleList[index].getVehicleStatus() == "Rented") {
			System.out.println("Sorry! The vehicle is Rented.");
			return false;
		} else if (vehicleList[index].getVehicleStatus() == "Under Maintenance") {
			System.out.println("Sorry! The vehicle is under maintenance.");
			return false;
		} else if (vehicleList[index].getVehicleStatus() == "Available")
			return true;
		return false;
	}

	
	// To complete the function of checking the entered customer ID is legal or not
	
	private boolean isValidCustomerID(String input) {
		if (!input.startsWith("CUS")) {
			System.out.println("Invalid Input! The customer ID should start with CUS.");
			return false;
		}
		customerID = input;
		return true;
	}

	// To complete the function of checking the validation of entered rent date
	
	private boolean isValidRentalDate(String rentalDay) {
		if (rentalDay.length() != 10)
			return false;
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
		try {
			Date time = (Date) format.parse(rentalDay);
			Date currentTime = new Date();
			if (time.compareTo(currentTime) < -1) {
				return false;
			}
			return (rentalDay.equals(format.format(time)));
		} catch (Exception e) {
			return false;
		}
	}

	// To complete the function of calculating the actual rental days
	
	private String calculateActualRentDays(String input, String rentalDay) {
		DateTime rent = calculateDate(rentalDay);
		DateTime current = new DateTime();
		int diff = DateTime.diffDays(rent, current) + Integer.parseInt(input);
		return String.valueOf(diff);
	}

	// To complete the function of calculating date
	
	private DateTime calculateDate(String rentalDate) {
		DateTime time = new DateTime((rentalDate.charAt(0) - '0') * 10 + (rentalDate.charAt(1) - '0'),
				(rentalDate.charAt(3) - '0') * 10 + (rentalDate.charAt(4) - '0'),
				(rentalDate.charAt(6) - '0') * 1000 + (rentalDate.charAt(7) - '0') * 100
						+ (rentalDate.charAt(8) - '0') * 10 + (rentalDate.charAt(9) - '0'));
		return (time);
	}

	// To complete the function of checking the validation of entered rental days
	
	private boolean isAllowedRentalDuration(String input, int index, String rentalDay) {
		if (vehicleList[index].getVehicleType().compareTo("Car") == 0) {// determine if 2 or 3 <= input <=14 for Car
			
			DateTime rentTime = calculateDate(rentalDay);
			if (Integer.parseInt(input) > 14) {
				System.out.println("Sorry! The rent days of Car cannot exceed 14 days!");
				return false;
			}
			if ((rentTime.getNameOfDay().compareTo("Friday") == 0 || rentTime.getNameOfDay().compareTo("Saturday") == 0)
					&& Integer.parseInt(input) < 1) {
				System.out.println(
						"The rental days for Car cannot be less than:\n 2 days if the rent day is between Sunday and Thursday!\n 3 days if the rental day is Friday or Saturday!");
				return false;
			} else if (Integer.parseInt(input) < 2) {
				System.out.println(
						"The rental days for Car cannot be less than:\n 2 days if the rent day is between Sunday and Thursday!\n 3 days if the rental day is Friday or Saturday!");
				return false;
			}
			return true;
		} else if (vehicleList[index].getVehicleType().compareTo("Van") == 0) { // determine if 1 <= input for Van
			
			if (Integer.parseInt(input) < 1 ) {
				System.out.println(
						"Sorry! The number of rental days for Van cannot be less than 1 day!");
				return false;
			} else
				return true;
		}
		return false;
	}

	// To complete the function of checking the validation of maintenance with the entered vehicle ID
	
	private boolean checkValidationForMaintenance(int index) {
		if (vehicleList[index].getVehicleStatus() == "Available") {// checking the status is available or others
			return true;
		} else if (vehicleList[index].getVehicleStatus() == "Rented") {// checking the status is available or others
			System.out.println("Sorry! The vehicle has been rented! It is not good time for Maintenance!");
			return false;
		} else if (vehicleList[index].getVehicleStatus() == "Under Maintenance") {// checking the status is available or others
			System.out.println("Sorry! The vehicle has been under maintenance!");
			return false;
		}
		return false;
	}

	// To complete the function of checking the validation of completing maintenance with the entered vehicle ID
	
	private boolean checkValidationForCompleteMaintenance(int index) {
		if (vehicleList[index].getVehicleStatus() == "Under Maintenance") {// checking the status is under maintenance 
			                                                               //or others
			return true;
		} else if (vehicleList[index].getVehicleStatus() == "Rented") {// checking the status is under maintenance or
																			// others
			System.out.println("Sorry! The vehicle is not under maintenance!");
			return false;
		} else if (vehicleList[index].getVehicleStatus() == "Available") {// checking the status is under maintenance
																			// or others
			System.out.println("Sorry! The vehicle is not under maintenance!");
			return false;
		}
		return false;
	}

	// To complete the function of checking the validation of maintenance with the
	// entered complete date
	private boolean checkValidationForCompleteMaintenanceDate(String date, int index) {
		try {
			DateTime time = calculateDate(date);
			DateTime lastDay = ((Van) vehicleList[index]).getMaintenanceDate();
			if (DateTime.diffDays(time, lastDay) >= 0) {
				return true;
			} else {
				System.out.println("The duration of maintenance must be at least one day!");
				return false;
			}
		} catch (Exception e) {
			return false;
		}
	}

	// To complete the function of checking the validation of entered return date
	// with previous input of vehicle ID
	private boolean checkValidationForReturnDate(String input, int index) {
		try {
			DateTime time = calculateDate(input);
			DateTime rentalTime = vehicleList[index].getRecord(0).getRentDate();
			if (vehicleList[index].getVehicleType() == "Car") {
				if (rentalTime.getNameOfDay() == "Friday" || rentalTime.getNameOfDay() == "Saturday") {
					if (DateTime.diffDays(time, rentalTime) < 3) {
						System.out.println(
								"The duration of rental for Car cannot be less than 2 days if the Rental Day is between Sunday \n and Thursday and 3 days if the Rental Day is Friday or Saturday");
						return false;
					}
				} else if (rentalTime.getNameOfDay().compareTo("Friday") != 0
						&& rentalTime.getNameOfDay().compareTo("Saturday") != 0) {
					if (DateTime.diffDays(time, rentalTime) < 2) {
						System.out.println("The duration of maintenance must be at least one day!");
						return false;
					}
				}
			} else if (vehicleList[index].getVehicleType() == "Van") {
				if (DateTime.diffDays(time, rentalTime) < 1) {
					System.out.println(
							"The duration of rental does not match the duration request, minimum 1 day, for Van!");
					return false;
				} else
					return true;
			}
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	//
	private boolean checkValidationForReturn(int index) {
		if (vehicleList[index].getVehicleStatus().compareTo("Rented") == 0)
			return true;
		else {
			System.out.println("Sorry! The requested vehicle has not been rented!");
			return false; // exist but not being rented
		}
	}

	// To complete the function of chuting down the ThriftyRent System
	private void systemExit() {
		System.out.println("ThriftyRent System has been terminated!");
		scan.close();
		System.exit(0);
	}
}