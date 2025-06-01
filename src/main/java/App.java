import model.Credential;
import service.AuthService;
import service.CredentialStorage;
import service.CredentialManager;
import utils.InputSanitizer;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Scanner;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class App {

    /**
     * Main entry point of the Secure Password Manager.
     * Handles authentication and interacts with the user via the command-line interface.
     *
     * @param args Command-line arguments (currently unused).
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        try {
            new AuthService(scanner);
        } catch (Exception e) {
            System.err.println("Authentication failed: " + e.getMessage());
            return;
        }

        List<Credential> credentials;
        try {
            credentials = CredentialStorage.loadCredentials();
        } catch (Exception e) {
            System.err.println("Failed to load credentials: " + e.getMessage());
            return;
        }

        CredentialManager manager = new CredentialManager(credentials);
        manager.showMenu();
    }

    /**
     * Checks if a password hash suffix has been found in known data breaches
     * using the Have I Been Pwned (HIBP) API.
     * The API implements k-anonymity, and only the SHA-1 hash prefix
     * is sent for checking.
     *
     * @param prefix The first 5 characters of the SHA-1 hash of the password.
     * @param suffix The remaining characters of the SHA-1 hash of the password.
     * @return {@code true} if the suffix was found in breaches; {@code false} otherwise.
     * @throws Exception If validation or connection fails.
     */
    static boolean checkPwned(String prefix, String suffix) throws Exception {
        try {
            prefix = InputSanitizer.sanitize(prefix, 5, false);
            suffix = InputSanitizer.sanitize(suffix, 100, false);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Input validation failed: " + e.getMessage());
        }

        HttpURLConnection conn = getHttpURLConnection(prefix, suffix);

        try (BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
            String line;
            while ((line = in.readLine()) != null) {
                String[] parts = line.split(":");
                if (parts.length > 0 && parts[0].equalsIgnoreCase(suffix)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Configures an HTTP connection to query the HIBP API for password breach information.
     *
     * @param prefix The first 5 characters of the SHA-1 hash of the password.
     * @param suffix The remaining characters of the SHA-1 hash (used for validation only).
     * @return A configured {@link HttpURLConnection} object ready for querying the API.
     * @throws URISyntaxException If the constructed URI is invalid.
     * @throws IOException If the connection fails.
     */
    private static HttpURLConnection getHttpURLConnection(String prefix, String suffix)
            throws URISyntaxException, IOException {

        if (!prefix.matches("[A-Fa-f0-9]{5}")) {
            throw new IllegalArgumentException("Prefix must contain exactly 5 hexadecimal characters.");
        }
        if (!suffix.matches("[A-Fa-f0-9]+")) {
            throw new IllegalArgumentException("Suffix must contain only hexadecimal characters.");
        }

        URI uri = new URI("https", "api.pwnedpasswords.com", "/range/" + prefix, null);
        URL url = uri.toURL();

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setConnectTimeout(5000);
        conn.setReadTimeout(5000);
        return conn;
    }
}