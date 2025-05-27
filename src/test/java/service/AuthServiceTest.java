package service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.io.ByteArrayInputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mockStatic;

/**
 * Unit tests for {@link AuthService}.
 */
@SuppressWarnings("SpellCheckingInspection")
class AuthServiceTest {

	private static final String MASTER_PASSWORD = "testPassword123";
	private static final Path MASTER_PASSWORD_PATH = Path.of("master_password.txt");
	private static final Path TOTP_SECRET_PATH = Path.of("totp_secret.txt");

	/**
	 * Sets up a valid master password file before each test.
	 */
	@BeforeEach
	void setUp() throws Exception {
		Files.deleteIfExists(MASTER_PASSWORD_PATH);
		Files.writeString(MASTER_PASSWORD_PATH, MASTER_PASSWORD);
		Files.deleteIfExists(TOTP_SECRET_PATH);
	}

	/**
	 * Tests successful authentication with valid master password and valid TOTP code.
	 */
	@Test
	@DisplayName("Should authenticate with correct password and TOTP")
	void testSuccessfulAuthentication() {
		String input = MASTER_PASSWORD + "\n123456\n";
		Scanner scanner = new Scanner(new ByteArrayInputStream(input.getBytes()));

		try (MockedStatic<TOTPService> mockedTOTP = mockStatic(TOTPService.class)) {
			mockedTOTP.when(TOTPService::loadOrCreateSecret).thenReturn("FAKESECRET");
			mockedTOTP.when(() -> TOTPService.validateCode("FAKESECRET", "123456")).thenReturn(true);
			mockedTOTP.when(() -> TOTPService.getBase32Secret("FAKESECRET")).thenReturn("BASE32_FAKESECRET");
			mockedTOTP.when(() -> TOTPService.getOtpAuthUrl("FAKESECRET", "user@example.com", "SecurePasswordManager"))
					.thenReturn("otpauth://mocked");

			assertDoesNotThrow(() -> new AuthService(scanner));
		}
	}

	/**
	 * Tests failed authentication after three incorrect password attempts.
	 */
	@Test
	@DisplayName("Should fail after three wrong password attempts")
	void testFailedAuthentication() {
		String input = "wrong1\n123456\nwrong2\n123456\nwrong3\n123456\n";
		Scanner scanner = new Scanner(new ByteArrayInputStream(input.getBytes()));

		try (MockedStatic<TOTPService> mockedTOTP = mockStatic(TOTPService.class)) {
			mockedTOTP.when(TOTPService::loadOrCreateSecret).thenReturn("FAKESECRET");

			Exception exception = assertThrows(Exception.class, () -> new AuthService(scanner));
			assertEquals("Authentication failed after maximum attempts.", exception.getMessage());
		}
	}

	/**
	 * Tests failed authentication with the correct password but invalid TOTP code.
	 */
	@Test
	@DisplayName("Should fail with correct password but wrong TOTP")
	void testFailedTOTPAuthentication() {
		String input = MASTER_PASSWORD + "\n000000\n" +
				MASTER_PASSWORD + "\n000000\n" +
				MASTER_PASSWORD + "\n000000\n";
		Scanner scanner = new Scanner(new ByteArrayInputStream(input.getBytes()));

		try (MockedStatic<TOTPService> mockedTOTP = mockStatic(TOTPService.class)) {
			mockedTOTP.when(TOTPService::loadOrCreateSecret).thenReturn("FAKESECRET");
			mockedTOTP.when(() -> TOTPService.validateCode("FAKESECRET", "000000")).thenReturn(false);
			mockedTOTP.when(() -> TOTPService.getBase32Secret("FAKESECRET")).thenReturn("BASE32_FAKESECRET");
			mockedTOTP.when(() -> TOTPService.getOtpAuthUrl("FAKESECRET", "user@example.com", "SecurePasswordManager"))
					.thenReturn("otpauth://mocked");

			Exception exception = assertThrows(Exception.class, () -> new AuthService(scanner));
			assertEquals("Authentication failed after maximum attempts.", exception.getMessage());
		}
	}

	/**
	 * Cleans up test files after each test.
	 */
	@AfterEach
	void tearDown() throws Exception {
		Files.deleteIfExists(MASTER_PASSWORD_PATH);
		Files.deleteIfExists(TOTP_SECRET_PATH);
	}
}
