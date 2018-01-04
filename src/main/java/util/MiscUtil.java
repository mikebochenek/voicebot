package util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class MiscUtil {
	private static String salt = "ax56;WU98,.A";

	public static String hash(String text) {
		MessageDigest digest = null;
		try {
			digest = MessageDigest.getInstance("SHA-256");
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
		}
		return (digest != null ? new String(Base64.getEncoder().encode(
				(digest.digest((salt + text).getBytes(StandardCharsets.UTF_8))))) : text);
	}

}
