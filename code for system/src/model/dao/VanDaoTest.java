package model.dao;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import model.entity.Van;
import model.exception.CompleteMaintenanceException;
import model.util.DateTime;

public class VanDaoTest {
	private Van van;
	@Before
	public void setUp() throws Exception {
		van=new Van();
		van.setVehicleId("V_2010WS");
		van.setLastMaintenance(new DateTime("29022019"));
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testFindPerDayFeeByVehicleId() {
		System.out.println(VanDao.findPerDayFeeByVehicleId(van.getVehicleId()));
	}

	@Test
	public void testUpdateLastCompleteMaintenance() throws CompleteMaintenanceException{
		VanDao.updateLastMaintenance(van);
	}
}
