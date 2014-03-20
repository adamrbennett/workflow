package com.lwolf.wf.api.auth;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Enumeration;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response.Status;

import org.apache.commons.codec.binary.Base64;
import org.codehaus.jackson.map.ObjectMapper;

import com.lwolf.wf.api.exceptions.AuthenticationException;
import com.lwolf.wf.api.exceptions.PayloadException;
import com.lwolf.wf.exceptions.RepositoryException;
import com.lwolf.wf.repository.KeyRepository;

public class RequestAuthenticator {
	
	private static final String UTF8 = "UTF-8";
	private static final String HMAC_SHA1 = "HmacSHA1";
	private static final String AUTHORIZATION = HttpHeaders.AUTHORIZATION;
	
	private final KeyRepository _keyRepository;
	private final ObjectMapper _mapper = new ObjectMapper();
	
	public RequestAuthenticator(KeyRepository keyRepository) {
		_keyRepository = keyRepository;
	}
	
	public void authenticate(HttpServletRequest request) throws WebApplicationException {
		authenticate(request, new byte[] {});
	}
	
	public void authenticate(HttpServletRequest request, Object obj) throws WebApplicationException {
		try {
			authenticate(request, _mapper.writeValueAsBytes(obj));
		} catch (IOException ex) {
			throw new WebApplicationException(ex, Status.INTERNAL_SERVER_ERROR);
		}
	}
	
	public void authenticate(HttpServletRequest request, byte[] content) throws WebApplicationException {
		try {
			
			// get the authorization header
			Enumeration<String> authorizationHeaders = request.getHeaders(AUTHORIZATION);
			if (authorizationHeaders == null)
				throw new WebApplicationException(new AuthenticationException("Missing Authorization header"), Status.BAD_REQUEST);
			
			String authorizationHeader = null;
			String header = null;
			while (authorizationHeaders.hasMoreElements()) {
				header = authorizationHeaders.nextElement();
				if (header != null && header.startsWith("WF"))
					authorizationHeader = header;
			}
			
			if (authorizationHeader == null)
				throw new WebApplicationException(new AuthenticationException("Missing Authorization header"), Status.BAD_REQUEST);
			
			// get the authorization tokens
			String[] tokens = authorizationHeader.split(":");
			
			// get the api key (remove "WF " prefix)
			String apiKey = tokens[0].substring(3);
			
			// get the secret key
			String secretKey = _keyRepository.get(apiKey);
			
			// build the payload
			Payload payload = new Payload(request, content);
			
			// sign the payload
			String signature = sign(payload, secretKey);
			
			// get the client request signature
			String clientSignature = tokens[1];
			
			// validate signature
			if (!clientSignature.equals(signature))
				throw new WebApplicationException(new AuthenticationException("Invalid request signature"), Status.UNAUTHORIZED);
			
		} catch (PayloadException ex) {
			throw new WebApplicationException(ex, Status.BAD_REQUEST);
		} catch (UnsupportedEncodingException ex) {
			throw new WebApplicationException(ex, Status.INTERNAL_SERVER_ERROR);
		} catch (NoSuchAlgorithmException ex) {
			throw new WebApplicationException(ex, Status.INTERNAL_SERVER_ERROR);
		} catch (InvalidKeyException ex) {
			throw new WebApplicationException(ex, Status.INTERNAL_SERVER_ERROR);
		} catch (RepositoryException ex) {
			throw new WebApplicationException(ex, Status.INTERNAL_SERVER_ERROR);
		}
	}
	
	private String sign(Payload payload, String key) throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException {
		// get hmac-sha1 key
		byte[] keyBytes = key.getBytes(UTF8);
		SecretKeySpec signingKey = new SecretKeySpec(keyBytes, HMAC_SHA1);
		
		// initialize hmac-sha1 mac
		Mac mac = Mac.getInstance(HMAC_SHA1);
		mac.init(signingKey);
		
		// compute hmac
		byte[] hmac = mac.doFinal(payload.payload().getBytes(UTF8));
		
		// encode hmac
		return Base64.encodeBase64String(hmac);
	}
}
