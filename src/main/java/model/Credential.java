package model;

public class Credential {
	private String serviceName;
	private String username;
	private String encryptedPassword;

	public Credential(String serviceName, String username, String encryptedPassword) {
		this.serviceName = serviceName;
		this.username = username;
		this.encryptedPassword = encryptedPassword;
	}

	public String getServiceName() {
		return serviceName;
	}

	public String getUsername() {
		return username;
	}

	public String getEncryptedPassword() {
		return encryptedPassword;
	}

	@Override
	public String toString() {
		return "Service: " + serviceName + ", Username: " + username;
	}
}
