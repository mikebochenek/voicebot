package controller;

import com.twilio.twiml.Play;
import com.twilio.twiml.Record;
import com.twilio.twiml.VoiceResponse;
import com.twilio.twiml.Say;
import com.twilio.twiml.TwiMLException;
import com.twilio.twiml.Say.Voice;

public class Twilio {
	static int timeoutSeconds = 3;
	
    public static String createFirstPrompt() {
        Say say = new Say.Builder("Hello World").voice(Voice.ALICE).build();
        Play play = new Play.Builder("https://api.twilio.com/Cowbell.mp3").build();
        Record record = new Record.Builder().action("url").playBeep(false).timeout(timeoutSeconds).build();
        
        VoiceResponse response = new VoiceResponse.Builder().say(say).play(play).record(record).build();

        try {
        	String xml = response.toXml();
            System.out.println(xml);
            return xml;
        } catch (TwiMLException e) {
            e.printStackTrace();
            return "";
        }
    }
}
