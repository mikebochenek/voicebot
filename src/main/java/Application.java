import model.*;
import spark.*;
import spark.template.velocity.*;
import util.CreationDateHelper;
import util.DataLoader;

import java.util.*;
import java.util.logging.Logger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import pojo.Poll;
import static spark.Spark.*;

/**
 * This class is the Application entry point (it follows normal Spark conventions)
 */
public class Application {

    public static void main(String[] args) {

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
        Logger.getAnonymousLogger().info("preset: " + preset + " email: " + email + " keyword: " + keyword);
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

}
