import model.Credential;
import service.AuthService;
import service.CredentialStorage;
import service.CredentialManager;

import java.net.URI;
import java.util.List;
import java.util.Scanner;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class App {

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);

		try {
			new AuthService(scanner); // Initialize authentication
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

	static boolean checkPwned(String prefix, String suffix) throws Exception {
		URI uri = new URI("https", "api.pwnedpasswords.com", "/range/" + prefix, null);
		URL url = uri.toURL();

		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("GET");
		conn.setConnectTimeout(5000);
		conn.setReadTimeout(5000);

		try (BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
			String line;
			while ((line = in.readLine()) != null) {
				String[] parts = line.split(":");
				if (parts.length > 0 && parts[0].equalsIgnoreCase(suffix)) {
					return true; // password found in breach
				}
			}
		}
		return false;
	}
}
