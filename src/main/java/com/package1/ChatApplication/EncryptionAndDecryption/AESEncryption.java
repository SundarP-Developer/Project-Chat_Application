package com.package1.ChatApplication.EncryptionAndDecryption;

import java.util.Base64;	
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AESEncryption {

	@Value("${aes.secret.key}")
	private  String key;
	
	private static final String ALGORITHM = "AES";
	
	public String encrypt(String data) {
		try {
			SecretKeySpec secretKey= new SecretKeySpec(key.getBytes(), ALGORITHM);
			Cipher cipher = Cipher.getInstance(ALGORITHM);
			cipher.init(Cipher.ENCRYPT_MODE, secretKey);
			byte[] encrypted=cipher.doFinal(data.getBytes());
			return Base64.getEncoder().encodeToString(encrypted);
		}
		catch(Exception e){
			throw new RuntimeException("Error encrypting", e);
		}
	}
	
	public String decrypt(String encryptedData) {
		try {
			SecretKeySpec secretKey=new SecretKeySpec(key.getBytes(),ALGORITHM);
			Cipher cipher=Cipher.getInstance(ALGORITHM);
			cipher.init(Cipher.DECRYPT_MODE, secretKey);
			byte[] decrypted=cipher.doFinal(Base64.getDecoder().decode(encryptedData));
			return new String(decrypted);
		}
		catch(Exception e) {
			throw new RuntimeException("Error decryption "+e);
		}
	}
}
