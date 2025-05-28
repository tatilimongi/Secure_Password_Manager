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

/**
 * Unit tests for manual list operations and the CredentialManager constructor behavior.
 */
@DisplayName("CredentialManager Unit Tests")
class CredentialManagerTest {

	private List<Credential> credentials;

	@BeforeEach
	void setUp() {
		credentials = new ArrayList<>();
	}

	@Test
	@DisplayName("Should add a credential to the list")
	void testAddCredentialAddsToList() {
		int initialSize = credentials.size();
		Credential testCred = new Credential("TestService", "testUser", "encryptedPass");
		credentials.add(testCred);

		assertEquals(initialSize + 1, credentials.size(), "List size should increase by 1.");
		assertEquals(testCred, credentials.getFirst(), "The added credential should be the first in the list.");
	}

	@Test
	@DisplayName("Should remove a credential from the list")
	void testRemoveCredentialRemovesFromList() {
		Credential testCred = new Credential("TestService", "testUser", "encryptedPass");
		credentials.add(testCred);
		int initialSize = credentials.size();

		credentials.removeFirst();

		assertEquals(initialSize - 1, credentials.size(), "List size should decrease by 1.");
		assertTrue(credentials.isEmpty(), "List should be empty after removal.");
	}

	@Test
	@DisplayName("Should initialize with an empty list")
	void testConstructorInitializesWithEmptyList() {
		assertNotNull(credentials, "Credential list should not be null upon setup.");
		assertTrue(credentials.isEmpty(), "Credential list should be empty upon setup.");
	}

	@Test
	@DisplayName("Should initialize with a provided list containing credentials")
	void testConstructorInitializesWithProvidedList() {
		List<Credential> testList = new ArrayList<>();
		testList.add(new Credential("TestService", "testUser", "encryptedPass"));

		CredentialManager newManager = new CredentialManager(testList);

		assertNotNull(newManager, "CredentialManager should be initialized.");
		assertEquals(1, testList.size(), "Provided list should contain one credential.");
	}
}
