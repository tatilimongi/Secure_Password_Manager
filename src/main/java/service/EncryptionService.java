package service;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import javax.crypto.spec.IvParameterSpec;
import java.security.SecureRandom;
import java.security.spec.KeySpec;
import java.util.Base64;

public class EncryptionService {

	private static final String SECRET_KEY = "MyVerySecurePassword123!";
	private static final String SALT = "12345678";

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
		SecretKey key = getSecretKey(SECRET_KEY, SALT);

		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

		// Generate random 16-byte IV
		byte[] iv = new byte[16];
		SecureRandom sr = new SecureRandom();
		sr.nextBytes(iv);
		IvParameterSpec ivSpec = new IvParameterSpec(iv);

		cipher.init(Cipher.ENCRYPT_MODE, key, ivSpec);
		byte[] encrypted = cipher.doFinal(strToEncrypt.getBytes());

		// Combine IV and encrypted bytes
		byte[] encryptedWithIv = new byte[iv.length + encrypted.length];
		System.arraycopy(iv, 0, encryptedWithIv, 0, iv.length);
		System.arraycopy(encrypted, 0, encryptedWithIv, iv.length, encrypted.length);

		// Return Base64 encoded IV + encrypted data
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
		SecretKey key = getSecretKey(SECRET_KEY, SALT);
		byte[] encryptedIvTextBytes = Base64.getDecoder().decode(strToDecrypt);

		// Extract IV from the beginning
		byte[] iv = new byte[16];
		System.arraycopy(encryptedIvTextBytes, 0, iv, 0, iv.length);
		IvParameterSpec ivSpec = new IvParameterSpec(iv);

		// Extract encrypted bytes after the IV
		int encryptedSize = encryptedIvTextBytes.length - iv.length;
		byte[] encryptedBytes = new byte[encryptedSize];
		System.arraycopy(encryptedIvTextBytes, iv.length, encryptedBytes, 0, encryptedSize);

		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		cipher.init(Cipher.DECRYPT_MODE, key, ivSpec);
		byte[] decrypted = cipher.doFinal(encryptedBytes);

		return new String(decrypted);
	}
}
