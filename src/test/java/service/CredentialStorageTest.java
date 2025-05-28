package service;

import model.Credential;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CredentialStorageTest {

	private static final Path TEST_FILE_PATH = Paths.get("credentials.dat");
	private static final Path BACKUP_FILE_PATH = Paths.get("credentials_backup.dat");

	@BeforeEach
	void setUp() throws Exception {
		// Ensure test files are removed before each test
		Files.deleteIfExists(TEST_FILE_PATH);
		Files.deleteIfExists(BACKUP_FILE_PATH);
	}

	@AfterEach
	void tearDown() throws Exception {
		// Clean up any files created during the tests
		Files.deleteIfExists(TEST_FILE_PATH);
		Files.deleteIfExists(BACKUP_FILE_PATH);
	}

	@Test
	@DisplayName("Should save and load valid credentials successfully")
	void saveAndLoadCredentialsValidDataSuccess() throws Exception {
		List<Credential> credentials = new ArrayList<>();
		credentials.add(new Credential("service1", "user1", "pass1"));
		credentials.add(new Credential("service2", "user2", "pass2"));

		CredentialStorage.saveCredentials(credentials);
		List<Credential> loadedCredentials = CredentialStorage.loadCredentials();

		assertEquals(2, loadedCredentials.size());
		assertEquals("service1", loadedCredentials.getFirst().serviceName());
		assertEquals("user1", loadedCredentials.getFirst().username());
		assertEquals("pass1", loadedCredentials.getFirst().encryptedPassword());
	}

	@Test
	@DisplayName("Should skip credentials with null fields when saving")
	void saveCredentialsWithNullFieldsSkipsInvalidCredentials() throws Exception {
		List<Credential> credentials = new ArrayList<>();
		credentials.add(new Credential(null, "user1", "pass1"));
		credentials.add(new Credential("service2", "user2", "pass2"));

		CredentialStorage.saveCredentials(credentials);
		List<Credential> loadedCredentials = CredentialStorage.loadCredentials();

		assertEquals(1, loadedCredentials.size());
		assertEquals("service2", loadedCredentials.getFirst().serviceName());
	}

	@Test
	@DisplayName("Should return empty list when loading from an empty file")
	void loadCredentialsEmptyFileReturnsEmptyList() throws Exception {
		List<Credential> loadedCredentials = CredentialStorage.loadCredentials();
		assertTrue(loadedCredentials.isEmpty());
	}

	@Test
	@DisplayName("Should create a backup file when overwriting credentials")
	void saveCredentialsCreatesBackupFile() throws Exception {
		List<Credential> credentials = new ArrayList<>();
		credentials.add(new Credential("service1", "user1", "pass1"));

		CredentialStorage.saveCredentials(credentials); // First save
		CredentialStorage.saveCredentials(credentials); // Second save triggers backup

		assertTrue(Files.exists(BACKUP_FILE_PATH));
	}
}
