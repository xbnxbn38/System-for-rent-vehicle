package model.dao;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import model.constant.VehicleStatus;
import model.entity.RentalVehicle;
import model.exception.PerformMaintenanceException;

public class RentalVehicleDaoTest {
	RentalVehicle rentalVehicle;
	@Before
	public void setUp() throws Exception {
		rentalVehicle=new RentalVehicle();
		rentalVehicle.setVehicleId("C_700BM");
		rentalVehicle.setStatus(VehicleStatus.BEING_RENTED);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGetAllRentalVehicle() {
		System.out.println(RentalVehicleDao.getAllRentalVehicle());
	}

	@Test
	public void testUpdateStatus() throws PerformMaintenanceException{
		RentalVehicleDao.updateStatus(rentalVehicle);
	}
}
