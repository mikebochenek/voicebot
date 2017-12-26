package util;

import static org.junit.Assert.*;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;

public class TestCreationDateHelper {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void getTimeToday() {
		long t = CreationDateHelper.getTimeToday();
		System.out.println("getTimeToday() " + t); // compare to https://www.epochconverter.com/
		assertTrue("should be less now", new Date().getTime() > t);;
	}

	@Test
	public void getTimeLastMonth() {
		System.out.println("getTimeLastMonth(): " + CreationDateHelper.getTimeLastMonth());
	}
	
	@Test
	public void getTimeLastYear() {
		System.out.println("getTimeLastYear(): " + CreationDateHelper.getTimeLastYear());
	}
}
