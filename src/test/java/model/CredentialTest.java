package model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link Credential} record.
 * These tests validate proper creation, behavior of {@code toString()},
 * equality and hashCode consistency.
 */
class CredentialTest {

    @Test
    @DisplayName("Should create Credential with correct field values")
    void testCredentialCreation() {
        Credential credential = new Credential("Gmail", "user@example.com", "encryptedPass123");

        assertNotNull(credential);
        assertEquals("Gmail", credential.serviceName());
        assertEquals("user@example.com", credential.username());
        assertEquals("encryptedPass123", credential.encryptedPassword());
    }

    @Test
    @DisplayName("Should return expected string from toString()")
    void testToString() {
        Credential credential = new Credential("Gmail", "user@example.com", "encryptedPass123");
        String expected = "Service: Gmail, Username: user@example.com";

        assertEquals(expected, credential.toString());
    }

    @Test
    @DisplayName("Should consider credentials equal when fields match")
    void testEquality() {
        Credential credential1 = new Credential("Gmail", "user@example.com", "encryptedPass123");
        Credential credential2 = new Credential("Gmail", "user@example.com", "encryptedPass123");
        Credential differentCredential = new Credential("Outlook", "user@example.com", "encryptedPass123");

        assertEquals(credential1, credential2);
        assertNotEquals(credential1, differentCredential);
    }

    @Test
    @DisplayName("Should return same hashCode for equal credentials")
    void testHashCode() {
        Credential credential1 = new Credential("Gmail", "user@example.com", "encryptedPass123");
        Credential credential2 = new Credential("Gmail", "user@example.com", "encryptedPass123");

        assertEquals(credential1.hashCode(), credential2.hashCode());
    }
}
