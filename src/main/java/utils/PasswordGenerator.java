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
        String characterPool = buildCharacterPool(includeUppercase, includeLowercase, includeNumbers, includeSymbols);
        validateInput(length, characterPool);
        return generateSecurePassword(length, characterPool);
    }

    private static String buildCharacterPool(boolean includeUppercase, boolean includeLowercase,
                                           boolean includeNumbers, boolean includeSymbols) {
        StringBuilder characterPool = new StringBuilder();
        if (includeUppercase) characterPool.append(UPPERCASE);
        if (includeLowercase) characterPool.append(LOWERCASE);
        if (includeNumbers) characterPool.append(NUMBERS);
        if (includeSymbols) characterPool.append(SYMBOLS);
        return characterPool.toString();
    }

    private static void validateInput(int length, String characterPool) {
        if (characterPool.isEmpty() || length <= 0) {
            throw new IllegalArgumentException("Invalid parameters for password generation.");
        }
    }

    private static String generateSecurePassword(int length, String characterPool) {
        String password;
        int breachCount;
        do {
            password = generateRandomPassword(length, characterPool);
            breachCount = PasswordBreachChecker.checkPassword(password);
            if (breachCount > 0) {
                System.out.printf("Generated password found in %d breach(es). Regenerating a safer password...%n", breachCount);
            }
        } while (breachCount > 0);
        return password;
    }

    private static String generateRandomPassword(int length, String characterPool) {
        StringBuilder passwordBuilder = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(characterPool.length());
            passwordBuilder.append(characterPool.charAt(index));
        }
        return passwordBuilder.toString();
    }
}