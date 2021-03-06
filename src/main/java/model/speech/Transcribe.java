package model.speech;

import java.util.List;

import model.Recording;
import model.Sql2oModel;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import util.FileDownload;

public class Transcribe {
    private static final int WAIT_BEFORE_DOWNLOADING_WAV = 2000; // milliseconds
	protected String url;
    protected String from;
    protected String to;
    protected String action;
    
    public static Logger logger = LoggerFactory.getLogger(Transcribe.class);
    
    public Transcribe() {
    	super();
    }

    public Transcribe(String u, String f, String t, String a) {
    	super();
    	url = u;
    	from = f;
    	to = t;
    	action = a;
    }
    
    public String transcribeURL() {
		String filename = FileDownload.generateWAVFilename();
		Recording r = new Recording(filename, url, from, to, action);
		long recordingID = Sql2oModel.getInstance().createRecording(r);
		r.id = (int)recordingID;
		logger.info("recording created: " + recordingID + " --> " + r.toString());
		
		try {
			Thread.sleep(WAIT_BEFORE_DOWNLOADING_WAV);
		} catch (InterruptedException ie) {
			logger.error("failed to wait for a few seconds", ie);
			Thread.currentThread().interrupt();
		}

		util.FileDownload.download(url, filename);

		String transcript = "";
		try {
 			long startTS = System.currentTimeMillis();
			transcript += QuickstartSample.process(filename);
			logger.info("Quickstart transcript: " + transcript + "    " + (System.currentTimeMillis() - startTS) + "ms");
		} catch (Exception e) {
			logger.error("failed to run google speech recognition", e);
		}
		
		r.status = 1;
		r.parsedtext = transcript;
		r.conversation = getConversationID();
		Sql2oModel.getInstance().updateRecording(r);
		
		return transcript;
    }
    
    public int getConversationID() {
    	int max = 0;
    	List<Recording> recordings = Sql2oModel.getInstance().getRecordings(from, to);
    	for (Recording recording : recordings) {
    		logger.debug("getConversationID(): " + recording);
    		if (recording.conversation > max) { 
    			max = recording.conversation;
    		}
    	}
    	return max == 0 ? Sql2oModel.getInstance().getMaximumConversationFromRecordings() + 1 : max; //TODO very important - need to double check
    }
}
