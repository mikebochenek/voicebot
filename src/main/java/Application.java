import model.*;
import spark.*;
import spark.template.velocity.*;
import util.CreationDateHelper;
import util.DataLoader;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import pojo.Poll;
import static spark.Spark.*;

/**
 * This class is the Application entry point (it follows normal Spark conventions)
 */
public class Application {
    public static Logger logger = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) {

        exception(Exception.class, (e, req, res) -> e.printStackTrace()); // print all exceptions

        if (args.length > 0 && "localhost".equals(args[0])) { //http://sparkjava.com/documentation.html#examples
            String projectDir = System.getProperty("user.dir");
            String staticDir = "/src/main/resources/public";
            staticFiles.externalLocation(projectDir + staticDir);
            logger.info("running as localhost, serving directly from " + projectDir + staticDir);
        } else {
            staticFiles.location("/public");
        }

        loadPropertiesFromFile(args.length > 1 ? args[1] : "config.properties");
    	
		if (args.length == 1) {
			DataLoader.main(args); // parse and import filename
		}
    	
        exception(Exception.class, (e, req, res) -> e.printStackTrace()); // print all exceptions
        staticFiles.location("/public");
        port(9989);

        get("/", (req, res) -> renderTemplate("velocity/index.vm", new HashMap<>(), req));
        get("/json/polls", (req, res) -> renderPolls(req, res));
    }
    
    private static String renderPolls(Request req, Response res) {
	    Gson gson = new GsonBuilder().setPrettyPrinting().create();
        DataLoader.initDBConnection();
        String email = req.queryParams("email");
        String keyword = req.queryParams("keyword");
        String preset = req.queryParams("preset");
        logger.info("preset: " + preset + " email: " + email + " keyword: " + keyword);
        List<String> pollStrings = Sql2oModel.getInstance().getPollsByInitiatorSearch(email, 
        		CreationDateHelper.convertPresetToTime(preset), keyword);
        List<Poll> polls = new ArrayList<Poll>();
        for (String s : pollStrings) { //TODO bad workaround for having them stored in JSON
        	polls.add(gson.fromJson(s, Poll.class));
        }
        res.type("application/json; charset=utf-8");
        return gson.toJson(polls);
    }

    private static String renderTemplate(String template, Map model, Request req) {
        return new VelocityTemplateEngine().render(new ModelAndView(model, template));
    }

    private static String getCurrentVMHeapState() {
    		return "total:" + Runtime.getRuntime().totalMemory() + " max:" + Runtime.getRuntime().maxMemory() + " free:" + Runtime.getRuntime().freeMemory();
    }
    
    private static void loadPropertiesFromFile(String filename) {
        Properties prop = System.getProperties();
        InputStream input = null;

        try {
            input = new FileInputStream(filename);
            prop.load(input); // load a properties file
            // System.out.println(System.getProperty("jdbc.url"));
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
}
