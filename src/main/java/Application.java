import model.*;
import spark.*;
import spark.template.velocity.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.sql2o.Sql2o;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import controller.Twilio;
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
        initDBConnection();
        testDBCode();
        
        exception(Exception.class, (e, req, res) -> e.printStackTrace()); // print all exceptions
        staticFiles.location("/public");
        port(9989);

        get("/", (req, res) -> renderTemplate("velocity/index.vm", new HashMap<>(), req));
        get("/api/recordings", (req, res) -> renderRecordings(req, res));
        get("/voice/intro", (req, res) -> renderXML(req, res, Twilio.createFirstPrompt(req)));
        post("/voice/record", (req, res) -> renderXML(req, res, Twilio.handleRecord(req)));
    }
    
    private static String renderRecordings(Request req, Response res) {
	    Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String email = req.queryParams("email");
        String keyword = req.queryParams("keyword");
        String preset = req.queryParams("preset");
        logger.info("preset: " + preset + " email: " + email + " keyword: " + keyword);
        List<Recording> recordings = Sql2oModel.getInstance().getRecordings(); //getPollsByInitiatorSearch(email, CreationDateHelper.convertPresetToTime(preset), keyword);
        res.type("application/json; charset=utf-8");
        return gson.toJson(recordings);
    }

    
    private static String renderTemplate(String template, Map model, Request req) {
        return new VelocityTemplateEngine().render(new ModelAndView(model, template));
    }
    
    private static String renderXML(Request req, Response res, String s) {
        res.type("text/xml"/*; charset=utf-8*/);
        return s;
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
    
	public static void initDBConnection() {
        final String jdbc = System.getProperty("jdbc.url", "jdbc:mysql://localhost:3306/doodle?useUnicode=true&characterEncoding=utf8"); //TODO should come from config file
        Sql2o sql2o = new Sql2o(jdbc, System.getProperty("jdbc.username", "doodleuser"), System.getProperty("jdbc.password", "dood78s")); 
        Sql2oModel model = new Sql2oModel(sql2o); // http://www.sql2o.org/docs/configuration/
	}
	
	private static void testDBCode() {
        //Sql2oModel.getInstance().createRecording(new Recording());
        logger.info("recordings size: " + Sql2oModel.getInstance().getRecordings().size());
        //Recording r = Sql2oModel.getInstance().getRecordings().get(0);
        //r.status = 1;
        //r.parsedtext = "blah";
        //Sql2oModel.getInstance().updateRecording(r);

        //Sql2oModel.getInstance().createUser(new User());
        logger.info("users size: " + Sql2oModel.getInstance().getUsers().size());
	}
}
