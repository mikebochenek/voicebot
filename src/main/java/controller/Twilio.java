package controller;

import static spark.Spark.get;
import static spark.Spark.post;

import java.util.concurrent.CompletableFuture;

import com.twilio.twiml.Record;
import com.twilio.twiml.VoiceResponse;
import com.twilio.twiml.Say;
import com.twilio.twiml.TwiMLException;
import com.twilio.twiml.Say.Voice;

import model.RecordingStatusCallback;
import model.speech.Transcribe;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import spark.Request;
import spark.Response;

public class Twilio {
	static int timeoutSeconds = 3;
	static String base = "http://www.resebot.com";
	
	private final static String recordAction = "/voice/record";

    public static Logger logger = LoggerFactory.getLogger(Twilio.class);

    /**
     * get("/voice/intro", (req, res) -> renderXML(req, res, Twilio.createFirstPrompt())); 
     * @return
     */
    public static String createFirstPrompt() {
        Say say = new Say.Builder("Hello World, please leave a message.").voice(Voice.ALICE).build();
        Record record = new Record.Builder().action(base + recordAction).playBeep(false).timeout(timeoutSeconds).build();
        VoiceResponse response = new VoiceResponse.Builder().say(say).record(record).build();
		return toXML(response);
    }
    
    /**
     * post("/voice/record", (req, res) -> renderXML(req, res, Twilio.handleRecord(req, res))); 
     * @param request
     * @param response
     * @return
     */
    public static String handleRecord(Request request, Response response) {
		Say say = new Say.Builder("OK, done recording").voice(Voice.ALICE).build();
		RecordingStatusCallback params = extractCallbackParameters(request);
        logger.info(params.toString());
        
        if (params.recordingUrl != null && params.recordingUrl.length() > 0 && params.recordingUrl.startsWith("http")) {
        	Transcribe t = new Transcribe(params.recordingUrl , params.from, params.to, recordAction);
        	final CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
        		return t.transcribeURL(); //...long running... (from http://www.nurkiewicz.com/2013/05/java-8-definitive-guide-to.html)
        	});
        }
		
		VoiceResponse vresponse = new VoiceResponse.Builder().say(say).build();
		return toXML(vresponse);
	}

	private static String toXML(VoiceResponse response) {
		try {
			String xml = response.toXml();
			logger.info(xml);
			return xml;
		} catch (TwiMLException e) {
			e.printStackTrace();
			return "";
		}
	}
	
	private static RecordingStatusCallback extractCallbackParameters(Request req) {
		return new RecordingStatusCallback(req.queryParams("RecordingSid"), req.queryParams("RecordingUrl"),
				req.queryParams("CallSid"), req.queryParams("From"), req.queryParams("To"), req.queryParams("CallStatus"),
				req.queryParams("ApiVersion"), req.queryParams("Direction"), req.queryParams("forwardedFrom"));
	}
}
