package util;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import model.Sql2oModel;
import pojo.Option;
import pojo.Participant;
import pojo.Poll;

import org.sql2o.Sql2o;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

public class DataLoader {

	private final static Logger logger = Logger.getLogger(DataLoader.class.getName());
	
	public static void main(String[] args) {
	    Gson gsonPretty = new GsonBuilder()./*.setPrettyPrinting().*/create(); 
		
		if (args.length < 1) {
			System.err.println("please provide JSON input file");
			System.exit(-1);
		}
		
		DataLoader dl = new DataLoader();
		initDBConnection();
		Sql2oModel model = Sql2oModel.getInstance();

		try {
			List<Poll> polls = dl.readFile(args[0]);
			for (Poll p : polls) {
				logger.info(p.toString());
				//System.out.println(p);
				long poll_id = model.createPoll(p, gsonPretty.toJson(p));
				for (Option o : p.options) {
					model.createOption(o, poll_id);
				}
				for (Participant pa : p.participants) {
					model.createParticipant(pa, poll_id);
				}
				model.createInitiators(p.initiator, poll_id);
			}
	        
	        logger.info("polls: "+model.getInstance().getPolls().size());
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static Sql2oModel initDBConnection() {
		//jdbc.url = jdbc:mysql://localhost:3306/doodle
		//jdbc.username = doodleuser
		//jdbc.password = dood78s

        final String jdbc = System.getProperty("jdbc.url", "jdbc:mysql://localhost:3306/doodle?useUnicode=true&characterEncoding=utf8"); //TODO should come from config file
        Sql2o sql2o = new Sql2o(jdbc, System.getProperty("jdbc.username", "doodleuser"), System.getProperty("jdbc.password", "dood78s")); 
        Sql2oModel model = new Sql2oModel(sql2o); // http://www.sql2o.org/docs/configuration/
        return model;
	}
	
	public List<Poll> readFile(String filename) throws FileNotFoundException, IOException, UnsupportedEncodingException {
	    Gson gson = new GsonBuilder().create();
	    List<Poll> polls = new ArrayList<Poll>();
	    
		BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(filename), "UTF8"));
		
		String s;
		while ((s = in.readLine()) != null) {		
		    JsonReader reader = new JsonReader(new StringReader(s));
		    reader.setLenient(true); //probably doesn't hurt in our case..
		    polls = gson.fromJson(reader, new TypeToken<List<Poll>>(){}.getType());
		}
		in.close();
		return polls;
	}

}
