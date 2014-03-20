package com.lwolf.wf.admin.auth.impl;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

import com.lwolf.wf.admin.auth.Payload;
import com.lwolf.wf.admin.auth.RequestSigner;
import com.lwolf.wf.admin.exceptions.SigningException;
import com.lwolf.wf.admin.utils.ApplicationProperties;

public class WorkflowRequestSigner implements RequestSigner {
	
	private static final String UTF8 = "UTF-8";
	private static final String HMAC_SHA1 = "HmacSHA1";
	
	@Override
	public String sign(Payload payload) throws SigningException {
		try {
			
			// get hmac-sha1 key
			byte[] keyBytes = ApplicationProperties.workflowSecretKey().getBytes(UTF8);
			SecretKeySpec signingKey = new SecretKeySpec(keyBytes, HMAC_SHA1);
			
			// initialize hmac-sha1 mac
			Mac mac = Mac.getInstance(HMAC_SHA1);
			mac.init(signingKey);
			
			// compute hmac
			byte[] hmac = mac.doFinal(payload.payload().getBytes(UTF8));
			
			// encode hmac
			return Base64.encodeBase64String(hmac);
		} catch (InvalidKeyException ex) {
			throw new SigningException(ex);
		} catch (UnsupportedEncodingException ex) {
			throw new SigningException(ex);
		} catch (IOException ex) {
			throw new SigningException(ex);
		} catch (NoSuchAlgorithmException ex) {
			throw new SigningException(ex);
		}
	}
	
}
