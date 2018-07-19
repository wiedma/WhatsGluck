package org.Security;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class AESEncrypt {
	
	public static Key schl�sselErzeugen() {
		try {
			KeyGenerator keyGen = KeyGenerator.getInstance("AES");
			keyGen.init(128);
			Key key = keyGen.generateKey();
			return key;
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static String nachrichtVerschl�sseln(Key key, String nachricht) {
		try {
			Cipher cipher = Cipher.getInstance("AES");
			cipher.init(Cipher.ENCRYPT_MODE, key);
			byte[] verschl�sselt = cipher.doFinal(nachricht.getBytes());
			BASE64Encoder myEncoder = new BASE64Encoder();
			String geheim = myEncoder.encode(verschl�sselt);
			return geheim;
		} catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
			e.printStackTrace();
			return null;
		} catch (InvalidKeyException e) {
			e.printStackTrace();
			return null;
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
			return null;
		} catch (BadPaddingException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static String nachrichtEntschl�sseln(Key key, String geheim) {
		try {
			BASE64Decoder myDecoder = new BASE64Decoder();
			byte[] verschl�sselt = myDecoder.decodeBuffer(geheim);
			Cipher cipher = Cipher.getInstance("AES");
			cipher.init(Cipher.DECRYPT_MODE, key);
			byte[] entschl�sselt = cipher.doFinal(verschl�sselt);
			String klar = new String(entschl�sselt);
			return klar;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null;
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
			return null;
		} catch (InvalidKeyException e) {
//			e.printStackTrace();
			return null;
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
			return null;
		} catch (BadPaddingException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static String keyToString(Key key) {
		return new BASE64Encoder().encode(key.getEncoded());
	}
	
	public static Key stringToKey(String keyString) {
		byte[] decodedKey;
		try {
			decodedKey = new BASE64Decoder().decodeBuffer(keyString);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		return new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES");
	}
}
