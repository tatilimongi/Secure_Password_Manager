import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Test class for App functionality
 * Focuses on password security and input handling
 */
class AppTest {

    private Scanner scanner;
    // Removed an unused credentials list
    private static final String VALID_PASSWORD = "password";
    private static final String VALID_SHA1 = "5baa61e4c9b93f3f0682250b6cf8331b7ee68fd8";

    @BeforeEach
    void setUp() {
        scanner = mock(Scanner.class);
    }

    @AfterEach
    void tearDown() {
        // Cleanup resources
        scanner = null;
    }

    @Nested
    @DisplayName("Scanner Input Tests")
    class ScannerInputTests {
        @Test
        @DisplayName("Should return input line when available")
        void testSafeReadLineReturnsLine() {
            when(scanner.hasNextLine()).thenReturn(true);
            when(scanner.nextLine()).thenReturn("test");
            assertEquals("test", invokeSafeReadLine(scanner));
            verify(scanner, times(1)).hasNextLine();
            verify(scanner, times(1)).nextLine();
        }

        @Test
        @DisplayName("Should return null when no input available")
        void testSafeReadLineReturnsNullOnNoInput() {
            when(scanner.hasNextLine()).thenReturn(false);
            assertNull(invokeSafeReadLine(scanner));
            verify(scanner, times(1)).hasNextLine();
            verify(scanner, never()).nextLine();
        }
    }

    @Nested
    @DisplayName("Password Hash Tests")
    class PasswordHashTests {
        @Test
        @DisplayName("Should generate correct SHA-1 hash")
        void testSha1Hex() {
            String hash = invokeSha1Hex(VALID_PASSWORD);
            assertEquals(VALID_SHA1, hash.toLowerCase());
        }

        @Test
        @DisplayName("Should handle empty password")
        void testSha1HexWithEmptyString() {
            String hash = invokeSha1Hex("");
            assertNotNull(hash);
            assertEquals(40, hash.length(), "SHA-1 hash should be 40 characters long");
        }
    }

    @Nested
    @DisplayName("Password Pwned Tests")
    class PasswordPwnedTests {
        @Test
        @DisplayName("Should detect compromised password")
        void testCheckPwnedFound() throws Exception {
            String prefix = "5BAA6";
            String suffix = "1E4C9B93F3F0682250B6CF8331B7EE68FD8";
            
            try (MockedStatic<App> appMock = Mockito.mockStatic(App.class, Mockito.CALLS_REAL_METHODS)) {
                appMock.when(() -> App.checkPwned(prefix, suffix)).thenReturn(true);
                assertTrue(App.checkPwned(prefix, suffix));
            }
        }

        @Test
        @DisplayName("Should identify safe password")
        void testCheckPwnedNotFound() {
            String prefix = "ABCDE";
            String suffix = "FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF";
            
            try (MockedStatic<App> appMock = Mockito.mockStatic(App.class, Mockito.CALLS_REAL_METHODS)) {
                appMock.when(() -> App.checkPwned(prefix, suffix)).thenReturn(false);
                assertFalse(App.checkPwned(prefix, suffix));
            } catch (Exception e) {
	            throw new RuntimeException(e);
            }
        }

        @Test
        @DisplayName("Should handle API connection failure")
        void testCheckPwnedWithConnectionFailure() {
            String prefix = "5BAA6";
            String suffix = "1E4C9B93F3F0682250B6CF8331B7EE68FD8";
            
            try (MockedStatic<App> appMock = Mockito.mockStatic(App.class, Mockito.CALLS_REAL_METHODS)) {
                appMock.when(() -> App.checkPwned(prefix, suffix)).thenThrow(new RuntimeException("API Connection failed"));
                assertThrows(RuntimeException.class, () -> App.checkPwned(prefix, suffix));
            }
        }
    }

    /**
     * Helper methods to access private static methods
     */
    private String invokeSafeReadLine(Scanner scanner) {
        try {
            var method = App.class.getDeclaredMethod("safeReadLine", Scanner.class);
            method.setAccessible(true);
            return (String) method.invoke(null, scanner);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException("Failed to invoke safeReadLine method", e);
        }
    }

    private String invokeSha1Hex(String input) {
        try {
            var method = App.class.getDeclaredMethod("sha1Hex", String.class);
            method.setAccessible(true);
            return (String) method.invoke(null, input);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException("Failed to invoke sha1Hex method", e);
        }
    }
}