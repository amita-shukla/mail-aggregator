package Utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Hash {

	private static Hash instance = new Hash();
	private Hash(){}
	public static Hash getInstance(){
		return instance;
	}
	
	public String generateHash(String value){
		MessageDigest md;
		StringBuffer hexString=null;
		try {
			md = MessageDigest.getInstance("SHA-256");
			md.update(value.getBytes());
			byte byteData[] = md.digest();
			// convert the byte to hex format
			hexString = new StringBuffer();
			for (int i = 0; i < byteData.length; i++) {
				String hex = Integer.toHexString(0xff & byteData[i]);
				if (hex.length() == 1)
					hexString.append('0');
				hexString.append(hex);
			}
//			String temp_password = hexString.toString();
			

		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			System.out.println("Exception while Hashing");
			e.printStackTrace();
		}
		return hexString.toString();
			}
}
