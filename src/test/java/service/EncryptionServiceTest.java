package service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class EncryptionServiceTest {

    @Test
    @DisplayName("Should derive the same key for same password and salt")
    void testGetSecretKeyConsistency() throws Exception {
        String password = "testPassword";
        String salt = "testSalt123";
        var key1 = EncryptionService.getSecretKey(password, salt);
        var key2 = EncryptionService.getSecretKey(password, salt);
        assertEquals(new String(key1.getEncoded()), new String(key2.getEncoded()));
    }

    @Test
    @DisplayName("Should encrypt and decrypt correctly with session key and salt")
    void testEncryptDecrypt() throws Exception {
        String password = "masterPass";
        String salt = "uniqueSalt!";
        EncryptionService.setSessionKeyAndSalt(password, salt);
        String original = "SensitiveData123!";
        String encrypted = EncryptionService.encrypt(original);
        String decrypted = EncryptionService.decrypt(encrypted);
        assertEquals(original, decrypted);
        EncryptionService.clearSessionKeyAndSalt();
    }

    @Test
    @DisplayName("Should throw when encrypting without session key and salt")
    void testEncryptWithoutSessionKey() {
        EncryptionService.clearSessionKeyAndSalt();
        assertThrows(IllegalStateException.class, () -> EncryptionService.encrypt("data"));
    }

    @Test
    @DisplayName("Should throw when decrypting with wrong session key")
    void testDecryptWithWrongKey() throws Exception {
        String password = "rightPass";
        String salt = "rightSalt";
        EncryptionService.setSessionKeyAndSalt(password, salt);
        String encrypted = EncryptionService.encrypt("Secret");
        EncryptionService.setSessionKeyAndSalt("wrongPass", salt);
        assertThrows(Exception.class, () -> EncryptionService.decrypt(encrypted));
        EncryptionService.clearSessionKeyAndSalt();
    }

    @Test
    @DisplayName("Should generate and persist salt")
    void testGetOrCreatePersistentSalt() throws Exception {
        String salt1 = EncryptionService.getOrCreatePersistentSalt();
        String salt2 = EncryptionService.getOrCreatePersistentSalt();
        assertEquals(salt1, salt2);
        assertEquals(24, salt1.length()); // 16 bytes base64 = 24 chars
    }
}
