package model;
import static org.junit.Assert.*;

import model.Sql2oModel;

import org.junit.Before;
import org.junit.Test;
import org.sql2o.Sql2o;

public class TestSql2oModel {

	final String testemail = "mh+sample@doodle.com";

	private Sql2oModel model;
	@Before
	public void setUp() throws Exception {
        final String jdbc = System.getProperty("jdbc.url", "jdbc:mysql://localhost:3306/voicebot?verifyServerCertificate=false&useSSL=true");
        Sql2o sql2o = new Sql2o(jdbc, System.getProperty("jdbc.username", "voiceuser"), System.getProperty("jdbc.password", "tuzysvoice")); 
        model = new Sql2oModel(sql2o); // http://www.sql2o.org/docs/configuration/
    }

	@Test
	public void getRecordings() {
		long s = System.currentTimeMillis();
		System.out.println("getRecordings:" + model.getRecordings().size());
		System.out.println((System.currentTimeMillis() - s) + "ms expired.");
	}
	
	@Test
	public void getPrompts() {
		long s = System.currentTimeMillis();
		System.out.println("getPrompts:" + model.getPrompts().size());
		assertTrue("should have prompts", model.getPrompts().size() > 0);
		System.out.println((System.currentTimeMillis() - s) + "ms expired.");
	}

	@Test
	public void getUsers() {
		long s = System.currentTimeMillis();
		System.out.println("getUsers:" + model.getUsers().size());
		System.out.println((System.currentTimeMillis() - s) + "ms expired.");
	}

	@Test
	public void getRestaurants() {
		long s = System.currentTimeMillis();
		System.out.println("getRestaurants:" + model.getRestaurants().size());
		System.out.println((System.currentTimeMillis() - s) + "ms expired.");
	}
}
