package service;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;
import java.security.spec.KeySpec;
import java.util.Base64;

/**
 * EncryptionService provides secure encryption and decryption of sensitive data using AES-GCM.
 * The encryption key is derived from the user's master password and a persistent salt using PBKDF2.
 * The key and salt are only kept in memory for the session and cleared on JVM shutdown.
 * Usage:
 * - After authentication, call setSessionKeyAndSalt(masterPassword, salt) to initialize the session key.
 *   Note: `setSessionKeyAndSalt` must be called before encrypt() or decrypt() to avoid errors.
 * - Use encrypt() and decrypt() for secure data operations.
 * - The persistent salt is managed in encryption_salt.dat.
 * Security Notes:
 * - Keys and salts are cleared from memory at JVM shutdown via a shutdown hook.
 * - AES/GCM/NoPadding is used for encryption, ensuring authenticated encryption.
 */
public class EncryptionService {

	private static String sessionKey = null;
	private static String sessionSalt = null;

	public static void setSessionKeyAndSalt(String key, String salt) {
		sessionKey = key;
		sessionSalt = salt;
	}

	private static SecretKey getSessionSecretKey() throws Exception {
		if (sessionKey == null || sessionSalt == null) {
			throw new IllegalStateException("Session key and salt must be set before encryption/decryption.");
		}
		return getSecretKey(sessionKey, sessionSalt);
	}

	public static void clearSessionKeyAndSalt() {
		sessionKey = null;
		sessionSalt = null;
	}

	// Call this method at JVM shutdown to clear sensitive data from memory
	static {
		Runtime.getRuntime().addShutdownHook(new Thread(EncryptionService::clearSessionKeyAndSalt));
	}

	/**
	 * Generates a SecretKey from a password and salt using PBKDF2 with HMAC SHA-256.
	 *
	 * @param password the password to derive the key from
	 * @param salt     the salt bytes as string
	 * @return a SecretKey suitable for AES encryption
	 * @throws Exception if key generation fails
	 */
	public static SecretKey getSecretKey(String password, String salt) throws Exception {
		byte[] saltBytes = salt.getBytes();
		SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
		KeySpec spec = new PBEKeySpec(password.toCharArray(), saltBytes, 65536, 256);
		SecretKey tmp = factory.generateSecret(spec);
		return new SecretKeySpec(tmp.getEncoded(), "AES");
	}

	/**
	 * Encrypts a plaintext string using AES/CBC/PKCS5Padding.
	 * A random IV is generated and prepended to the encrypted data.
	 * The result is Base64 encoded.
	 *
	 * @param strToEncrypt plaintext string to encrypt
	 * @return Base64 encoded string of IV + encrypted data
	 * @throws Exception if encryption fails
	 */
	public static String encrypt(String strToEncrypt) throws Exception {
		if (strToEncrypt == null) {
			throw new NullPointerException("Input to encrypt cannot be null");
		}
		SecretKey key = getSessionSecretKey();
		Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
		byte[] iv = new byte[12];
		SecureRandom sr = new SecureRandom();
		sr.nextBytes(iv);
		GCMParameterSpec gcmSpec = new GCMParameterSpec(128, iv);
		cipher.init(Cipher.ENCRYPT_MODE, key, gcmSpec);
		byte[] encrypted = cipher.doFinal(strToEncrypt.getBytes());
		byte[] encryptedWithIv = new byte[iv.length + encrypted.length];
		System.arraycopy(iv, 0, encryptedWithIv, 0, iv.length);
		System.arraycopy(encrypted, 0, encryptedWithIv, iv.length, encrypted.length);
		return Base64.getEncoder().encodeToString(encryptedWithIv);
	}

	/**
	 * Decrypts a Base64 encoded string that contains a 16-byte IV prepended to the encrypted data.
	 *
	 * @param strToDecrypt Base64 encoded string containing IV + encrypted data
	 * @return the decrypted plaintext string
	 * @throws Exception if decryption fails
	 */
	public static String decrypt(String strToDecrypt) throws Exception {
		try {
			SecretKey key = getSessionSecretKey();
			byte[] encryptedIvTextBytes = Base64.getDecoder().decode(strToDecrypt);
			if (encryptedIvTextBytes.length < 13) {
				throw new IllegalArgumentException("Invalid encrypted input length");
			}
			byte[] iv = new byte[12];
			System.arraycopy(encryptedIvTextBytes, 0, iv, 0, iv.length);
			byte[] encryptedBytes = new byte[encryptedIvTextBytes.length - iv.length];
			System.arraycopy(encryptedIvTextBytes, iv.length, encryptedBytes, 0, encryptedBytes.length);
			Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
			GCMParameterSpec gcmSpec = new GCMParameterSpec(128, iv);
			cipher.init(Cipher.DECRYPT_MODE, key, gcmSpec);
			byte[] decrypted = cipher.doFinal(encryptedBytes);
			return new String(decrypted);
		} catch (Exception e) {
			throw new Exception("Decryption failed", e);
		}
	}

	// Utility to generate or load a persistent salt for PBKDF2
	public static String getOrCreatePersistentSalt() throws Exception {
		java.nio.file.Path saltPath = java.nio.file.Paths.get("encryption_salt.dat"); // Alterado para .dat
		if (java.nio.file.Files.exists(saltPath)) {
			return java.nio.file.Files.readString(saltPath).trim();
		}
		// Generate a new random salt (16 bytes, base64 encoded)
		byte[] saltBytes = new byte[16];
		new SecureRandom().nextBytes(saltBytes);
		String salt = Base64.getEncoder().encodeToString(saltBytes);
		java.nio.file.Files.writeString(saltPath, salt);
		return salt;
	}

}