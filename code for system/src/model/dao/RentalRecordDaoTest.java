package model.dao;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import model.entity.RentalVehicle;
import model.entity.RentalRecord;
import model.exception.ReturnException;
import model.util.DateTime;

public class RentalRecordDaoTest {
	private RentalVehicle rentalVehicle;

	@Before
	public void setUp() throws Exception {
		rentalVehicle = new RentalVehicle();
		rentalVehicle.setVehicleId("C_701BM");
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testSave() throws Exception {
		RentalRecord record = new RentalRecord(rentalVehicle, "CUS700", new DateTime("01032019"),
				new DateTime("03032019"));
		RentalRecordDao.save(record);

	}

	@Test
	public void testFindLatestRecordByVehicle() {
		System.out.println(RentalRecordDao.findLatestRecordByVehicle(rentalVehicle).getRentalVehicle().getVehicleType());
	}

	@Test
	public void testUpdate() throws ReturnException {
		RentalRecord record=RentalRecordDao.findLatestRecordByVehicle(rentalVehicle);
		record.setActualReturnDateAndFees(new DateTime("03032019"));
		System.out.println(record.getDetail());
		RentalRecordDao.update(record);
	}
}
