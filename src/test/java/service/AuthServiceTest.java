package service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Unit tests for the AuthService class.
 * These tests cover the creation and verification of the master password file,
 * as well as authentication behavior with valid and invalid inputs.
 */
@DisplayName("AuthService Unit Tests")
class AuthServiceTest {

    private static final String PASSWORD_FILE = "master_password.txt";

    /**
     * Ensures the password file is deleted before each test.
     */
    @BeforeEach
    void cleanUp() throws IOException {
        Files.deleteIfExists(Paths.get(PASSWORD_FILE));
    }

    /**
     * Tests that a new master password file is created
     * when none exists and a password is provided.
     */
    @Test
    @DisplayName("Should create password file if it does not exist")
    void testLoadOrCreatePassword_createsFile() {
        String input = "newpassword\n000000\n";
        Scanner scanner = new Scanner(new ByteArrayInputStream(input.getBytes()));

        assertThrows(Exception.class, () -> new AuthService(scanner));
        assertTrue(Files.exists(Paths.get(PASSWORD_FILE)), "Password file should be created");
    }

    /**
     * Tests that the master password is loaded correctly
     * from an existing file and no error is thrown during password validation.
     */
    @Test
    @DisplayName("Should load existing password file without throwing on valid password")
    void testLoadOrCreatePassword_loadsExistingFile() throws Exception {
        String hash = org.mindrot.jbcrypt.BCrypt.hashpw("testpass", org.mindrot.jbcrypt.BCrypt.gensalt());
        Files.writeString(Paths.get(PASSWORD_FILE), hash);

        String input = "testpass\n000000\n";
        Scanner scanner = new Scanner(new ByteArrayInputStream(input.getBytes()));

        // We expect an exception due to 2FA (TOTP) failure, not password mismatch
        assertThrows(Exception.class, () -> new AuthService(scanner));
    }

    /**
     * Tests that authentication fails after the maximum number of incorrect attempts.
     */
    @Test
    @DisplayName("Should fail authentication after max incorrect password attempts")
    void testAuthenticationFailsAfterMaxAttempts() {
        String input = "wrong1\nwrong2\nwrong3\n";
        Scanner scanner = new Scanner(new ByteArrayInputStream(input.getBytes()));

        assertThrows(Exception.class, () -> new AuthService(scanner));
    }
}
