package service;

import model.Credential;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

public class CredentialStorage {
	private static final Path FILE_PATH = Paths.get("credentials.dat");

	/**
	 * Save a list of credentials to an encrypted file.
	 */
	public static void saveCredentials(List<Credential> credentials) throws Exception {
		List<String> encryptedLines = new ArrayList<>();

		for (Credential cred : credentials) {
			if (cred.serviceName() == null || cred.username() == null || cred.encryptedPassword() == null) {
				System.err.println("Skipping invalid credential (null fields).");
				continue;
			}

			String line = cred.serviceName() + "," + cred.username() + "," + cred.encryptedPassword();
			String encrypted = EncryptionService.encrypt(line);
			encryptedLines.add(encrypted);
		}

		// Optionally, create a backup
		if (Files.exists(FILE_PATH)) {
			Files.copy(FILE_PATH, Paths.get("credentials_backup.dat"), StandardCopyOption.REPLACE_EXISTING);
		}

		try (BufferedWriter writer = Files.newBufferedWriter(FILE_PATH)) {
			for (String line : encryptedLines) {
				writer.write(line);
				writer.newLine();
			}
		} catch (IOException e) {
			throw new IOException("Error writing to credentials file: " + e.getMessage(), e);
		}
	}

	/**
	 * Load and decrypt credentials from the file.
	 */
	public static List<Credential> loadCredentials() throws Exception {
		List<Credential> credentials = new ArrayList<>();

		if (!Files.exists(FILE_PATH)) {
			return credentials;
		}

		try (BufferedReader reader = Files.newBufferedReader(FILE_PATH)) {
			String line;
			while ((line = reader.readLine()) != null) {
				try {
					String decrypted = EncryptionService.decrypt(line);
					String[] parts = decrypted.split(",", 3);
					if (parts.length == 3) {
						credentials.add(new Credential(parts[0], parts[1], parts[2]));
					} else {
						System.err.println("Invalid line format: " + decrypted);
					}
				} catch (Exception ex) {
					System.err.println("Error decrypting line: " + ex.getMessage());
				}
			}
		} catch (IOException e) {
			throw new IOException("Error reading credentials file: " + e.getMessage(), e);
		}

		return credentials;
	}
}
