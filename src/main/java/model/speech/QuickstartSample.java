package model.speech;

/* based on:
 * https://raw.githubusercontent.com/GoogleCloudPlatform/java-docs-samples/master/speech/cloud-client/src/main/java/com/example/speech/QuickstartSample.java
 */
//[START speech_quickstart]
//Imports the Google Cloud client library
import com.google.cloud.speech.v1.RecognitionAudio;
import com.google.cloud.speech.v1.RecognitionConfig;
import com.google.cloud.speech.v1.RecognitionConfig.AudioEncoding;
import com.google.cloud.speech.v1.RecognizeResponse;
import com.google.cloud.speech.v1.SpeechClient;
import com.google.cloud.speech.v1.SpeechRecognitionAlternative;
import com.google.cloud.speech.v1.SpeechRecognitionResult;
import com.google.protobuf.ByteString;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class QuickstartSample {
    public static Logger logger = LoggerFactory.getLogger(QuickstartSample.class);
	
	public static void main(String... args) throws Exception {
		// The path to the audio file to transcribe
		String fileName = "/tmp/test.wav";
		process(fileName);
	}

	public static String process(String fileName) throws IOException, Exception {
		// Instantiates a client
		SpeechClient speech = SpeechClient.create();

		// Reads the audio file into memory
		Path path = Paths.get(fileName);
		byte[] data = Files.readAllBytes(path);
		ByteString audioBytes = ByteString.copyFrom(data);

		// Builds the sync recognize request
		RecognitionConfig config = RecognitionConfig.newBuilder()
				.setEncoding(AudioEncoding.LINEAR16)
				//.setSampleRateHertz(16000)
				.setLanguageCode("en-US")
				.build();
		RecognitionAudio audio = RecognitionAudio.newBuilder().setContent(audioBytes).build();

		// Performs speech recognition on the audio file
		RecognizeResponse response = speech.recognize(config, audio);
		List<SpeechRecognitionResult> results = response.getResultsList();

		String output = "";
		for (SpeechRecognitionResult result : results) {
			// There can be several alternative transcripts for a given chunk of
			// speech. Just use the first (most likely) one here.
			SpeechRecognitionAlternative alternative = result.getAlternativesList().get(0);
			logger.info("Transcription: %s%n", alternative.getTranscript());
			output += alternative.getTranscript();
		}
		speech.close();
		return output;
	}
}
// [END speech_quickstart]