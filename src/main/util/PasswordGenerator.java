package util;

import java.security.SecureRandom;

public class PasswordGenerator {
	private static final String UPPERCASE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	private static final String LOWERCASE = "abcdefghijklmnopqrstuvwxyz";
	private static final String NUMBERS = "0123456789";
	private static final String SYMBOLS = "!@#$%&*()-_=+[]{}";

	private static final SecureRandom random = new SecureRandom();

	public static String generate(int length, boolean includeUppercase, boolean includeLowercase,
	                              boolean includeNumbers, boolean includeSymbols) {
		StringBuilder characterPool = new StringBuilder();
		if (includeUppercase) characterPool.append(UPPERCASE);
		if (includeLowercase) characterPool.append(LOWERCASE);
		if (includeNumbers) characterPool.append(NUMBERS);
		if (includeSymbols) characterPool.append(SYMBOLS);

		if (characterPool.length() == 0 || length <= 0) {
			throw new IllegalArgumentException("Invalid parameters for password generation.");
		}

		StringBuilder password = new StringBuilder(length);
		for (int i = 0; i < length; i++) {
			int index = random.nextInt(characterPool.length());
			password.append(characterPool.charAt(index));
		}

		return password.toString();
	}
}
