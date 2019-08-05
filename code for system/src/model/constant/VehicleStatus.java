package model.constant;

public interface VehicleStatus {
	public static final String AVAILABLE = "currently available for rent";
	public static final String BEING_RENTED = "being rented";
	public static final String UNDER_MAINTENANCE = "under maintenance";

	public interface CarStatus extends VehicleStatus {
	}

	public interface VanStatus extends VehicleStatus {
	}

}
