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
		try {
			Thread.sleep(2000);
		} catch (InterruptedException ie) {
			logger.error("failed to wait for a few seconds", ie);
			Thread.currentThread().interrupt();
		}

		String filename = FileDownload.generateWAVFilename();
		util.FileDownload.download(url, filename);

		Recording r = new Recording(filename, url, from);
		long recordingID = Sql2oModel.getInstance().createRecording(r);
		logger.info("recording created: " + recordingID + " --> " + r.toString());

		String transcript = "";
		try {
			long startTS = System.currentTimeMillis();
			transcript += QuickstartSample.process(filename);
			logger.info("Quickstart transcript: " + transcript + "    " + (System.currentTimeMillis() - startTS) + "ms");
		} catch (Exception e) {
			logger.error("failed to run google speech recognition", e);
		}
		return transcript;
    }
    
}
