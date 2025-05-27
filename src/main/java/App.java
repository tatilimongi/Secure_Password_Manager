import model.Credential;
import service.AuthService;
import service.CredentialStorage;
import service.EncryptionService;
import service.TOTPService;
import utils.PasswordGenerator;

import java.util.List;
import java.util.Scanner;

public class App {

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);

		try {
			new AuthService(scanner); // Initialize authentication
		} catch (Exception e) {
			System.err.println("Authentication failed: " + e.getMessage());
			return;
		}

//		// Generate and display TOTP Base32 secret for initial setup
//		String base64Secret = TOTPService.generateSecret();
//		String base32Secret = TOTPService.getBase32Secret(base64Secret);
//		System.out.println("\nYour TOTP secret (store it in your 2FA app like Google Authenticator):");
//		System.out.println(base32Secret);
//		System.out.println("(This secret is generated every time the app starts. Persist it if needed!)");

		List<Credential> credentials;

		try {
			credentials = CredentialStorage.loadCredentials();
		} catch (Exception e) {
			System.err.println("Failed to load credentials: " + e.getMessage());
			return;
		}

		while (true) {
			System.out.println("\n=== Credential Manager ===");
			System.out.println("1. List all credentials");
			System.out.println("2. Add new credential");
			System.out.println("3. Delete a credential");
			System.out.println("4. Copy password to clipboard");
			System.out.println("5. Exit");
			System.out.print("Choose an option: ");

			String option = safeReadLine(scanner);
			if (option == null) {
				System.out.println("\nInput ended unexpectedly. Exiting...");
				break;
			}

			switch (option) {
				case "1" -> {
					if (credentials.isEmpty()) {
						System.out.println("No credentials stored.");
					} else {
						System.out.println("\nStored credentials:");
						int i = 1;
						for (Credential c : credentials) {
							System.out.println(i++ + ". " + c.serviceName() + " | " + c.username());
						}
					}
				}

				case "2" -> {
					System.out.print("Service name: ");
					String service = safeReadLine(scanner);
					if (service == null) break;

					System.out.print("Username: ");
					String user = safeReadLine(scanner);
					if (user == null) break;

					System.out.print("Generate a strong password automatically? (y/n): ");
					String choice = safeReadLine(scanner);
					if (choice == null) break;
					choice = choice.trim().toLowerCase();

					String pass;
					if (choice.equals("y")) {
						pass = PasswordGenerator.generate(16, true, true, true, true);
						System.out.println("Generated password: " + pass);
					} else {
						System.out.print("Password: ");
						pass = safeReadLine(scanner);
						if (pass == null) break;
					}

					try {
						String encryptedPassword = EncryptionService.encrypt(pass);
						credentials.add(new Credential(service, user, encryptedPassword));
						CredentialStorage.saveCredentials(credentials);
						System.out.println("Credential saved.");
					} catch (Exception e) {
						System.err.println("Error saving credential: " + e.getMessage());
					}
				}

				case "3" -> {
					if (credentials.isEmpty()) {
						System.out.println("No credentials to delete.");
						break;
					}

					System.out.print("Enter number of credential to delete: ");
					String input = safeReadLine(scanner);
					if (input == null) break;

					int indexToDelete;
					try {
						indexToDelete = Integer.parseInt(input) - 1;
					} catch (NumberFormatException e) {
						System.out.println("Invalid number format.");
						break;
					}

					if (indexToDelete >= 0 && indexToDelete < credentials.size()) {
						Credential removed = credentials.remove(indexToDelete);
						try {
							CredentialStorage.saveCredentials(credentials);
							System.out.println("Removed: " + removed.serviceName());
						} catch (Exception e) {
							System.err.println("Error saving credentials after deletion: " + e.getMessage());
						}
					} else {
						System.out.println("Invalid index.");
					}
				}

				case "4" -> {
					if (credentials.isEmpty()) {
						System.out.println("No credentials to copy.");
						break;
					}

					System.out.println("Choose a credential to copy password:");
					for (int j = 0; j < credentials.size(); j++) {
						System.out.println((j + 1) + ". " + credentials.get(j).serviceName());
					}
					String input = safeReadLine(scanner);
					if (input == null) break;

					int idx;
					try {
						idx = Integer.parseInt(input) - 1;
					} catch (NumberFormatException e) {
						System.out.println("Invalid number format.");
						break;
					}

					if (idx >= 0 && idx < credentials.size()) {
						try {
							String decrypted = EncryptionService.decrypt(credentials.get(idx).encryptedPassword());
							java.awt.Toolkit.getDefaultToolkit()
									.getSystemClipboard()
									.setContents(new java.awt.datatransfer.StringSelection(decrypted), null);
							System.out.println("Password copied to clipboard.");
						} catch (Exception e) {
							System.err.println("Error decrypting password: " + e.getMessage());
						}
					} else {
						System.out.println("Invalid index.");
					}
				}

				case "5" -> {
					System.out.println("Exiting...");
					return;
				}

				default -> System.out.println("Invalid option.");
			}
		}


	}

	private static String safeReadLine(Scanner scanner) {
		try {
			if (scanner.hasNextLine()) {
				return scanner.nextLine();
			} else {
				return null;
			}
		} catch (Exception e) {
			System.err.println("Error reading input: " + e.getMessage());
			return null;
		}
	}

}
