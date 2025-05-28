package service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;
import org.mindrot.jbcrypt.BCrypt;

/**
 * AuthService handles user authentication and two-factor verification using TOTP.
 * It prompts the user for password and TOTP code and validates access.
 */
public class AuthService {

	private static final String PASSWORD_FILE = "master_password.txt";
	private static final int MAX_ATTEMPTS = 3;

	private final Scanner scanner;

	/**
	 * Initializes the AuthService and performs the authentication process.
	 * It loads or creates the master password and validates TOTP.
	 *
	 * @param scanner A Scanner instance for reading user input.
	 * @throws Exception If authentication fails after multiple attempts.
	 */
	public AuthService(Scanner scanner) throws Exception {
		this.scanner = scanner;
		String masterPasswordHash = loadOrCreatePassword();
		String totpSecret = TOTPService.loadOrCreateSecret();

		System.out.println("\nTwo-Factor Authentication (TOTP) is enabled.");
		System.out.println("Use this secret in your authenticator app if not already registered:");
		System.out.println(TOTPService.getBase32Secret(totpSecret));
		System.out.println("Alternatively, scan a QR code using this URL:");
		System.out.println(TOTPService.getOtpAuthUrl(totpSecret, "user@example.com", "SecurePasswordManager"));

		String sessionPassword = null;
		for (int attempts = 1; attempts <= MAX_ATTEMPTS; attempts++) {
			System.out.print("\nEnter master password: ");
			String inputPassword = scanner.nextLine();

			if (!org.mindrot.jbcrypt.BCrypt.checkpw(inputPassword, masterPasswordHash)) {
				System.out.println("Incorrect password.");
				continue;
			}

			System.out.print("Enter current TOTP code: ");
			String inputCode = scanner.nextLine();

			if (TOTPService.validateCode(totpSecret, inputCode)) {
				System.out.println("Authentication successful.");
				sessionPassword = inputPassword;
				break;
			} else {
				System.out.println("Invalid TOTP code.");
			}
		}

		if (sessionPassword == null) {
			throw new Exception("Authentication failed after maximum attempts.");
		}

		// Set up encryption session key and salt
		String salt = service.EncryptionService.getOrCreatePersistentSalt();
		service.EncryptionService.setSessionKeyAndSalt(sessionPassword, salt);
	}

	/**
	 * Loads the master password from a file or creates a new one if not found.
	 *
	 * @return The master password.
	 * @throws Exception If the password cannot be read or written.
	 */
	private String loadOrCreatePassword() throws Exception {
		Path path = Paths.get(PASSWORD_FILE);
		if (Files.exists(path)) {
			return Files.readString(path).trim();
		}

		System.out.println("No master password found. Please create one now.");
		System.out.print("New password: ");
		String newPassword = scanner.nextLine();
		String hash = BCrypt.hashpw(newPassword, BCrypt.gensalt());
		Files.writeString(path, hash);
		System.out.println("Master password saved.");
		return hash;
	}
}
