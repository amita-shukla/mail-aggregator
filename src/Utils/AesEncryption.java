/**
 * http://techie-experience.blogspot.in/2012/10/encryption-and-decryption-using-aes.html
 */

package Utils;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

public class AesEncryption {

	public static AesEncryption instance = new AesEncryption();

	private AesEncryption() {
	}

	public static AesEncryption getInstance() {
		return instance;
	}

	private static byte[] key = {
        0x74, 0x68, 0x69, 0x73, 0x49, 0x73, 0x41, 0x53, 0x65, 0x63, 0x72, 0x65, 0x74, 0x4b, 0x65, 0x79
};


	// string to hold the name of key file
	public final String KEY_FILE = "C:/Users/user/workspace/AesEncryption/key/symmetric.key";

	public String encrypt(String plainText)
			throws Exception {

		// encrypt the message
		Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        final SecretKeySpec secretKey = new SecretKeySpec(key, "AES");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        final String encryptedString = Base64.encodeBase64String(cipher.doFinal(plainText.getBytes("UTF-8")));
        return encryptedString;
	}

	//@SuppressWarnings("static-access")
	public String decrypt(String encryptedText)
			throws Exception {
		Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        final SecretKeySpec secretKey = new SecretKeySpec(key, "AES");
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        final String decryptedString = new String(cipher.doFinal( Base64.decodeBase64(encryptedText.getBytes("UTF-8"))));
        return decryptedString;

		
	}


}