package service;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import org.mindrot.jbcrypt.BCrypt;

/**
 * AuthService handles master-password and TOTP-based authentication.
 */
public class AuthService {
	private static final String AUTH_FILE = "auth.dat";

	public AuthService() {
		File file = new File(AUTH_FILE);
		if (!file.exists()) {
			System.out.println("=== First time setup ===");
			initialSetup();
		} else {
			System.out.println("=== Authentication Required ===");
			authenticate();
		}
	}

	/**
	 * First-time setup: asks for a password, hashes it, generates a TOTP secret,
	 * and stores both securely in auth.dat.
	 */
	private void initialSetup() {
		Scanner scanner = new Scanner(System.in);
		System.out.print("Set your master password: ");
		String plainPassword = scanner.nextLine();

		String hashedPassword = BCrypt.hashpw(plainPassword, BCrypt.gensalt());
		String secret = TOTPService.generateSecret();

		System.out.println("\nYour TOTP secret (store it in your 2FA app):");
		System.out.println(secret);

		try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(AUTH_FILE))) {
			writer.write(hashedPassword);
			writer.newLine();
			writer.write(secret);
			System.out.println("\nSetup complete. Restart the application to login.");
		} catch (IOException e) {
			System.err.println("Error writing to auth.dat: " + e.getMessage());
		}
	}

	/**
	 * Performs authentication with master password and TOTP code.
	 */
	private void authenticate() {
		int attempts = 0;
		final int MAX_ATTEMPTS = 3;

		try {
			List<String> lines = Files.readAllLines(Paths.get(AUTH_FILE));
			if (lines.size() < 2) {
				System.err.println("Invalid auth.dat format.");
				return;
			}

			String storedHash = lines.get(0);
			String storedSecret = lines.get(1);

			while (attempts < MAX_ATTEMPTS) {
				System.out.print("Enter master password: ");
				String inputPassword = scanner.nextLine();

				if (!BCrypt.checkpw(inputPassword, storedHash)) {
					attempts++;
					System.out.println("Incorrect password. Attempts left: " + (MAX_ATTEMPTS - attempts));
					logAttempt("FAIL_PASSWORD");
					continue;
				}

				System.out.print("Enter TOTP code: ");
				String code = scanner.nextLine();

				if (TOTPService.verifyCode(storedSecret, code)) {
					System.out.println("Authentication successful. Access granted.");
					logAttempt("SUCCESS");
					return;
				} else {
					attempts++;
					System.out.println("Invalid TOTP code. Attempts left: " + (MAX_ATTEMPTS - attempts));
					logAttempt("FAIL_TOTP");
				}
			}

			System.out.println("Too many failed attempts. Exiting.");
			logAttempt("LOCKOUT");

		} catch (IOException e) {
			System.err.println("Error reading auth.dat: " + e.getMessage());
		}
	}

	public static void main(String[] args) {
		new AuthService();
	}

	private void logAttempt(String result) {
		String logLine = String.format("%s - [%s] %s",
				java.time.LocalDateTime.now(),
				result,
				System.getProperty("user.name") // Pode usar IP ou username real no futuro
		);

		try (BufferedWriter writer = new BufferedWriter(new FileWriter("access.log", true))) {
			writer.write(logLine);
			writer.newLine();
		} catch (IOException e) {
			System.err.println("Error writing to access.log: " + e.getMessage());
		}
	}

}
