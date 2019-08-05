package model.dao;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import model.entity.RentalVehicle;

public class CarDaoTest {
	private RentalVehicle rentalVehicle;
	@Before
	public void setUp() throws Exception {
		rentalVehicle=new RentalVehicle();
		rentalVehicle.setVehicleId("C_2010BM");
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testFindPerDayFeeByVehicleId() {
		System.out.println(CarDao.findPerDayFeeByVehicleId(rentalVehicle.getVehicleId()));
	}

}
