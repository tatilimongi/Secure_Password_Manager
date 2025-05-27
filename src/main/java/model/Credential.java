package model;

/**
 * Represents a saved user credential for a specific service.
 */
public record Credential(String serviceName, String username, String encryptedPassword) {
	/**
	 * Constructs a new Credential.
	 *
	 * @param serviceName       the name of the service (e.g., "Gmail")
	 * @param username          the username associated with the service
	 * @param encryptedPassword the password, already encrypted
	 */
	public Credential {
	}


	@Override
	public String toString() {
		return "Service: " + serviceName + ", Username: " + username;
	}
}
