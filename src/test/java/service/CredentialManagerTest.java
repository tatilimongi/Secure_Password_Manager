package service;

import model.Credential;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CredentialManagerTest {

	private List<Credential> credentials;

	@BeforeEach
	void setUp() {
		credentials = new ArrayList<>();
	}

	@Test
	@DisplayName("Should add a credential to the list")
	void testAddCredential_AddsToList() {
		int initialSize = credentials.size();
		Credential testCred = new Credential("TestService", "testUser", "encryptedPass");
		credentials.add(testCred);

		assertEquals(initialSize + 1, credentials.size());
		assertEquals(testCred, credentials.getFirst());
	}

	@Test
	@DisplayName("Should remove a credential from the list")
	void testRemoveCredential_RemovesFromList() {
		Credential testCred = new Credential("TestService", "testUser", "encryptedPass");
		credentials.add(testCred);
		int initialSize = credentials.size();

		credentials.removeFirst();

		assertEquals(initialSize - 1, credentials.size());
		assertTrue(credentials.isEmpty());
	}

	@Test
	@DisplayName("Should initialize with an empty list")
	void testConstructor_InitializesWithEmptyList() {
		assertNotNull(credentials);
		assertTrue(credentials.isEmpty());
	}

	@Test
	@DisplayName("Should initialize with a provided list containing credentials")
	void testConstructor_InitializesWithProvidedList() {
		List<Credential> testList = new ArrayList<>();
		testList.add(new Credential("TestService", "testUser", "encryptedPass"));

		CredentialManager newManager = new CredentialManager(testList);

		assertNotNull(newManager);
		assertEquals(1, testList.size());
	}
}
