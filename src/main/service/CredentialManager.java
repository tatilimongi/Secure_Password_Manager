package service;

import model.Credential;
import utils.PasswordGenerator;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;

import java.util.*;

public class CredentialManager {
	private List<Credential> credentials;
	private final Scanner scanner = new Scanner(System.in);

	public CredentialManager(List<Credential> credentials) {
		this.credentials = credentials;
	}

	public void showMenu() {
		int option;
		do {
			System.out.println("\n===== Credential Manager =====");
			System.out.println("1. List credentials");
			System.out.println("2. Add credential");
			System.out.println("3. Remove credential");
			System.out.println("4. Search by service");
			System.out.println("5. Show decrypted password");
			System.out.println("6. Copy decrypted password to clipboard");
			System.out.println("7. Exit");
			System.out.print("Choose an option: ");
			option = getIntInput();

			switch (option) {
				case 1 -> listCredentials();
				case 2 -> addCredential();
				case 3 -> removeCredential();
				case 4 -> searchCredential();
				case 5 -> showDecryptedPassword();
				case 6 -> copyPasswordToClipboard();
				case 7 -> saveAndExit();
				default -> System.out.println("Invalid option. Try again.");
			}
		} while (option != 7);
	}

	private void listCredentials() {
		if (credentials.isEmpty()) {
			System.out.println("No credentials stored.");
			return;
		}
		System.out.println("Stored Credentials:");
		for (int i = 0; i < credentials.size(); i++) {
			Credential c = credentials.get(i);
			System.out.printf("%d. Service: %s | Username: %s | Password: %s%n",
					i + 1, c.getServiceName(), c.getUsername(), c.getEncryptedPassword());
		}
	}

	private void addCredential() {
		System.out.print("Enter service name: ");
		String service = scanner.nextLine().trim();
		System.out.print("Enter username: ");
		String username = scanner.nextLine().trim();
		System.out.print("Generate strong password? (y/n): ");
		String choice = scanner.nextLine().trim().toLowerCase();
		String password;
		if (choice.equals("y")) {
			password = PasswordGenerator.generate(16);
			System.out.println("Generated password: " + password);
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

	private void removeCredential() {
		listCredentials();
		if (credentials.isEmpty()) return;

		System.out.print("Enter number to remove: ");
		int index = getIntInput() - 1;
		if (index >= 0 && index < credentials.size()) {
			Credential removed = credentials.remove(index);
			System.out.println("Removed: " + removed.getServiceName());
		} else {
			System.out.println("Invalid index.");
		}
	}

	private void searchCredential() {
		System.out.print("Search for service: ");
		String query = scanner.nextLine().trim().toLowerCase();
		boolean found = false;
		for (Credential c : credentials) {
			if (c.getServiceName().toLowerCase().contains(query)) {
				System.out.printf("Service: %s | Username: %s | Password: %s%n",
						c.getServiceName(), c.getUsername(), c.getEncryptedPassword());
				found = true;
			}
		}
		if (!found) {
			System.out.println("No match found.");
		}
	}

	private void showDecryptedPassword() {
		if (credentials.isEmpty()) {
			System.out.println("No credentials stored.");
			return;
		}

		listCredentials();
		System.out.print("Enter number to decrypt: ");
		int index = getIntInput() - 1;

		if (index < 0 || index >= credentials.size()) {
			System.out.println("Invalid index.");
			return;
		}

		System.out.print("Re-enter master password to confirm: ");
		String inputPassword = scanner.nextLine();

		try {
			List<String> lines = Files.readAllLines(Paths.get("auth.dat"));
			String storedHash = lines.get(0);

			if (!BCrypt.checkpw(inputPassword, storedHash)) {
				System.out.println("Incorrect master password. Access denied.");
				return;
			}

			Credential selected = credentials.get(index);
			String decrypted = EncryptionService.decrypt(selected.getEncryptedPassword());
			System.out.printf("Decrypted password for %s: %s%n", selected.getServiceName(), decrypted);
		} catch (IOException e) {
			System.err.println("Error reading auth.dat: " + e.getMessage());
		} catch (Exception e) {
			System.err.println("Error decrypting password: " + e.getMessage());
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
			List<String> lines = Files.readAllLines(Paths.get("auth.dat"));
			String storedHash = lines.get(0);

			if (!BCrypt.checkpw(inputPassword, storedHash)) {
				System.out.println("Incorrect master password. Access denied.");
				return;
			}

			Credential selected = credentials.get(index);
			String decrypted = EncryptionService.decrypt(selected.getEncryptedPassword());

			// Copiar para a área de transferência
			copyToClipboard(decrypted);
			System.out.printf("Password for %s copied to clipboard.%n", selected.getServiceName());

		} catch (IOException e) {
			System.err.println("Error reading auth.dat: " + e.getMessage());
		} catch (Exception e) {
			System.err.println("Error decrypting password: " + e.getMessage());
		}
	}

	private void copyToClipboard(String text) {
		try {
			java.awt.datatransfer.StringSelection stringSelection = new java.awt.datatransfer.StringSelection(text);
			java.awt.datatransfer.Clipboard clipboard = java.awt.Toolkit.getDefaultToolkit().getSystemClipboard();
			clipboard.setContents(stringSelection, null);
		} catch (Exception e) {
			System.err.println("Clipboard operation not supported: " + e.getMessage());
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
			return -1;
		}
	}
}
