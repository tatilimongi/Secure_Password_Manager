package service;

import javax.crypto.*;
import javax.crypto.spec.*;
import java.security.*;
import java.util.Base64;

public class EncryptionService {

	private static final String SECRET_KEY = "MyVerySecurePassword123!"; // você pode pedir essa senha no login
	private static final String SALT = "12345678"; // deve ser salva com segurança

	public static SecretKey getSecretKey(String password, String salt) throws Exception {
		byte[] saltBytes = salt.getBytes();
		SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
		KeySpec spec = new PBEKeySpec(password.toCharArray(), saltBytes, 65536, 256);
		SecretKey tmp = factory.generateSecret(spec);
		return new SecretKeySpec(tmp.getEncoded(), "AES");
	}

	public static String encrypt(String strToEncrypt) throws Exception {
		SecretKey key = getSecretKey(SECRET_KEY, SALT);
		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		byte[] iv = new byte[16];
		SecureRandom sr = new SecureRandom();
		sr.nextBytes(iv);
		IvParameterSpec ivSpec = new IvParameterSpec(iv);

		cipher.init(Cipher.ENCRYPT_MODE, key, ivSpec);
		byte[] encrypted = cipher.doFinal(strToEncrypt.getBytes());

		// Retornar IV + texto criptografado como Base64
		byte[] encryptedWithIv = new byte[iv.length + encrypted.length];
		System.arraycopy(iv, 0, encryptedWithIv, 0, iv.length);
		System.arraycopy(encrypted, 0, encryptedWithIv, iv.length, encrypted.length);

		return Base64.getEncoder().encodeToString(encryptedWithIv);
	}

	public static String decrypt(String strToDecrypt) throws Exception {
		SecretKey key = getSecretKey(SECRET_KEY, SALT);
		byte[] encryptedIvTextBytes = Base64.getDecoder().decode(strToDecrypt);

		byte[] iv = new byte[16];
		System.arraycopy(encryptedIvTextBytes, 0, iv, 0, iv.length);
		IvParameterSpec ivSpec = new IvParameterSpec(iv);

		int encryptedSize = encryptedIvTextBytes.length - iv.length;
		byte[] encryptedBytes = new byte[encryptedSize];
		System.arraycopy(encryptedIvTextBytes, iv.length, encryptedBytes, 0, encryptedSize);

		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		cipher.init(Cipher.DECRYPT_MODE, key, ivSpec);
		byte[] decrypted = cipher.doFinal(encryptedBytes);

		return new String(decrypted);
	}
}
