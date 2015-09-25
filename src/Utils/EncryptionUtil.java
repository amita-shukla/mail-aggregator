package Utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class EncryptionUtil {
	private static EncryptionUtil instance = new EncryptionUtil();
	private EncryptionUtil(){}
	public static EncryptionUtil getInstance(){
		return instance;
	}
	
	
	// string to hold the name of algorithm
	public  final String ALGORITHM = "RSA";
	// string to hold the name of private key file
	public  final String PRIVATE_KEY_FILE = "C:/Users/user/workspace/Mail_Application/KEYS/private.key";
	// string to hold the name of public key file
	public  final String PUBLIC_KEY_FILE = "C:/Users/user/workspace/Mail_Application/KEYS/public.key";

	/**
	 * Generate key which contains a pair of private and public key using 1024
	 * bytes. Store the set of keys in Public.key and Private.key files.
	 */
	public void generateKey() {
		try {
			final KeyPairGenerator keyGen = KeyPairGenerator
					.getInstance(ALGORITHM);
			keyGen.initialize(1024);
			final KeyPair key = keyGen.generateKeyPair();

			File privateKeyFile = new File(PRIVATE_KEY_FILE);
			File publicKeyFile = new File(PUBLIC_KEY_FILE);

			// Create files to store public and private key
			if (privateKeyFile.getParentFile() != null) {
				privateKeyFile.getParentFile().mkdirs();
			}
			privateKeyFile.createNewFile();

			if (publicKeyFile.getParentFile() != null) {
				publicKeyFile.getParentFile().mkdirs();
			}
			publicKeyFile.createNewFile();

			// Saving the public key in a file
			ObjectOutputStream publicKeyOS = new ObjectOutputStream(
					new FileOutputStream(publicKeyFile));
			publicKeyOS.writeObject(key.getPublic());
			publicKeyOS.close();

			// Saving the private key in a file
			ObjectOutputStream privateKeyOS = new ObjectOutputStream(
					new FileOutputStream(privateKeyFile));
			privateKeyOS.writeObject(key.getPrivate());
			privateKeyOS.close();

		} catch (NoSuchAlgorithmException | IOException e) {
			System.out.println("Exception while generating key pair");
			e.printStackTrace();
		}
	}

	/**
	 * This method checks if a pair of public and private keys has been
	 * generated
	 */

	public  boolean areKeysPresent() {
		File privateKey = new File(PRIVATE_KEY_FILE);
		File publicKey = new File(PUBLIC_KEY_FILE);

		if (privateKey.exists() && publicKey.exists()) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Encrypt the plain text using Public key
	 * 
	 * @throws NoSuchPaddingException
	 * @throws NoSuchAlgorithmException
	 */
	public byte[] encrypt(String text, PublicKey key)
			throws NoSuchAlgorithmException, NoSuchPaddingException {
		
		byte[] cipherText = null;
		try {
			// Get an RSA Cipher object and print the provider
			final Cipher cipher = Cipher.getInstance(ALGORITHM);
			// encrypt the plain text using the public key

			cipher.init(Cipher.ENCRYPT_MODE, key);
			//cipherText = cipher.doFinal(text.getBytes("UTF8"));
			cipherText = cipher.doFinal(text.getBytes("UTF8"));

		} catch (InvalidKeyException | IllegalBlockSizeException | BadPaddingException | UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		return cipherText;
	}
	
	/**
	 * Decrypt using private key
	 * @throws NoSuchPaddingException 
	 * @throws NoSuchAlgorithmException  
	 */
	
	public String decrypt(byte[] text, PrivateKey key) throws NoSuchAlgorithmException, NoSuchPaddingException{
		byte[] decryptedText= null;
		try {
		//Get and RSA object and print the provider
		final Cipher cipher = Cipher.getInstance(ALGORITHM);
		
		//decrypt the text using the private key
		
			cipher.init(Cipher.DECRYPT_MODE, key);
			decryptedText = cipher.doFinal(text);
			System.out.println(decryptedText.toString());
		} catch (InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new String(decryptedText);
	}
	
	/**
	   * Test the EncryptionUtil
	   */
	  public static void main(String[] args) {
		  EncryptionUtil eUtil = EncryptionUtil.getInstance();
	    try {

	      // Check if the pair of keys are present else generate those.
	      if (!eUtil.areKeysPresent()) {
	        // Method generates a pair of keys using the RSA algorithm and stores it
	        // in their respective files
	        eUtil.generateKey();
	      }

	      final String originalText = "Text to be encrypted ";
	      ObjectInputStream inputStream = null;

	      // Encrypt the string using the public key
	      inputStream = new ObjectInputStream(new FileInputStream(eUtil.PUBLIC_KEY_FILE));
	      final PublicKey publicKey = (PublicKey) inputStream.readObject();
	      final byte[] cipherText = eUtil.encrypt(originalText, publicKey);

	      // Decrypt the cipher text using the private key.
	      inputStream = new ObjectInputStream(new FileInputStream(eUtil.PRIVATE_KEY_FILE));
	      final PrivateKey privateKey = (PrivateKey) inputStream.readObject();
	      final String plainText = eUtil.decrypt(cipherText, privateKey);

	      // Printing the Original, Encrypted and Decrypted Text
	      System.out.println("Original: " + originalText);
	      System.out.println("Encrypted: " +cipherText.toString());
	      System.out.println("Decrypted: " + plainText);

	    } catch (Exception e) {
	      e.printStackTrace();
	    }
	  }
}
