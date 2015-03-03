package encryptionStuff;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.junit.Assert;


public class GenerateSymmetricMasterKey {
	
	private static final String keyDir = "C:\\Users\\Kennedy\\testfiles";
	private static final String keyName = "secret.key";
	
	
	public static void main(String[] args) throws Exception {
		KeyGenerator symKeyGenerator = KeyGenerator.getInstance("AES");
		symKeyGenerator.init(128);
		
		SecretKey symKey = symKeyGenerator.generateKey();
//		System.out.println("the sym key is" + symKey);
		saveSymmetricKey(keyDir, symKey);
		
		SecretKey symKeyLoaded = loadSymmetricAESKey(keyDir, "AES");
		Assert.assertTrue(Arrays.equals(symKey.getEncoded(), symKeyLoaded.getEncoded()));
			
	}

	private static void saveSymmetricKey(String path, SecretKey secretKey) throws Exception{
		 
		X509EncodedKeySpec x509EncodedKeySpec  =  new X509EncodedKeySpec (secretKey.getEncoded());
		FileOutputStream keyfos = new FileOutputStream(path + "/" + keyName);
		keyfos.write(x509EncodedKeySpec.getEncoded());
		keyfos.close();
		System.out.println("the symmetric key has been saved at " + keyDir);
		
	}

	public static SecretKey loadSymmetricAESKey(String path, String algorithm)  throws IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidKeySpecException{
	
		File keyFile = new File(path + "/" + keyName);
		FileInputStream keyfis =new FileInputStream(keyFile);
		byte [] encodedPrivateKey = new byte [(int) keyFile.length()];
		keyfis.read(encodedPrivateKey);
		keyfis.close();
		
		
		return new SecretKeySpec(encodedPrivateKey, "AES");

	}


	

}
