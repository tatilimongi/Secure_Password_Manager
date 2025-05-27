package service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class EncryptionServiceTest {

	@Test
	@DisplayName("Should encrypt and decrypt a string correctly")
	void testEncryptAndDecrypt() throws Exception {
		String originalText = "My secret password";

		String encryptedText = EncryptionService.encrypt(originalText);
		String decryptedText = EncryptionService.decrypt(encryptedText);

		assertNotEquals(originalText, encryptedText, "Encrypted text should differ from the original");
		assertEquals(originalText, decryptedText, "Decrypted text should match the original");
	}

	@Test
	@DisplayName("Should generate different encryption for the same text")
	void testEncryptRandomness() throws Exception {
		String text = "test123";

		String firstEncryption = EncryptionService.encrypt(text);
		String secondEncryption = EncryptionService.encrypt(text);

		assertNotEquals(firstEncryption, secondEncryption,
				"Encryption of the same text should differ due to random IV");
	}

	@Test
	@DisplayName("Should throw exception when decrypting invalid input")
	void testDecryptInvalidInput() {
		String invalidText = "invalidUnencryptedText";

		assertThrows(Exception.class,
				() -> EncryptionService.decrypt(invalidText),
				"Should throw exception when decrypting invalid text");
	}

	@Test
	@DisplayName("Should throw NullPointerException when encrypting null input")
	void testEncryptNullInput() {
		assertThrows(NullPointerException.class,
				() -> EncryptionService.encrypt(null),
				"Should throw NullPointerException when trying to encrypt null");
	}

}
