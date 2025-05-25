import model.Credential;
import service.AuthService;
import service.CredentialStorage;
import service.EncryptionService;
import utils.PasswordGenerator;

import java.util.List;
import java.util.Scanner;

public class App {
	public static void main(String[] args) throws Exception {
		// Inicia autenticação
		AuthService auth = new AuthService();

		// Se chegou até aqui, a autenticação foi bem-sucedida
		Scanner scanner = new Scanner(System.in);
		List<Credential> credentials = CredentialStorage.loadCredentials();

		while (true) {
			System.out.println("\n=== Credential Manager ===");
			System.out.println("1. List all credentials");
			System.out.println("2. Add new credential");
			System.out.println("3. Delete a credential");
			System.out.println("4. Copy password to clipboard");
			System.out.println("5. Exit");
			System.out.print("Choose an option: ");
			String option = scanner.nextLine();

			switch (option) {
				case "1":
					System.out.println("\nStored credentials:");
					int i = 1;
					for (Credential c : credentials) {
						System.out.println(i++ + ". " + c.getServiceName() + " | " + c.getUsername());
					}
					break;

				case "2":
					System.out.print("Service name: ");
					String service = scanner.nextLine();
					System.out.print("Username: ");
					String user = scanner.nextLine();

					System.out.print("Generate a strong password automatically? (y/n): ");
					String choice = scanner.nextLine().trim().toLowerCase();
					String pass;

					if (choice.equals("y")) {
						pass = PasswordGenerator.generate(16, true, true, true, true);
						System.out.println("Generated password: " + pass);
					} else {
						System.out.print("Password: ");
						pass = scanner.nextLine();
					}

					String encryptedPassword = EncryptionService.encrypt(pass);
					credentials.add(new Credential(service, user, encryptedPassword));
					CredentialStorage.saveCredentials(credentials);
					System.out.println("Credential saved.");
					break;

				case "3":
					System.out.println("Enter number of credential to delete:");
					int indexToDelete = Integer.parseInt(scanner.nextLine()) - 1;
					if (indexToDelete >= 0 && indexToDelete < credentials.size()) {
						Credential removed = credentials.remove(indexToDelete);
						CredentialStorage.saveCredentials(credentials);
						System.out.println("Removed: " + removed.getServiceName());
					} else {
						System.out.println("Invalid index.");
					}
					break;

				case "4":
					System.out.println("Choose a credential to copy password:");
					for (int j = 0; j < credentials.size(); j++) {
						System.out.println((j + 1) + ". " + credentials.get(j).getServiceName());
					}
					int idx = Integer.parseInt(scanner.nextLine()) - 1;
					if (idx >= 0 && idx < credentials.size()) {
						String decrypted = EncryptionService.decrypt(credentials.get(idx).getEncryptedPassword());
						java.awt.Toolkit.getDefaultToolkit()
								.getSystemClipboard()
								.setContents(new java.awt.datatransfer.StringSelection(decrypted), null);
						System.out.println("Password copied to clipboard.");
					} else {
						System.out.println("Invalid index.");
					}
					break;

				case "5":
					System.out.println("Exiting...");
					return;

				default:
					System.out.println("Invalid option.");
			}
		}
	}
}
