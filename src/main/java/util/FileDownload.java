package util;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;


import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

public class FileDownload {
	public static Logger logger = LoggerFactory.getLogger(FileDownload.class);
			
	/**
	 * download URL and save to a local file
	 * https://stackoverflow.com/questions/921262/how-to-download-and-save-a-file-from-internet-using-java
	 * @param url
	 * @param filename
	 */
	public static void download(String url, String filename) {
		try {
			URL website = new URL(url);
			ReadableByteChannel rbc = Channels.newChannel(website.openStream());
			FileOutputStream fos = new FileOutputStream(filename);
			long bytestrasferred = fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
			logger.info("downloaded: " + url + " - bytes: " + bytestrasferred);
			fos.close();
		} catch (IOException e) {
			logger.error("failed download of: " + url, e);
		}
	}
	
	/**
	 * create random filename for .wav files
	 * @return new filename
	 */
	public static String generateWAVFilename() {
		String path = System.getProperty("data.local.path", "/tmp");
		if (!path.endsWith("/")) {
			path += "/";
		}
		path += "speech-" + System.currentTimeMillis() + ".wav";
		return path;
	}
}
