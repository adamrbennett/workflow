package com.lwolf.wf.util.security;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import sun.misc.BASE64Encoder;

public class KeyGenerator {

	public static void main(String[] args) {
		try {
			
			String apiKey = generateKey();
			String secretKey = generateKey();
			
			System.out.println(apiKey);
			System.out.println(secretKey);
			
			System.exit(0);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			System.exit(1);
		} catch (InvalidKeyException e) {
			e.printStackTrace();
			System.exit(2);
		}
	}
	
	private static String generateKey() throws NoSuchAlgorithmException, InvalidKeyException {
		SecureRandom random = new SecureRandom();
		
		byte[] signerBytes = new byte[1024];
		byte[] keyBytes = new byte[1024];
		
		random.nextBytes(signerBytes);
		
		// get hmac-sha1 key
		SecretKeySpec signingKey = new SecretKeySpec(signerBytes, "HmacSHA1");
		
		// initialize hmac-sha1 mac
		Mac mac = Mac.getInstance("HmacSHA1");
		mac.init(signingKey);
		
		// compute hmac
		byte[] hmac = mac.doFinal(keyBytes);
		
		// encode hmac
		BASE64Encoder encoder = new BASE64Encoder();
		return encoder.encode(hmac);
	}

}
