package model;
import static org.junit.Assert.*;

import java.util.Date;
import java.util.List;

import model.Sql2oModel;

import org.junit.Before;
import org.junit.Test;

import pojo.Poll;
import util.CreationDateHelper;
import util.DataLoader;

public class TestSql2oModel {

	final String testemail = "mh+sample@doodle.com";
	
	private Sql2oModel model;
	@Before
	public void setUp() throws Exception {
        model = DataLoader.initDBConnection(); //TODO bad place to be initing DB connection
    }

	@Test
	public void getPollsByInitiator() {
		long s = System.currentTimeMillis();
		System.out.println(model.getPollsByInitiator(testemail).size());
		System.out.println((System.currentTimeMillis() - s) + "ms expired.");
	}

	@Test
	public void getPollsByInitiatorSearch() {
		long i = CreationDateHelper.getTimeLastMonth() * 1000;
		List<Poll> polls = model.getPollsByInitiatorSearch(testemail, i);
		for (Poll p : polls) {
			System.out.println(new Date(p.initiated));
		}
	}

	@Test
	public void getPollsByInitiatorSearchWithKeyword() {
		long i = CreationDateHelper.getTimeLastYear() * 1000;
		List<String> polls = model.getPollsByInitiatorSearch(testemail, i, "help");
		for (String p : polls) {
			System.out.println(p);
		}
	}

	@Test
	public void getPollsByInitiatorSearchWithoutKeyword() {
		long i = CreationDateHelper.convertPresetToTime("this_year");
		List<String> polls = model.getPollsByInitiatorSearch(testemail, i, "");
		for (String p : polls) {
			System.out.println(p);
		}
		polls = model.getPollsByInitiatorSearch(testemail, i, null);
		for (String p : polls) {
			System.out.println(p);
		}
	}
	
}
