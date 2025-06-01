package utils;

public class InputSanitizer {
    /**
     * Private constructor to prevent instantiation.
     */
    private InputSanitizer() {
        // Utility class, should not be instantiated
    }

    /**
     * Sanitizes user-provided input to prevent potential injection attacks.
     *
     * @param input           The raw user input.
     * @param maxLength       The maximum allowed length of input.
     * @param numericOnly     Whether to allow only numbers.
     * @return Sanitized and safe user input.
     * @throws IllegalArgumentException If input is null, invalid, or unsafe.
     */
    public static String sanitize(String input, int maxLength, boolean numericOnly) throws IllegalArgumentException {
        if (input == null) {
            throw new IllegalArgumentException("Input cannot be null.");
        }
        input = input.trim();
        if (input.isEmpty() || input.length() > maxLength) {
            throw new IllegalArgumentException("Input is invalid or exceeds allowed length.");
        }
        if (numericOnly && !input.matches("\\d+")) {
            throw new IllegalArgumentException("Input must contain only numeric characters.");
        }
        if (!numericOnly && input.indexOf(';') >= 0 || 
                    input.indexOf('\'') >= 0 ||
                    input.indexOf('"') >= 0 ||
                    input.indexOf('<') >= 0 ||
                    input.indexOf('>') >= 0 ||
                    input.indexOf(',') >= 0) {
            throw new IllegalArgumentException("Input contains unsafe characters.");
        }
        return input;
    }

    /**
     * Escapes potentially unsafe input for safe logging.
     *
     * @param input The user-provided input.
     * @return Input with unsafe characters escaped.
     */
    public static String escapeForLog(String input) {
        if (input == null) {
            return null;
        }
        return input.replace("&", "&amp;")
                   .replace("<", "&lt;")
                   .replace(">", "&gt;")
                   .replace("\"", "&quot;")
                   .replace("'", "&#39;");
    }
}