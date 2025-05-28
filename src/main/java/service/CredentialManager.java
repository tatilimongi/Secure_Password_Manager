package service;

import model.Credential;
import utils.PasswordGenerator;
import org.mindrot.jbcrypt.BCrypt;

import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.Toolkit;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

/**
 * Handles user interaction for managing credentials, including
 * listing, adding, removing, searching, decrypting, and copying passwords.
 */
public class CredentialManager {
	private final List<Credential> credentials;
	private final Scanner scanner = new Scanner(System.in);

	/**
	 * Initializes the credential manager with a list of credentials.
	 *
	 * @param credentials the credentials to manage
	 */
	public CredentialManager(List<Credential> credentials) {
		this.credentials = credentials;
	}

	/**
	 * Displays the interactive menu for managing credentials.
	 */
	public void showMenu() {
		while (true) {
			System.out.println("\n=== Credential Manager ===");
			System.out.println("1. List all credentials");
			System.out.println("2. Add new credential");
			System.out.println("3. Delete a credential");
			System.out.println("4. Copy password to clipboard");
			System.out.println("5. Check if any password has been compromised");
			System.out.println("6. Exit");
			System.out.print("Choose an option: ");
			String option = scanner.nextLine();

			switch (option) {
				case "1" -> listCredentials();
				case "2" -> addCredential();
				case "3" -> removeCredential();
				case "4" -> copyPasswordToClipboard();
				case "5" -> checkCompromisedPasswords();
				case "6" -> {
					saveAndExit();
					return;
				}
				default -> System.out.println("Invalid option. Try again.");
			}
		}
	}

	private void listCredentials() {
		if (credentials.isEmpty()) {
			System.out.println("No credentials stored.");
			return;
		}
		System.out.println("Stored Credentials:");
		for (int i = 0; i < credentials.size(); i++) {
			Credential c = credentials.get(i);
			System.out.printf("%d. Service: %s | Username: %s%n",
				i + 1, c.serviceName(), c.username());
		}
	}

	void addCredential() {
		System.out.print("Enter service name: ");
		String service = scanner.nextLine().trim();
		System.out.print("Enter username: ");
		String username = scanner.nextLine().trim();
		System.out.print("Generate strong password? (y/n): ");
		String choice = scanner.nextLine().trim().toLowerCase();
		String password;
		if (choice.equals("y")) {
			password = PasswordGenerator.generate(16, true, true, true, true);
			System.out.println("Generated password.");
		} else {
			System.out.print("Enter password: ");
			password = scanner.nextLine().trim();
		}

		try {
			String encryptedPassword = EncryptionService.encrypt(password);
			credentials.add(new Credential(service, username, encryptedPassword));
			System.out.println("Credential added successfully.");
		} catch (Exception e) {
			System.err.println("Error encrypting password: " + e.getMessage());
		}
	}

	void removeCredential() {
		listCredentials();
		if (credentials.isEmpty()) return;

		System.out.print("Enter number to remove: ");
		int index = getIntInput() - 1;
		if (index >= 0 && index < credentials.size()) {
			Credential removed = credentials.remove(index);
			System.out.println("Removed: " + removed.serviceName());
		} else {
			System.out.println("Invalid index.");
		}
	}

	private void copyPasswordToClipboard() {
		if (credentials.isEmpty()) {
			System.out.println("No credentials stored.");
			return;
		}

		listCredentials();
		System.out.print("Enter number to copy password: ");
		int index = getIntInput() - 1;

		if (index < 0 || index >= credentials.size()) {
			System.out.println("Invalid index.");
			return;
		}

		System.out.print("Re-enter master password to confirm: ");
		String inputPassword = scanner.nextLine();

		try {
			java.nio.file.Path passwordPath = java.nio.file.Paths.get("master_password.txt");
			if (!java.nio.file.Files.exists(passwordPath)) {
				System.out.println("master_password.txt file not found. Please set up your master password again.");
				return;
			}
			String storedHash = java.nio.file.Files.readAllLines(passwordPath).getFirst();

			if (!BCrypt.checkpw(inputPassword, storedHash)) {
				System.out.println("Incorrect master password. Access denied.");
				return;
			}

			Credential selected = credentials.get(index);
			String decrypted = EncryptionService.decrypt(selected.encryptedPassword());
			copyToClipboard(decrypted);
			System.out.printf("Password for %s copied to clipboard.%n", selected.serviceName());
		} catch (IOException e) {
			System.err.println("Error reading master_password.txt: " + e.getMessage());
		} catch (Exception e) {
			System.err.println("Error decrypting password: " + e.getMessage());
		}
	}

	private void copyToClipboard(String text) {
		try {
			StringSelection selection = new StringSelection(text);
			Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
			clipboard.setContents(selection, null);
		} catch (Exception e) {
			System.err.println("Clipboard operation not supported: " + e.getMessage());
		}
	}

	private void checkCompromisedPasswords() {
		if (credentials.isEmpty()) {
			System.out.println("No credentials stored.");
			return;
		}
		System.out.println("Checking all stored passwords for breaches...");
		boolean anyCompromised = false;
		for (Credential c : credentials) {
			try {
				String decrypted = EncryptionService.decrypt(c.encryptedPassword());
				int count = PasswordBreachChecker.checkPassword(decrypted);
				if (count > 0) {
					System.out.printf("WARNING: Password for service '%s' (username: %s) was found %d times in breaches!%n",
							c.serviceName(), c.username(), count);
					anyCompromised = true;
				}
			} catch (Exception e) {
				System.err.println("Error checking password for service '" + c.serviceName() + "': " + e.getMessage());
			}
		}
		if (!anyCompromised) {
			System.out.println("No compromised passwords found in your credentials.");
		}
	}

	private void saveAndExit() {
		try {
			CredentialStorage.saveCredentials(credentials);
			System.out.println("Credentials saved. Exiting...");
		} catch (Exception e) {
			System.err.println("Error saving credentials: " + e.getMessage());
		}
	}

	private int getIntInput() {
		try {
			return Integer.parseInt(scanner.nextLine().trim());
		} catch (NumberFormatException e) {
			System.out.println("Invalid input. Please enter a number.");
			return -1;
		}
	}
}
