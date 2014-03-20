package com.lwolf.wf.api.auth;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.HttpHeaders;

import org.apache.commons.codec.binary.Base64;

import com.lwolf.wf.api.exceptions.PayloadException;

public class Payload {

	private static final String MD5 = "MD5";
	private static final String CONTENT_MD5 = "Content-MD5";
	private static final String CONTENT_TYPE = HttpHeaders.CONTENT_TYPE;
	private static final String DATE = HttpHeaders.DATE;
	
	private final String _payload;
	
	public Payload(HttpServletRequest request, byte[] content) throws PayloadException, NoSuchAlgorithmException, UnsupportedEncodingException {
		
		// build uri
		String uri = request.getRequestURL().toString();
		uri = uri.substring(uri.indexOf("://") + 3);
		uri = uri.substring(uri.indexOf("/"));
		
		// get parts
		String method = request.getMethod();
		String protocol = request.getProtocol();
		String contentType = request.getHeader(CONTENT_TYPE);
		String date = request.getHeader(DATE);
		
		if (contentType == null)
			throw new PayloadException("Missing Content-Type header");
		if (date == null)
			throw new PayloadException("Missing Date header");
		
		// hash and encode the body
		String contentMD5 = md5(content);
		
		// build payload
		StringBuilder payload = new StringBuilder();
		payload.append(method).append(" ");
		payload.append(uri).append(" ");
		payload.append(protocol).append("\n");
		payload.append(CONTENT_MD5).append(": ").append(contentMD5).append("\n");
		payload.append(CONTENT_TYPE).append(": ").append(contentType).append("\n");
		payload.append(DATE).append(": ").append(date.toString());
		
		_payload = payload.toString();
	}
	
	private String md5(byte[] value) throws NoSuchAlgorithmException, UnsupportedEncodingException {
		MessageDigest digest = MessageDigest.getInstance(MD5);
		return Base64.encodeBase64String(digest.digest(value));
	}
	
	public String payload() {
		return _payload;
	}
	
	@Override
	public String toString() {
		return _payload;
	}

}