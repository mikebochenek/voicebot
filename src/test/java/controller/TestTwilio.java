package controller;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class TestTwilio {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testCreateFirstPrompt() {
		Twilio t = new Twilio();
		t.createFirstPrompt();
	}

}
