package service;

import utils.InputSanitizer;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.regex.Pattern;
import org.mindrot.jbcrypt.BCrypt;

/**
 * Handles user authentication with master password and TOTP verification.
 * It ensures secure access using hashed credentials and time-based codes.
 */
public class AuthService {

    private static final String PASSWORD_FILE = "master_password.dat";
    private static final int MAX_ATTEMPTS = 3;
    private static final int MAX_PASSWORD_LENGTH = 64;
    private static final int MAX_TOTP_LENGTH = 6;
    private static final Pattern NUMBER_PATTERN = Pattern.compile("\\d+");

    private final Scanner scanner;

    /**
     * Constructs the AuthService, initializing authentication with
     * password and TOTP validation.
     * Generates or loads a secure secret.
     *
     * @param scanner Scanner used for reading user input
     * @throws Exception if authentication fails after the maximum number of attempts
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
            try {
                System.out.print("\nEnter master password: ");
                String inputPassword = InputSanitizer.sanitize(scanner.nextLine(), MAX_PASSWORD_LENGTH, false);

                if (!BCrypt.checkpw(inputPassword, masterPasswordHash)) {
                    System.out.println("Incorrect password.");
                    continue;
                }

                System.out.print("Enter current TOTP code: ");
                String inputCode = InputSanitizer.sanitize(scanner.nextLine(), MAX_TOTP_LENGTH, true);

                if (!NUMBER_PATTERN.matcher(inputCode).matches()) {
                    throw new IllegalArgumentException("Only numbers are allowed for this field.");
                }

                if (TOTPService.validateCode(totpSecret, inputCode)) {
                    System.out.println("Authentication successful.");
                    sessionPassword = inputPassword;
                    break;
                }
            } catch (IllegalArgumentException ex) {
                System.out.println("Invalid input. " + InputSanitizer.escapeForLog(ex.getMessage()));
            }
        }

        if (sessionPassword == null) {
            throw new Exception("Authentication failed after maximum attempts.");
        }

        String salt = EncryptionService.getOrCreatePersistentSalt();
        EncryptionService.setSessionKeyAndSalt(sessionPassword, salt);
    }

    /**
     * Loads the existing master password hash or prompts the user to create a new one.
     * Checks the password against known data breaches and enforces a minimum length.
     *
     * @return The hashed master password
     * @throws Exception if reading or writing, the password file fails
     */
    String loadOrCreatePassword() throws Exception {
        Path path = Paths.get(PASSWORD_FILE);

        if (Files.exists(path)) {
            return Files.readString(path).trim();
        }

        System.out.println("No master password found. Please create one now.");

        String newPassword;

        while (true) {
            try {
                System.out.print("New password: ");
                newPassword = InputSanitizer.sanitize(scanner.nextLine(), MAX_PASSWORD_LENGTH, false);

                int breachCount = PasswordBreachChecker.checkPassword(newPassword);
                if (breachCount < 0) {
                    System.out.println("Error checking the password against known breaches. Please try again.");
                    continue;
                } else if (breachCount > 0) {
                    System.out.printf(
                            "This password has appeared in %d breach(es). Please choose a stronger password.%n",
                            breachCount
                    );
                    continue;
                }

                if (newPassword.length() < 8) {
                    System.out.println("Password must be at least 8 characters long. Please try again.");
                    continue;
                }

                System.out.print("Re-enter master password to confirm: ");
                String inputPassword = InputSanitizer.sanitize(scanner.nextLine(), MAX_PASSWORD_LENGTH, false);

                if (!newPassword.equals(inputPassword)) {
                    System.out.println("Passwords do not match. Please try again.");
                    continue;
                }

                break;
            } catch (IllegalArgumentException ex) {
                System.out.println("Invalid input. " + InputSanitizer.escapeForLog(ex.getMessage()));
            }
        }

        String hash = BCrypt.hashpw(newPassword, BCrypt.gensalt());
        Files.writeString(path, hash);
        System.out.println("Master password saved.");
        return hash;
    }
}