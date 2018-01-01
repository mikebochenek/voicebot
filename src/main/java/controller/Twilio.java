package controller;

import java.util.concurrent.CompletableFuture;

import com.twilio.twiml.Record;
import com.twilio.twiml.VoiceResponse;
import com.twilio.twiml.Say;
import com.twilio.twiml.TwiMLException;
import com.twilio.twiml.Say.Voice;

import model.speech.Transcribe;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import spark.Request;
import spark.Response;

public class Twilio {
	static int timeoutSeconds = 3;
	static String base = "http://www.resebot.com";

    public static Logger logger = LoggerFactory.getLogger(Twilio.class);
    
    public static String createFirstPrompt() {
        Say say = new Say.Builder("Hello World, please leave a message.").voice(Voice.ALICE).build();
        Record record = new Record.Builder().action(base + "/voice/record").playBeep(false).timeout(timeoutSeconds).build();
        
        VoiceResponse response = new VoiceResponse.Builder().say(say).record(record).build();

		try {
			String xml = response.toXml();
			logger.info(xml);
			return xml;
		} catch (TwiMLException e) {
			e.printStackTrace();
			return "";
		}
    }
	
    public static String handleRecord(Request request, Response response) {
		Say say = new Say.Builder("OK, done recording").voice(Voice.ALICE).build();

        String url = request.queryParams("RecordingUrl");
        String from = request.queryParams("from");
        logger.info("url: " + url + "  from: " + from);
        
        if (url != null && url.length() > 0 && url.startsWith("http")) {
        	Transcribe t = new Transcribe(url, from);
        	final CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
        		return t.transcribeURL(); //...long running... (from http://www.nurkiewicz.com/2013/05/java-8-definitive-guide-to.html)
        	});
        }
		
		VoiceResponse vresponse = new VoiceResponse.Builder().say(say).build();

		try {
			String xml = vresponse.toXml();
			logger.info(xml);
			return xml;
		} catch (TwiMLException e) {
			e.printStackTrace();
			return "";
		}
	}
}
