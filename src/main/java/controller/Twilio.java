package controller;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import com.twilio.twiml.Record;
import com.twilio.twiml.Redirect;
import com.twilio.twiml.VoiceResponse;
import com.twilio.twiml.Say;
import com.twilio.twiml.TwiMLException;
import com.twilio.twiml.Say.Voice;

import model.Recording;
import model.RecordingStatusCallback;
import model.Sql2oModel;
import model.speech.Transcribe;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import spark.Request;

public class Twilio {
	static int timeoutSeconds = 3;
	static String base = "http://www.resebot.com";
	
	public final static String introAction = "/voice/intro";
	public final static String recordAction = "/voice/record";
	public final static String guestsAction = "/voice/guests";
	public final static String processingWaitAction = "/voice/processingwait";
	public final static String confirmationAction = "/voice/confirmation";

    public static Logger logger = LoggerFactory.getLogger(Twilio.class);

    /** get("/voice/intro" */
    public static String createFirstPrompt(Request request) {
		RecordingStatusCallback params = extractCallbackParameters(request);
        logger.info("handling: " + introAction + ": " + params.toString());
		String prompt = Sql2oModel.getInstance().getPrompt(params.to, introAction).ptext; //TODO maybe check URL and make more robust (what if phone number does not exist?)
        Say say = new Say.Builder(prompt).voice(Voice.ALICE).build();
        Record record = new Record.Builder().action(base + recordAction).playBeep(false).timeout(timeoutSeconds).build();
		return toXML(new VoiceResponse.Builder().say(say).record(record).build());
    }
    
    /** post("/voice/record" */
    public static String handleRecord(Request request) {
    	return genericHandleRecord(request, recordAction, guestsAction);
	}

    /** post("/voice/guests" */
    public static String handleGuests(Request request) {
    	return genericHandleRecord(request, guestsAction, processingWaitAction);
	}
    
    /** post("/voice/processingwait" */
    public static String handleProcessingWait(Request request) {
    	String currentURL = processingWaitAction;
		RecordingStatusCallback params = extractCallbackParameters(request);
        logger.info("handling: " + currentURL + ": " + params.toString());
		String prompt = Sql2oModel.getInstance().getPrompt(params.to, currentURL).ptext; //TODO maybe check URL and make more robust (what if phone number does not exist?)
		Say say = new Say.Builder(prompt).voice(Voice.ALICE).build();
		Redirect redirect = new Redirect.Builder().url(confirmationAction).build();
		return toXML(new VoiceResponse.Builder().say(say).redirect(redirect)./*record(record).*/build());
	}
    
    /** post("/voice/confirmation" */
    public static String handleConfirmation(Request request) {
    	String currentURL = confirmationAction;
    	RecordingStatusCallback params = extractCallbackParameters(request);
    	logger.info("handling: " + currentURL + ": " + params.toString());
    	String confirmationText = extractConfirmationText(params);
    	logger.info("confirmationText : " + confirmationText); 
    	String prompt = confirmationText + Sql2oModel.getInstance().getPrompt(params.to, currentURL).ptext; //TODO maybe check URL and make more robust (what if phone number does not exist?)
    	Say say = new Say.Builder(prompt).voice(Voice.ALICE).build();
    	return toXML(new VoiceResponse.Builder().say(say).build());
    }

    private static String genericHandleRecord(Request request, String currentURL, String nextURL) {
		RecordingStatusCallback params = extractCallbackParameters(request);
        logger.info("handling: " + currentURL + ": " + params.toString());
		String prompt = Sql2oModel.getInstance().getPrompt(params.to, currentURL).ptext; //TODO maybe check URL and make more robust (what if phone number does not exist?)
		Say say = new Say.Builder(prompt).voice(Voice.ALICE).build();
        Record record = new Record.Builder().action(base + nextURL).playBeep(false).timeout(timeoutSeconds).build();
        
        if (params.recordingUrl != null && params.recordingUrl.length() > 0 && params.recordingUrl.startsWith("http")) {
        	Transcribe t = new Transcribe(params.recordingUrl , params.from, params.to, currentURL);
        	final CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
        		return t.transcribeURL(); //...long running... (from http://www.nurkiewicz.com/2013/05/java-8-definitive-guide-to.html)
        	});
        }
		
		return toXML(new VoiceResponse.Builder().say(say).record(record).build());
	}
    
	private static String toXML(VoiceResponse response) {
		try {
			String xml = response.toXml();
			logger.info(xml);
			return xml;
		} catch (TwiMLException e) {
			logger.error("toXML failed: ", e);
			return "";
		}
	}
	
	private static RecordingStatusCallback extractCallbackParameters(Request req) {
		return new RecordingStatusCallback(req.queryParams("RecordingSid"), req.queryParams("RecordingUrl"),
				req.queryParams("CallSid"), req.queryParams("From"), req.queryParams("To"), req.queryParams("CallStatus"),
				req.queryParams("ApiVersion"), req.queryParams("Direction"), req.queryParams("forwardedFrom"));
	}
	
	private static String extractConfirmationText(RecordingStatusCallback params) {
		String val = "";
		List<Recording> recordings = Sql2oModel.getInstance().getRecordings(params.from, params.to);
		for (Recording recording : recordings) {
			if (guestsAction.equals(recording.urlcalled)) {
				val += recording.parsedtext + ". ";
			}
			if (recordAction.equals(recording.urlcalled)) {
				val += recording.parsedtext + ". ";
			}
		}
		return val;
	}
}
