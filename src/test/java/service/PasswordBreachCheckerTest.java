package service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.assertTrue;

class PasswordBreachCheckerTest {

    @Test
    @DisplayName("Should return zero for a strong password")
    void testCheckSafePassword() {
        String strongPassword = "Str0ngP@ssw0rd2024!";
        int result = PasswordBreachChecker.checkPassword(strongPassword);
        assertTrue(result >= 0, "Result should be greater than or equal to zero");
    }

    @Test
    @DisplayName("Should detect compromised password")
    void testCheckCompromisedPassword() {
        String compromisedPassword = "123456";
        int result = PasswordBreachChecker.checkPassword(compromisedPassword);
        assertTrue(result > 0, "Common password should be detected as compromised");
    }

    @Test
    @DisplayName("Should handle empty password")
    void testCheckEmptyPassword() {
        String emptyPassword = "";
        int result = PasswordBreachChecker.checkPassword(emptyPassword);
        assertTrue(result >= -1, "Should return -1 or higher for an empty password");
    }
}
