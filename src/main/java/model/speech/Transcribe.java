package model.speech;

import model.Recording;
import model.Sql2oModel;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import util.FileDownload;

public class Transcribe {
    protected String url;
    protected String from;
    public static Logger logger = LoggerFactory.getLogger(Transcribe.class);
    
    public Transcribe() {
    	super();
    }

    public Transcribe(String u, String f) {
    	super();
    	url = u;
    	from = f;
    }

    
    public String transcribeURL() {
		String filename = FileDownload.generateWAVFilename();
		Recording r = new Recording(filename, url, from);
		long recordingID = Sql2oModel.getInstance().createRecording(r);
		r.id = (int)recordingID;
		logger.info("recording created: " + recordingID + " --> " + r.toString());
		
		try {
			Thread.sleep(2000);
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
		Sql2oModel.getInstance().updateRecording(r);
		
		return transcript;
    }
    
}