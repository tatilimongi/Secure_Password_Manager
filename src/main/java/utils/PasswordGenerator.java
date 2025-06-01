package utils;

import service.PasswordBreachChecker;
import java.security.SecureRandom;

public class PasswordGenerator {
    private static final String UPPERCASE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String LOWERCASE = "abcdefghijklmnopqrstuvwxyz";
    private static final String NUMBERS = "0123456789";
    private static final String SYMBOLS = "!@#$%&*()-_=+[]{}";
    private static final SecureRandom random = new SecureRandom();

    /**
     * Generates a strong password based on user preferences.
     *
     * @param length           The length of the generated password.
     * @param includeUppercase Whether to include uppercase letters.
     * @param includeLowercase Whether to include lowercase letters.
     * @param includeNumbers   Whether to include numeric digits.
     * @param includeSymbols   Whether to include special characters.
     * @return A randomly generated password as a String.
     */
    public static String generate(int length, boolean includeUppercase, boolean includeLowercase,
                                  boolean includeNumbers, boolean includeSymbols) {
        StringBuilder characterPool = new StringBuilder();
        if (includeUppercase) characterPool.append(UPPERCASE);
        if (includeLowercase) characterPool.append(LOWERCASE);
        if (includeNumbers) characterPool.append(NUMBERS);
        if (includeSymbols) characterPool.append(SYMBOLS);

        if (characterPool.isEmpty() || length <= 0) {
            throw new IllegalArgumentException("Invalid parameters for password generation.");
        }

        String password;
        int breachCount;
        do {
            StringBuilder passwordBuilder = new StringBuilder(length);
            for (int i = 0; i < length; i++) {
                int index = random.nextInt(characterPool.length());
                passwordBuilder.append(characterPool.charAt(index));
            }
            password = passwordBuilder.toString();
            
            // Check with PasswordBreachChecker
            breachCount = PasswordBreachChecker.checkPassword(password);

            if (breachCount > 0) {
                System.out.printf("Generated password found in %d breach(es). Regenerating a safer password...%n", breachCount);
            }
        } while (breachCount > 0); // Regenerate if the password is compromised

        return password;
    }
}