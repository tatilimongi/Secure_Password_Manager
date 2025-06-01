package service;

import model.Credential;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link CredentialStorage} class.
 * These tests validate the saving and loading of encrypted credentials
 * while ensuring complete isolation of test environments using temporary files.
 */
@DisplayName("CredentialStorage Unit Tests")
class CredentialStorageTest {

    private Path tempFile;          // Temporary file path for credentials.dat
    private Path tempBackupFile;    // Temporary file path for credentials_backup.dat

    /**
     * Sets up the environment by creating temporary files for testing
     * and initializing the encryption session.
     *
     * @throws Exception if an error occurs during setup.
     */
    @BeforeEach
    void setUp() throws Exception {
        // Create temporary files to isolate tests
        tempFile = Files.createTempFile("credentials", ".dat");
        tempBackupFile = Files.createTempFile("credentials_backup", ".dat");

        // Set a test master password and salt for encryption
        EncryptionService.setSessionKeyAndSalt("testMasterPassword", "testSalt123");

        // Ensure a persistent salt file is created for encryption (if required)
        Path saltPath = Path.of("encryption_salt.dat");
        if (!Files.exists(saltPath)) {
            Files.writeString(saltPath, "testSalt123", StandardOpenOption.CREATE);
        }
    }

    /**
     * Tests that credentials are correctly saved to disk and reloaded,
     * ensuring data consistency through encryption and decryption.
     *
     * @throws Exception if an error occurs during the test.
     */
    @Test
    @DisplayName("Should correctly save and load credentials")
    void testSaveAndLoadCredentials() throws Exception {
        // Create a list of credentials to save
        List<Credential> credentials = new ArrayList<>();
        credentials.add(new Credential("service1", "user1", EncryptionService.encrypt("pass1")));
        credentials.add(new Credential("service2", "user2", EncryptionService.encrypt("pass2")));

        // Save credentials to the file
        CredentialStorage.saveCredentials(credentials);

        // Load the credentials from the file
        List<Credential> loadedCredentials = CredentialStorage.loadCredentials();

        // Validate that the original and loaded credentials match
        assertEquals(2, loadedCredentials.size(), "The number of loaded credentials should match the original.");
        assertEquals("service1", loadedCredentials.getFirst().serviceName(), "Service name mismatch for first credential.");
        assertEquals("user1", loadedCredentials.getFirst().username(), "Username mismatch for first credential.");
        assertEquals("pass1", EncryptionService.decrypt(loadedCredentials.getFirst().encryptedPassword()), "Password mismatch for first credential.");
    }

    /**
     * Tests that loading credentials from a nonexistent file returns an empty list.
     *
     * @throws Exception if an error occurs during the test.
     */
    @Test
    @DisplayName("Should return an empty list when the credentials file does not exist")
    void testLoadCredentialsWhenFileDoesNotExist() throws Exception {
        // Remove any residual files to simulate the absence of a credential file
        Files.deleteIfExists(tempFile);
        Files.deleteIfExists(tempBackupFile);

        // Load credentials from the nonexistent file
        List<Credential> loadedCredentials = CredentialStorage.loadCredentials();

        // Validate that the returned list is empty
        assertNotNull(loadedCredentials, "The returned credentials list should not be null.");
        assertTrue(loadedCredentials.isEmpty(), "The returned list should be empty when the file is missing.");
    }

    /**
     * Cleans up temporary files and clears the encryption session after each test.
     *
     * @throws Exception if an error occurs during cleanup.
     */
    @AfterEach
    void tearDown() throws Exception {
        // Delete temporary files
        Files.deleteIfExists(tempFile);
        Files.deleteIfExists(tempBackupFile);

        // Clear the encryption session to reset the state
        EncryptionService.clearSessionKeyAndSalt();
    }
}