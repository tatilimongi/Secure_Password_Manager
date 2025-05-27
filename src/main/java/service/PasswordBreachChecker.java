package service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

public class PasswordBreachChecker {

	/**
	 * Checks if a password has been found in known data breaches using the "Have I Been Pwned API".
	 * @param password The password to check.
	 * @return Number of times the password was found in breaches (0 = safe).
	 */
	public static int checkPassword(String password) {
		try {
			// Step 1: SHA-1 hash of the password
			MessageDigest sha1 = MessageDigest.getInstance("SHA-1");
			byte[] hashBytes = sha1.digest(password.getBytes(StandardCharsets.UTF_8));
			StringBuilder sb = new StringBuilder();
			for (byte b : hashBytes) {
				sb.append(String.format("%02X", b));
			}
			String sha1Hash = sb.toString();
			String prefix = sha1Hash.substring(0, 5);
			String suffix = sha1Hash.substring(5);

			// Step 2: Query the API with the prefix
			URL url = new URL("https://api.pwnedpasswords.com/range/" + prefix);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");

			BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String line;
			while ((line = reader.readLine()) != null) {
				if (line.startsWith(suffix)) {
					String[] parts = line.split(":");
					return Integer.parseInt(parts[1].trim());
				}
			}
			reader.close();
			return 0; // Not found in breaches

		} catch (Exception e) {
			System.err.println("Error checking password breach: " + e.getMessage());
			return -1;
		}
	}
}
