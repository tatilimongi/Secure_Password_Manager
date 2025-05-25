package service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;
import java.time.Instant;
import java.util.Base64;

public class TOTPService {
	private static final long TIME_STEP_SECONDS = 30;
	private static final int CODE_DIGITS = 6;
	private static final String HMAC_ALGORITHM = "HmacSHA1";

	/**
	 * Generates a new Base64-encoded secret key using secure random bytes.
	 * This key should be stored securely and associated with the user.
	 */
	public static String generateSecret() {
		byte[] randomBytes = new byte[20]; // 160 bits
		new SecureRandom().nextBytes(randomBytes);
		return Base64.getEncoder().encodeToString(randomBytes);
	}

	/**
	 * Validates a TOTP code entered by the user, allowing a small window of time drift.
	 *
	 * @param base64Secret The Base64-encoded secret key.
	 * @param inputCode    The TOTP code entered by the user.
	 * @return true if the code is valid; false otherwise.
	 */
	public static boolean validateCode(String base64Secret, String inputCode) {
		if (inputCode == null || inputCode.length() != CODE_DIGITS || !inputCode.matches("\\d+")) {
			return false;
		}

		try {
			long currentWindow = Instant.now().getEpochSecond() / TIME_STEP_SECONDS;
			for (long offset = -1; offset <= 1; offset++) {
				String expectedCode = generateCodeAtTime(base64Secret, currentWindow + offset);
				if (expectedCode.equals(inputCode)) {
					return true;
				}
			}
		} catch (Exception e) {
			System.err.println("TOTP validation failed: " + e.getMessage());
		}
		return false;
	}

	/**
	 * Generates the TOTP code for the current time.
	 *
	 * @param base64Secret The Base64-encoded secret key.
	 * @return The TOTP code as a String.
	 */
	public static String generateCurrentCode(String base64Secret) throws Exception {
		long timeWindow = Instant.now().getEpochSecond() / TIME_STEP_SECONDS;
		return generateCodeAtTime(base64Secret, timeWindow);
	}

	/**
	 * Generates a TOTP code for a specific time window.
	 *
	 * @param base64Secret The Base64-encoded secret key.
	 * @param timeWindow   The time window to generate the code for.
	 * @return The TOTP code as a String.
	 */
	private static String generateCodeAtTime(String base64Secret, long timeWindow) throws Exception {
		byte[] key = Base64.getDecoder().decode(base64Secret);
		byte[] data = new byte[8];
		for (int i = 7; i >= 0; i--) {
			data[i] = (byte) (timeWindow & 0xFF);
			timeWindow >>= 8;
		}

		Mac mac = Mac.getInstance(HMAC_ALGORITHM);
		mac.init(new SecretKeySpec(key, HMAC_ALGORITHM));
		byte[] hmac = mac.doFinal(data);

		int offset = hmac[hmac.length - 1] & 0xF;
		int binary = ((hmac[offset] & 0x7F) << 24)
				| ((hmac[offset + 1] & 0xFF) << 16)
				| ((hmac[offset + 2] & 0xFF) << 8)
				| (hmac[offset + 3] & 0xFF);

		int otp = binary % (int) Math.pow(10, CODE_DIGITS);
		return String.format("%0" + CODE_DIGITS + "d", otp);
	}
}
