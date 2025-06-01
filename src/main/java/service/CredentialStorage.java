package service;

import model.Credential;
import utils.InputSanitizer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

/**
 * Responsible for saving and loading credentials to and from an encrypted file.
 */
public class CredentialStorage {
    private static final Path FILE_PATH = Paths.get("credentials.dat");

    /**
     * Save a list of credentials to an encrypted file.
     *
     * @param credentials The list of credentials to save.
     * @throws Exception If an error occurs during encryption or file writing.
     */
    public static void saveCredentials(List<Credential> credentials) throws Exception {
        List<String> encryptedLines = new ArrayList<>();

        for (Credential cred : credentials) {
            String serviceName;
            String username;
            String encryptedPassword;

            try {
                // Ensure all fields are sanitized
                serviceName = InputSanitizer.sanitize(cred.serviceName(), 50, false);
                username = InputSanitizer.sanitize(cred.username(), 50, false);
                encryptedPassword = InputSanitizer.sanitize(cred.encryptedPassword(), 128, false);

                String line = String.format("%s,%s,%s", serviceName, username, encryptedPassword);
                encryptedLines.add(EncryptionService.encrypt(line));
            } catch (IllegalArgumentException e) {
                System.err.println("Skipping invalid credential: " + e.getMessage());
            }
        }

        // Create a backup of the current file if it exists
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
     *
     * @return A list of decrypted credentials.
     * @throws Exception If an error occurs during decryption or file reading.
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
                        // Sanitize and validate the decrypted parts
                        String serviceName = InputSanitizer.sanitize(parts[0], 50, false);
                        String username = InputSanitizer.sanitize(parts[1], 50, false);
                        String encryptedPassword = InputSanitizer.sanitize(parts[2], 128, false);

                        credentials.add(new Credential(serviceName, username, encryptedPassword));
                    } else {
                        System.err.println("Invalid line format: " + decrypted);
                    }
                } catch (IllegalArgumentException ex) {
                    System.err.println("Invalid credential format: " + ex.getMessage());
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